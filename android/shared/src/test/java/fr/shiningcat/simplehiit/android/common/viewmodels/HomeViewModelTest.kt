package fr.shiningcat.simplehiit.android.shared.home

import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.home.HomeDialog
import fr.shiningcat.simplehiit.sharedui.home.HomePresenter
import fr.shiningcat.simplehiit.sharedui.home.HomeViewState
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeViewModelTest : AbstractMockkTest() {
    private val mockHomePresenter = mockk<HomePresenter>()
    private val testDispatcher = StandardTestDispatcher()

    private val testViewStateFlow = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    private val testDialogStateFlow = MutableStateFlow<HomeDialog>(HomeDialog.None)
    private val testUser = User(id = 1L, name = "Test User", selected = false)

    private lateinit var testedViewModel: HomeViewModel

    @BeforeEach
    fun setUpDispatcher() {
        Dispatchers.setMain(testDispatcher)
        every { mockHomePresenter.screenViewState } returns testViewStateFlow
        every { mockHomePresenter.dialogState } returns testDialogStateFlow

        testedViewModel =
            HomeViewModel(
                homePresenter = mockHomePresenter,
                mainDispatcher = testDispatcher,
            )
    }

    @AfterEach
    fun tearDownDispatcher() {
        Dispatchers.resetMain()
    }

    @Test
    fun `screenViewState exposes presenter state`() =
        runTest(testDispatcher) {
            val testViewState =
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users = emptyList(),
                    totalSessionLengthFormatted = "20mn",
                )

            // Launch collector to activate the StateFlow
            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    testedViewModel.screenViewState.collect {}
                }

            testViewStateFlow.value = testViewState
            advanceUntilIdle()

            assertEquals(testViewState, testedViewModel.screenViewState.value)
            job.cancel()
        }

    @Test
    fun `dialogViewState exposes presenter dialog state`() =
        runTest(testDispatcher) {
            // Launch collector to activate the StateFlow
            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    testedViewModel.dialogViewState.collect {}
                }

            testDialogStateFlow.value = HomeDialog.ConfirmWholeReset
            advanceUntilIdle()

            assertEquals(HomeDialog.ConfirmWholeReset, testedViewModel.dialogViewState.value)
            job.cancel()
        }

    @Test
    fun `modifyNumberCycles delegates to presenter with current view state`() =
        runTest(testDispatcher) {
            val testViewState =
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users = emptyList(),
                    totalSessionLengthFormatted = "20mn",
                )

            // Launch collector to activate the StateFlow
            val job =
                launch(UnconfinedTestDispatcher(testScheduler)) {
                    testedViewModel.screenViewState.collect {}
                }

            testViewStateFlow.value = testViewState
            advanceUntilIdle()

            coEvery {
                mockHomePresenter.modifyNumberCycles(
                    testViewState,
                    CyclesModification.INCREASE,
                )
            } just Runs

            testedViewModel.modifyNumberCycles(CyclesModification.INCREASE)
            advanceUntilIdle()

            coVerify(exactly = 1) {
                mockHomePresenter.modifyNumberCycles(
                    testViewState,
                    CyclesModification.INCREASE,
                )
            }

            job.cancel()
        }

    @Test
    fun `toggleSelectedUser delegates to presenter`() =
        runTest(testDispatcher) {
            coEvery { mockHomePresenter.toggleUserSelection(testUser) } just Runs

            testedViewModel.toggleSelectedUser(testUser)
            advanceUntilIdle()

            coVerify(exactly = 1) { mockHomePresenter.toggleUserSelection(testUser) }
        }

    @Test
    fun `resetWholeApp delegates to presenter showResetConfirmation`() =
        runTest(testDispatcher) {
            coEvery { mockHomePresenter.showResetConfirmation() } just Runs

            testedViewModel.resetWholeApp()
            advanceUntilIdle()

            coVerify(exactly = 1) { mockHomePresenter.showResetConfirmation() }
        }

    @Test
    fun `resetWholeAppConfirmationDeleteEverything delegates to presenter resetWholeApp`() =
        runTest(testDispatcher) {
            coEvery { mockHomePresenter.resetWholeApp() } just Runs

            testedViewModel.resetWholeAppConfirmationDeleteEverything()
            advanceUntilIdle()

            coVerify(exactly = 1) { mockHomePresenter.resetWholeApp() }
        }

    @Test
    fun `cancelDialog delegates to presenter dismissDialog`() =
        runTest(testDispatcher) {
            coEvery { mockHomePresenter.dismissDialog() } just Runs

            testedViewModel.cancelDialog()
            advanceUntilIdle()

            coVerify(exactly = 1) { mockHomePresenter.dismissDialog() }
        }
}

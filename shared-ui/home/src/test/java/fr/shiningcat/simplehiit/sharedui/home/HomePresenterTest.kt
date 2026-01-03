package fr.shiningcat.simplehiit.sharedui.home

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.home.usecases.ModifyNumberCyclesUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomePresenterTest : AbstractMockkTest() {
    private val mockHomeInteractor = mockk<HomeInteractor>()
    private val mockHomeViewStateMapper = mockk<HomeViewStateMapper>()
    private val mockModifyNumberCyclesUseCase = mockk<ModifyNumberCyclesUseCase>()

    private val homeSettingsFlow = MutableSharedFlow<Output<HomeSettings>>(replay = 1)
    private val testUser = User(id = 1L, name = "Test User", selected = false)

    private lateinit var testedPresenter: HomePresenter

    @BeforeEach
    fun setUp() {
        every { mockHomeInteractor.getHomeSettings() } returns homeSettingsFlow
        every { mockHomeViewStateMapper.map(any()) } returns HomeViewState.Loading

        testedPresenter =
            HomePresenter(
                homeInteractor = mockHomeInteractor,
                homeViewStateMapper = mockHomeViewStateMapper,
                modifyNumberCyclesUseCase = mockModifyNumberCyclesUseCase,
                logger = mockHiitLogger,
            )
    }

    @Test
    fun `getScreenViewState returns mapped flow from interactor`() =
        runTest {
            val testHomeSettings =
                HomeSettings(
                    numberCumulatedCycles = 5,
                    cycleLengthMs = 240000L,
                    users = emptyList(),
                    warning = null,
                )
            val testViewState =
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users = emptyList(),
                    totalSessionLengthFormatted = "20mn",
                )

            every { mockHomeViewStateMapper.map(Output.Success(testHomeSettings)) } returns testViewState

            // Emit first (with replay=1, collector will receive it)
            homeSettingsFlow.emit(Output.Success(testHomeSettings))

            val stateFlow = testedPresenter.screenViewState

            // Now collect - should immediately get the replayed value
            val result = stateFlow.first()

            assertEquals(testViewState, result)
            coVerify(exactly = 1) { mockHomeInteractor.getHomeSettings() }
            coVerify(exactly = 1) { mockHomeViewStateMapper.map(Output.Success(testHomeSettings)) }
        }

    @Test
    fun `getDialogState initially returns None`() =
        runTest {
            val dialogState = testedPresenter.dialogState.first()

            assertEquals(HomeDialog.None, dialogState)
        }

    @Test
    fun `modifyNumberCycles with Nominal state and successful use case calls interactor`() =
        runTest {
            val currentViewState =
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users = emptyList(),
                    totalSessionLengthFormatted = "20mn",
                )
            val modification = CyclesModification.INCREASE

            every {
                mockModifyNumberCyclesUseCase.execute(5, CyclesModification.INCREASE)
            } returns ModifyNumberCyclesUseCase.Result.Success(6)
            coEvery { mockHomeInteractor.setTotalRepetitionsNumber(6) } just Runs

            testedPresenter.modifyNumberCycles(currentViewState, modification)

            coVerify(exactly = 1) { mockModifyNumberCyclesUseCase.execute(5, CyclesModification.INCREASE) }
            coVerify(exactly = 1) { mockHomeInteractor.setTotalRepetitionsNumber(6) }
        }

    @Test
    fun `modifyNumberCycles with non-Nominal state does not call interactor`() =
        runTest {
            val currentViewState = HomeViewState.Loading
            val modification = CyclesModification.INCREASE

            testedPresenter.modifyNumberCycles(currentViewState, modification)

            coVerify(exactly = 0) { mockModifyNumberCyclesUseCase.execute(any(), any()) }
            coVerify(exactly = 0) { mockHomeInteractor.setTotalRepetitionsNumber(any()) }
        }

    @Test
    fun `modifyNumberCycles with Error state does not call interactor`() =
        runTest {
            val currentViewState = HomeViewState.Error(errorCode = "ERROR")
            val modification = CyclesModification.DECREASE

            testedPresenter.modifyNumberCycles(currentViewState, modification)

            coVerify(exactly = 0) { mockModifyNumberCyclesUseCase.execute(any(), any()) }
            coVerify(exactly = 0) { mockHomeInteractor.setTotalRepetitionsNumber(any()) }
        }

    @Test
    fun `modifyNumberCycles with WouldBeNonPositive error does not call interactor`() =
        runTest {
            val currentViewState =
                HomeViewState.Nominal(
                    numberCumulatedCycles = 1,
                    cycleLength = "4mn",
                    users = emptyList(),
                    totalSessionLengthFormatted = "4mn",
                )
            val modification = CyclesModification.DECREASE

            every {
                mockModifyNumberCyclesUseCase.execute(1, CyclesModification.DECREASE)
            } returns ModifyNumberCyclesUseCase.Result.Error.WouldBeNonPositive

            testedPresenter.modifyNumberCycles(currentViewState, modification)

            coVerify(exactly = 1) { mockModifyNumberCyclesUseCase.execute(1, CyclesModification.DECREASE) }
            coVerify(exactly = 0) { mockHomeInteractor.setTotalRepetitionsNumber(any()) }
        }

    @Test
    fun `toggleUserSelection toggles user state and calls interactor`() =
        runTest {
            val user = testUser.copy(selected = false)
            val expectedToggledUser = user.copy(selected = true)

            coEvery { mockHomeInteractor.toggleUserSelected(expectedToggledUser) } returns Output.Success(1)

            testedPresenter.toggleUserSelection(user)

            coVerify(exactly = 1) { mockHomeInteractor.toggleUserSelected(expectedToggledUser) }
        }

    @Test
    fun `toggleUserSelection with already selected user toggles to false`() =
        runTest {
            val user = testUser.copy(selected = true)
            val expectedToggledUser = user.copy(selected = false)

            coEvery { mockHomeInteractor.toggleUserSelected(expectedToggledUser) } returns Output.Success(1)

            testedPresenter.toggleUserSelection(user)

            coVerify(exactly = 1) { mockHomeInteractor.toggleUserSelected(expectedToggledUser) }
        }

    @Test
    fun `showResetConfirmation emits ConfirmWholeReset dialog state`() =
        runTest {
            testedPresenter.showResetConfirmation()

            val dialogState = testedPresenter.dialogState.first()
            assertTrue(dialogState is HomeDialog.ConfirmWholeReset)
        }

    @Test
    fun `dismissDialog emits None dialog state`() =
        runTest {
            testedPresenter.showResetConfirmation()
            testedPresenter.dismissDialog()

            val dialogState = testedPresenter.dialogState.first()
            assertEquals(HomeDialog.None, dialogState)
        }

    @Test
    fun `resetWholeApp calls interactor`() =
        runTest {
            coEvery { mockHomeInteractor.resetWholeApp() } just Runs

            testedPresenter.resetWholeApp()

            coVerify(exactly = 1) { mockHomeInteractor.resetWholeApp() }
        }

    @Test
    fun `dialog state flow works correctly through sequence of operations`() =
        runTest {
            // Initially None
            assertEquals(HomeDialog.None, testedPresenter.dialogState.first())

            // Show confirmation
            testedPresenter.showResetConfirmation()
            assertTrue(testedPresenter.dialogState.first() is HomeDialog.ConfirmWholeReset)

            // Dismiss
            testedPresenter.dismissDialog()
            assertEquals(HomeDialog.None, testedPresenter.dialogState.first())
        }
}

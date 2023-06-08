package fr.shining_cat.simplehiit.android.mobile.ui.home

import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.HomeSettings
import fr.shining_cat.simplehiit.domain.common.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.SetTotalRepetitionsNumberUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ToggleUserSelectedUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ValidateInputNumberCyclesUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class HomeInteractorImplTest : AbstractMockkTest() {

    private val mockGetHomeSettingsUseCase = mockk<GetHomeSettingsUseCase>()
    private val mockSetTotalRepetitionsNumberUseCase = mockk<SetTotalRepetitionsNumberUseCase>()
    private val mockToggleUserSelectedUseCase = mockk<ToggleUserSelectedUseCase>()
    private val mockResetWholeAppUseCase = mockk<ResetWholeAppUseCase>()
    private val mockValidateInputNumberCyclesUseCase = mockk<ValidateInputNumberCyclesUseCase>()

    private val settingsFlow = MutableSharedFlow<Output<HomeSettings>>()
    private val testUser = User(name= "test user name")
    private val testReturnInt = 123

    private val testedInteractor = HomeInteractorImpl(
        mockGetHomeSettingsUseCase,
        mockSetTotalRepetitionsNumberUseCase,
        mockToggleUserSelectedUseCase,
        mockResetWholeAppUseCase,
        mockValidateInputNumberCyclesUseCase
    )

    @BeforeEach
    fun setUpMock() {
        coEvery {mockGetHomeSettingsUseCase.execute()} answers {settingsFlow}
        coEvery {mockSetTotalRepetitionsNumberUseCase.execute(any())} just Runs
        coEvery {mockToggleUserSelectedUseCase.execute(any())} returns Output.Success(testReturnInt)
        coEvery {mockResetWholeAppUseCase.execute()} just Runs
        coEvery {mockValidateInputNumberCyclesUseCase.execute(any())} returns Constants.InputError.NONE
    }

    @Test
    fun `calls on interactor getHomeSettings calls GetHomeSettingsUseCase`() = runTest(UnconfinedTestDispatcher()) {
        val result = testedInteractor.getHomeSettings()
        coVerify(exactly = 1) { mockGetHomeSettingsUseCase.execute() }
        assertEquals(settingsFlow, result)
    }

    @Test
    fun `calls on interactor setTotalRepetitionsNumber calls SetTotalRepetitionsNumberUseCase`() = runTest(UnconfinedTestDispatcher()) {
        testedInteractor.setTotalRepetitionsNumber(34)
        coVerify(exactly = 1) { mockSetTotalRepetitionsNumberUseCase.execute(34) }
    }

    @Test
    fun `calls on interactor toggleUserSelected calls GetHomeSettingsUseCase`() = runTest(UnconfinedTestDispatcher()) {
        val result = testedInteractor.toggleUserSelected(testUser)
        coVerify(exactly = 1) { mockToggleUserSelectedUseCase.execute(testUser) }
        assertTrue(result is Output.Success)
        result as Output.Success
        assertEquals(testReturnInt, result.result)
    }

    @Test
    fun `calls on interactor resetWholeApp calls ResetWholeAppUseCase`() = runTest(UnconfinedTestDispatcher()) {
        testedInteractor.resetWholeApp()
        coVerify(exactly = 1) { mockResetWholeAppUseCase.execute() }
    }


    @Test
    fun `calls on interactor validateInputNumberCycles calls ValidateInputNumberCyclesUseCase`() = runTest(UnconfinedTestDispatcher()) {
        val result = testedInteractor.validateInputNumberCycles("test input string")
        coVerify(exactly = 1) { mockValidateInputNumberCyclesUseCase.execute("test input string") }
        assertEquals(Constants.InputError.NONE, result)
    }

}
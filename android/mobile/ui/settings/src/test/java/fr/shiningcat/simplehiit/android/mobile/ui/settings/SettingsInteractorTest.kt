package fr.shiningcat.simplehiit.android.mobile.ui.settings

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.settings.usecases.CreateUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.DeleteUserUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.GetGeneralSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ResetAllSettingsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SaveSelectedExerciseTypesUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetAppLanguageUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetAppThemeUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetBeepSoundUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetPeriodStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetRestPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetSessionStartCountDownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.SetWorkPeriodLengthUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ToggleExerciseTypeInListUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.UpdateUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputPeriodStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputSessionStartCountdownUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateInputUserNameUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidateNumberOfWorkPeriodsUseCase
import fr.shiningcat.simplehiit.domain.settings.usecases.ValidatePeriodLengthUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
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
internal class SettingsInteractorTest : AbstractMockkTest() {
    private val mockGetGeneralSettingsUseCase = mockk<GetGeneralSettingsUseCase>()
    private val mockSetWorkPeriodLengthUseCase = mockk<SetWorkPeriodLengthUseCase>()
    private val mockSetRestPeriodLengthUseCase = mockk<SetRestPeriodLengthUseCase>()
    private val mockSetNumberOfWorkPeriodsUseCase = mockk<SetNumberOfWorkPeriodsUseCase>()
    private val mockSetBeepSoundUseCase = mockk<SetBeepSoundUseCase>()
    private val mockSetSessionStartCountDownUseCase = mockk<SetSessionStartCountDownUseCase>()
    private val mockSetPeriodStartCountDownUseCase = mockk<SetPeriodStartCountDownUseCase>()
    private val mockUpdateUserNameUseCase = mockk<UpdateUserNameUseCase>()
    private val mockDeleteUserUseCase = mockk<DeleteUserUseCase>()
    private val mockCreateUserUseCase = mockk<CreateUserUseCase>()
    private val mockSaveSelectedExerciseTypesUseCase = mockk<SaveSelectedExerciseTypesUseCase>()
    private val mockSetAppLanguageUseCase = mockk<SetAppLanguageUseCase>()
    private val mockSetAppThemeUseCase = mockk<SetAppThemeUseCase>()
    private val mockResetAllSettingsUseCase = mockk<ResetAllSettingsUseCase>()
    private val mockValidatePeriodLengthUseCase = mockk<ValidatePeriodLengthUseCase>()
    private val mockValidateNumberOfWorkPeriodsUseCase = mockk<ValidateNumberOfWorkPeriodsUseCase>()
    private val mockValidateInputSessionStartCountdownUseCase =
        mockk<ValidateInputSessionStartCountdownUseCase>()
    private val mockValidateInputPeriodStartCountdownUseCase =
        mockk<ValidateInputPeriodStartCountdownUseCase>()
    private val mockValidateInputUserNameUseCase = mockk<ValidateInputUserNameUseCase>()
    private val mockToggleExerciseTypeInListUseCase = mockk<ToggleExerciseTypeInListUseCase>()

    private val settingsFlow = MutableSharedFlow<Output<GeneralSettings>>()
    private val testUser = User(name = "test user name")
    private val testUsersList =
        listOf(User(name = "test user 1"), User(name = "test user 2"), User(name = "test user 3"))
    private val testString = "this is a test string"
    private val testReturnInt = 123
    private val testReturnLong = 123L
    private val testListExercises =
        listOf(
            ExerciseTypeSelected(ExerciseType.CAT, true),
            ExerciseTypeSelected(ExerciseType.LUNGE, true),
        )

    private val testedInteractor =
        SettingsInteractorImpl(
            mockGetGeneralSettingsUseCase,
            mockSetWorkPeriodLengthUseCase,
            mockSetRestPeriodLengthUseCase,
            mockSetNumberOfWorkPeriodsUseCase,
            mockSetBeepSoundUseCase,
            mockSetSessionStartCountDownUseCase,
            mockSetPeriodStartCountDownUseCase,
            mockUpdateUserNameUseCase,
            mockDeleteUserUseCase,
            mockCreateUserUseCase,
            mockSaveSelectedExerciseTypesUseCase,
            mockSetAppLanguageUseCase,
            mockSetAppThemeUseCase,
            mockResetAllSettingsUseCase,
            mockValidatePeriodLengthUseCase,
            mockValidateNumberOfWorkPeriodsUseCase,
            mockValidateInputSessionStartCountdownUseCase,
            mockValidateInputPeriodStartCountdownUseCase,
            mockValidateInputUserNameUseCase,
            mockToggleExerciseTypeInListUseCase,
        )

    @BeforeEach
    fun setUpMock() {
        coEvery { mockGetGeneralSettingsUseCase.execute() } answers { settingsFlow }
        coEvery { mockSetWorkPeriodLengthUseCase.execute(any()) } just Runs
        coEvery { mockSetRestPeriodLengthUseCase.execute(any()) } just Runs
        coEvery { mockSetNumberOfWorkPeriodsUseCase.execute(any()) } just Runs
        coEvery { mockSetBeepSoundUseCase.execute(any()) } just Runs
        coEvery { mockSetSessionStartCountDownUseCase.execute(any()) } just Runs
        coEvery { mockSetPeriodStartCountDownUseCase.execute(any()) } just Runs
        coEvery { mockUpdateUserNameUseCase.execute(any()) } returns Output.Success(testReturnInt)
        coEvery { mockDeleteUserUseCase.execute(any()) } returns Output.Success(testReturnInt)
        coEvery { mockCreateUserUseCase.execute(any()) } returns Output.Success(testReturnLong)
        coEvery { mockSaveSelectedExerciseTypesUseCase.execute(any()) } just Runs
        coEvery { mockSetAppLanguageUseCase.execute(any()) } returns Output.Success(testReturnInt)
        coEvery { mockSetAppThemeUseCase.execute(any()) } just Runs
        coEvery { mockResetAllSettingsUseCase.execute() } just Runs
        coEvery {
            mockValidatePeriodLengthUseCase.execute(
                any(),
                any(),
            )
        } returns Constants.InputError.NONE
        coEvery { mockValidateNumberOfWorkPeriodsUseCase.execute(any()) } returns Constants.InputError.NONE
        coEvery { mockValidateInputSessionStartCountdownUseCase.execute(any()) } returns Constants.InputError.NONE
        coEvery {
            mockValidateInputPeriodStartCountdownUseCase.execute(
                any(),
                any(),
                any(),
            )
        } returns Constants.InputError.NONE
        coEvery {
            mockValidateInputUserNameUseCase.execute(
                any(),
                any(),
            )
        } returns Constants.InputError.NONE
        coEvery {
            mockToggleExerciseTypeInListUseCase.execute(
                any(),
                any(),
            )
        } returns testListExercises
    }

    @Test
    fun `calls on interactor getAllUsers calls GetAllUsersUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.getGeneralSettings()
            coVerify(exactly = 1) { mockGetGeneralSettingsUseCase.execute() }
            assertEquals(settingsFlow, result)
        }

    @Test
    fun `calls on interactor setWorkPeriodLength calls SetWorkPeriodLengthUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setWorkPeriodLength(654L)
            coVerify(exactly = 1) { mockSetWorkPeriodLengthUseCase.execute(654L) }
        }

    @Test
    fun `calls on interactor setRestPeriodLength calls SetRestPeriodLengthUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setRestPeriodLength(245L)
            coVerify(exactly = 1) { mockSetRestPeriodLengthUseCase.execute(245L) }
        }

    @Test
    fun `calls on interactor setNumberOfWorkPeriods calls SetNumberOfWorkPeriodsUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setNumberOfWorkPeriods(987)
            coVerify(exactly = 1) { mockSetNumberOfWorkPeriodsUseCase.execute(987) }
        }

    @Test
    fun `calls on interactor setNumberOfWorkPeriods calls SetBeepSoundUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setBeepSound(true)
            coVerify(exactly = 1) { mockSetBeepSoundUseCase.execute(true) }
        }

    @Test
    fun `calls on interactor setSessionStartCountDown calls SetSessionStartCountDownUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setSessionStartCountDown(357L)
            coVerify(exactly = 1) { mockSetSessionStartCountDownUseCase.execute(357L) }
        }

    @Test
    fun `calls on interactor setPeriodStartCountDown calls SetPeriodStartCountDownUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setPeriodStartCountDown(843L)
            coVerify(exactly = 1) { mockSetPeriodStartCountDownUseCase.execute(843L) }
        }

    @Test
    fun `calls on interactor updateUserName calls UpdateUserNameUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.updateUserName(testUser)
            coVerify(exactly = 1) { mockUpdateUserNameUseCase.execute(testUser) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(testReturnInt, result.result)
        }

    @Test
    fun `calls on interactor deleteUser calls DeleteUserUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.deleteUser(testUser)
            coVerify(exactly = 1) { mockDeleteUserUseCase.execute(testUser) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(testReturnInt, result.result)
        }

    @Test
    fun `calls on interactor createUser calls CreateUserUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.createUser(testUser)
            coVerify(exactly = 1) { mockCreateUserUseCase.execute(testUser) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(testReturnLong, result.result)
        }

    @Test
    fun `calls on interactor saveSelectedExerciseTypes calls SaveSelectedExerciseTypesUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.saveSelectedExerciseTypes(testListExercises)
            coVerify(exactly = 1) { mockSaveSelectedExerciseTypesUseCase.execute(testListExercises) }
        }

    @Test
    fun `calls on interactor setAppLanguage calls SetAppLanguageUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.setAppLanguage(AppLanguage.FRENCH)
            coVerify(exactly = 1) { mockSetAppLanguageUseCase.execute(AppLanguage.FRENCH) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(testReturnInt, result.result)
        }

    @Test
    fun `calls on interactor setAppTheme calls SetAppThemeUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.setAppTheme(AppTheme.DARK)
            coVerify(exactly = 1) { mockSetAppThemeUseCase.execute(AppTheme.DARK) }
        }

    @Test
    fun `calls on interactor resetAllSettings calls ResetAllSettingsUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.resetAllSettings()
            coVerify(exactly = 1) { mockResetAllSettingsUseCase.execute() }
        }

    @Test
    fun `calls on interactor validatePeriodLength calls ValidatePeriodLengthUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.validatePeriodLength(testString, 531L)
            coVerify(exactly = 1) { mockValidatePeriodLengthUseCase.execute(testString, 531L) }
            assertEquals(Constants.InputError.NONE, result)
        }

    @Test
    fun `calls on interactor validateNumberOfWorkPeriods calls ValidateNumberOfWorkPeriodsUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.validateNumberOfWorkPeriods(testString)
            coVerify(exactly = 1) { mockValidateNumberOfWorkPeriodsUseCase.execute(testString) }
            assertEquals(Constants.InputError.NONE, result)
        }

    @Test
    fun `calls on interactor validateInputSessionStartCountdown calls ValidateInputSessionStartCountdownUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.validateInputSessionStartCountdown(testString)
            coVerify(exactly = 1) { mockValidateInputSessionStartCountdownUseCase.execute(testString) }
            assertEquals(Constants.InputError.NONE, result)
        }

    @Test
    fun `calls on interactor validateInputPeriodStartCountdown calls ValidateInputPeriodStartCountdownUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.validateInputPeriodStartCountdown(testString, 495L, 285L)
            coVerify(exactly = 1) {
                mockValidateInputPeriodStartCountdownUseCase.execute(
                    testString,
                    495L,
                    285L,
                )
            }
            assertEquals(Constants.InputError.NONE, result)
        }

    @Test
    fun `calls on interactor validateInputUserName calls ValidateInputUserNameUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.validateInputUserName(testUser, testUsersList)
            coVerify(exactly = 1) {
                mockValidateInputUserNameUseCase.execute(
                    testUser,
                    testUsersList,
                )
            }
            assertEquals(Constants.InputError.NONE, result)
        }

    @Test
    fun `calls on interactor toggleExerciseTypeInList calls mockToggleExerciseTypeInListUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result =
                testedInteractor.toggleExerciseTypeInList(
                    testListExercises,
                    ExerciseTypeSelected(ExerciseType.CAT, true),
                )
            coVerify(exactly = 1) {
                mockToggleExerciseTypeInListUseCase.execute(
                    testListExercises,
                    ExerciseTypeSelected(ExerciseType.CAT, true),
                )
            }
            assertEquals(testListExercises, result)
        }
}

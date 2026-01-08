package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterTest : AbstractMockkTest() {
    private val mockSettingsInteractor = mockk<SettingsInteractor>()
    private val mockMapper = mockk<SettingsViewStateMapper>()
    private val testDispatcher = StandardTestDispatcher()

    private val generalSettingsFlow = MutableStateFlow<Output<GeneralSettings>>(Output.Success(testGeneralSettings()))

    private val testUser1 = User(id = 1L, name = "User One", selected = true)
    private val testUser2 = User(id = 2L, name = "User Two", selected = false)

    private fun testGeneralSettings() =
        GeneralSettings(
            workPeriodLengthMs = 20000L,
            restPeriodLengthMs = 10000L,
            numberOfWorkPeriods = 8,
            cycleLengthMs = 30000L,
            beepSoundCountDownActive = true,
            sessionStartCountDownLengthMs = 10000L,
            periodsStartCountDownLengthMs = 5000L,
            users = listOf(testUser1, testUser2),
            exerciseTypes =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, true),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                ),
            currentLanguage = AppLanguage.ENGLISH,
            currentTheme = AppTheme.FOLLOW_SYSTEM,
        )

    private fun testNominalViewState() =
        SettingsViewState.Nominal(
            workPeriodLengthAsSeconds = "20",
            restPeriodLengthAsSeconds = "10",
            numberOfWorkPeriods = "8",
            totalCycleLength = "30",
            beepSoundCountDownActive = true,
            sessionStartCountDownLengthAsSeconds = "10",
            periodsStartCountDownLengthAsSeconds = "5",
            users = listOf(testUser1, testUser2),
            exerciseTypes =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, true),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                ),
            currentLanguage = AppLanguage.ENGLISH,
            currentTheme = AppTheme.FOLLOW_SYSTEM,
        )

    private lateinit var testedPresenter: SettingsPresenter

    @BeforeEach
    fun setUp() {
        every { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
        every { mockMapper.map(any()) } returns SettingsViewState.Loading

        testedPresenter =
            SettingsPresenter(
                settingsInteractor = mockSettingsInteractor,
                mapper = mockMapper,
                dispatcher = testDispatcher,
                logger = mockHiitLogger,
            )
    }

    // Screen state tests
    @Test
    fun `screenViewState returns mapped flow from interactor`() =
        runTest(testDispatcher) {
            val nominalState = testNominalViewState()
            every { mockMapper.map(Output.Success(testGeneralSettings())) } returns nominalState
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            // Launch a collector to activate the WhileSubscribed flow
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertEquals(nominalState, state)

            collectorJob.cancel()
        }

    @Test
    fun `dialogViewState initially emits None`() =
        runTest(testDispatcher) {
            val state = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, state)
        }

    // Work period tests
    @Test
    fun `editWorkPeriodLength with Nominal state emits dialog with current value`() =
        runTest(testDispatcher) {
            val nominalState = testNominalViewState()
            every { mockMapper.map(any()) } returns nominalState
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editWorkPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditWorkPeriodLength)
            assertEquals("20", (dialogState as SettingsDialog.EditWorkPeriodLength).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `editWorkPeriodLength with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editWorkPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setWorkPeriodLength with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns null
            coEvery { mockSettingsInteractor.setWorkPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setWorkPeriodLength("30")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setWorkPeriodLength(30000L) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `validatePeriodLengthInput with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength("25", 5L) } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("25")

            assertEquals(null, result)
            coVerify { mockSettingsInteractor.validatePeriodLength("25", 5L) }

            collectorJob.cancel()
        }

    // Rest period tests
    @Test
    fun `editRestPeriodLength with Nominal state emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editRestPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditRestPeriodLength)
            assertEquals("10", (dialogState as SettingsDialog.EditRestPeriodLength).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setRestPeriodLength with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns null
            coEvery { mockSettingsInteractor.setRestPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setRestPeriodLength("15")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setRestPeriodLength(15000L) }
        }

    // Number of work periods tests
    @Test
    fun `editNumberOfWorkPeriods emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editNumberOfWorkPeriods()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditNumberCycles)
            assertEquals("8", (dialogState as SettingsDialog.EditNumberCycles).numberOfCycles)

            collectorJob.cancel()
        }

    @Test
    fun `setNumberOfWorkPeriods with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateNumberOfWorkPeriods(any()) } returns null
            coEvery { mockSettingsInteractor.setNumberOfWorkPeriods(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setNumberOfWorkPeriods("12")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setNumberOfWorkPeriods(12) }
        }

    @Test
    fun `validateNumberOfWorkPeriods delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockSettingsInteractor.validateNumberOfWorkPeriods("10") } returns null

            val result = testedPresenter.validateNumberOfWorkPeriods("10")

            assertEquals(null, result)
        }

    // Beep sound tests
    @Test
    fun `toggleBeepSound with Nominal state calls interactor with toggled value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            coEvery { mockSettingsInteractor.setBeepSound(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.toggleBeepSound()
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setBeepSound(false) } // Was true, now false

            collectorJob.cancel()
        }

    // Session start countdown tests
    @Test
    fun `editSessionStartCountDown emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editSessionStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditSessionStartCountDown)
            assertEquals("10", (dialogState as SettingsDialog.EditSessionStartCountDown).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setSessionStartCountDown with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateInputSessionStartCountdown(any()) } returns null
            coEvery { mockSettingsInteractor.setSessionStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setSessionStartCountDown("12")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setSessionStartCountDown(12000L) }
        }

    // Period start countdown tests
    @Test
    fun `editPeriodStartCountDown emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editPeriodStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditPeriodStartCountDown)
            assertEquals("5", (dialogState as SettingsDialog.EditPeriodStartCountDown).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setPeriodStartCountDown with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(any(), any(), any())
            } returns null
            coEvery { mockSettingsInteractor.setPeriodStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setPeriodStartCountDown("8")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setPeriodStartCountDown(8000L) }
        }

    @Test
    fun `validateInputPeriodStartCountdown with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown("6", 20L, 10L)
            } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("6")

            assertEquals(null, result)
        }

    // User management tests
    @Test
    fun `addUser emits AddUser dialog with empty name`() =
        runTest(testDispatcher) {
            testedPresenter.addUser()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.AddUser)
            assertEquals("", (dialogState as SettingsDialog.AddUser).userName)
        }

    @Test
    fun `addUser with name emits AddUser dialog with provided name`() =
        runTest(testDispatcher) {
            testedPresenter.addUser("Test Name")
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.AddUser)
            assertEquals("Test Name", (dialogState as SettingsDialog.AddUser).userName)
        }

    @Test
    fun `editUser emits EditUser dialog with user`() =
        runTest(testDispatcher) {
            testedPresenter.editUser(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditUser)
            assertEquals(testUser1, (dialogState as SettingsDialog.EditUser).user)
        }

    @Test
    fun `saveUser with new user (id=0) creates user via interactor`() =
        runTest(testDispatcher) {
            val newUser = User(id = 0L, name = "New User", selected = false)
            coEvery { mockSettingsInteractor.createUser(any()) } returns Output.Success(1L)

            testedPresenter.saveUser(newUser)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.createUser(newUser) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `saveUser with existing user updates user via interactor`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.updateUserName(any()) } returns Output.Success(1)

            testedPresenter.saveUser(testUser1)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.updateUserName(testUser1) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `saveUser with create error emits Error dialog`() =
        runTest(testDispatcher) {
            val newUser = User(id = 0L, name = "New User", selected = false)
            coEvery {
                mockSettingsInteractor.createUser(any())
            } returns Output.Error(DomainError.DATABASE_INSERT_FAILED, Exception("Test"))

            testedPresenter.saveUser(newUser)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.Error)
            assertEquals(DomainError.DATABASE_INSERT_FAILED.code, (dialogState as SettingsDialog.Error).errorCode)
        }

    @Test
    fun `deleteUser emits ConfirmDeleteUser dialog`() =
        runTest(testDispatcher) {
            testedPresenter.deleteUser(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.ConfirmDeleteUser)
            assertEquals(testUser1, (dialogState as SettingsDialog.ConfirmDeleteUser).user)
        }

    @Test
    fun `deleteUserConfirmation with success calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.deleteUser(any()) } returns Output.Success(1)

            testedPresenter.deleteUserConfirmation(testUser1)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.deleteUser(testUser1) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `deleteUserConfirmation with error emits Error dialog`() =
        runTest(testDispatcher) {
            coEvery {
                mockSettingsInteractor.deleteUser(any())
            } returns Output.Error(DomainError.DATABASE_DELETE_FAILED, Exception("Test"))

            testedPresenter.deleteUserConfirmation(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.Error)
        }

    @Test
    fun `validateInputUserNameString with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputUserName(testUser1, listOf(testUser1, testUser2))
            } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(testUser1)

            assertEquals(null, result)
        }

    // Exercise types tests
    @Test
    fun `toggleSelectedExercise with Nominal state calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            val toggledList =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, false),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                )
            every {
                mockSettingsInteractor.toggleExerciseTypeInList(any(), any())
            } returns toggledList
            coEvery { mockSettingsInteractor.saveSelectedExerciseTypes(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val exerciseToToggle = ExerciseTypeSelected(ExerciseType.LUNGE, true)
            testedPresenter.toggleSelectedExercise(exerciseToToggle)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.saveSelectedExerciseTypes(toggledList) }

            collectorJob.cancel()
        }

    // App settings tests
    @Test
    fun `editLanguage emits PickLanguage dialog with current language`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editLanguage()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.PickLanguage)
            assertEquals(AppLanguage.ENGLISH, (dialogState as SettingsDialog.PickLanguage).currentLanguage)

            collectorJob.cancel()
        }

    @Test
    fun `setLanguage calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.setAppLanguage(any()) } returns Output.Success(1)

            testedPresenter.setLanguage(AppLanguage.FRENCH)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setAppLanguage(AppLanguage.FRENCH) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editTheme emits PickTheme dialog with current theme`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editTheme()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.PickTheme)
            assertEquals(AppTheme.FOLLOW_SYSTEM, (dialogState as SettingsDialog.PickTheme).currentTheme)

            collectorJob.cancel()
        }

    @Test
    fun `setTheme calls interactor dismisses dialog and emits restart trigger`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.setAppTheme(any()) } just runs

            // Collect restartTrigger emissions
            val emissions = mutableListOf<Unit>()
            val collectorJob =
                launch {
                    testedPresenter.restartTrigger.take(1).toList(emissions)
                }

            testedPresenter.setTheme(AppTheme.DARK)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setAppTheme(AppTheme.DARK) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify restart trigger was emitted
            assertEquals(1, emissions.size)

            collectorJob.cancel()
        }

    // Reset tests
    @Test
    fun `resetAllSettings emits ConfirmResetAllSettings dialog`() =
        runTest(testDispatcher) {
            testedPresenter.resetAllSettings()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.ConfirmResetAllSettings, dialogState)
        }

    @Test
    fun `resetAllSettingsConfirmation calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.resetAllSettings() } returns Unit

            testedPresenter.resetAllSettingsConfirmation()
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.resetAllSettings() }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    // Dialog control tests
    @Test
    fun `cancelDialog emits None dialog state`() =
        runTest(testDispatcher) {
            testedPresenter.addUser("Test")
            advanceUntilIdle()

            testedPresenter.cancelDialog()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }
}

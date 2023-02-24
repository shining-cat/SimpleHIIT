package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseType.*
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.SimpleHiitPreferences
import io.mockk.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplSettingsTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitDataStoreManager = mockSimpleHiitDataStoreManager,
        hiitLogger = mockHiitLogger
    )

    @Test
    fun `setWorkPeriodLength calls HiitDataStoreManager setWorkPeriodLength with correct value `() =
        runTest {
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setWorkPeriodLength(any()) } just Runs
            //
            simpleHiitRepository.setWorkPeriodLength(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setWorkPeriodLength(testValue) }
        }

    @Test
    fun `setRestPeriodLength calls HiitDataStoreManager setRestPeriodLength with correct value `() =
        runTest {
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setRestPeriodLength(any()) } just Runs
            //
            simpleHiitRepository.setRestPeriodLength(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setRestPeriodLength(testValue) }
        }

    @Test
    fun `setNumberOfWorkPeriods calls HiitDataStoreManager setNumberOfWorkPeriods with correct value `() =
        runTest {
            val testValue = 123
            coEvery { mockSimpleHiitDataStoreManager.setNumberOfWorkPeriods(any()) } just Runs
            //
            simpleHiitRepository.setNumberOfWorkPeriods(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testValue) }
        }


    @ParameterizedTest(name = "{index} -> when called with {0}, should call HiitDataStoreManager with {0}")
    @ValueSource(booleans = [true, false])
    fun `setBeepSound calls HiitDataStoreManager setBeepSound with correct value `(
        testValue: Boolean
    ) = runTest {
        coEvery { mockSimpleHiitDataStoreManager.setBeepSound(any()) } just Runs
        //
        simpleHiitRepository.setBeepSound(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setBeepSound(testValue) }
    }


    @Test
    fun `setSessionStartCountdown calls HiitDataStoreManager setSessionStartCountdown with correct value `() =
        runTest {
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setSessionStartCountdown(any()) } just Runs
            //
            simpleHiitRepository.setSessionStartCountdown(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setSessionStartCountdown(
                    testValue
                )
            }
        }

    @Test
    fun `setPeriodStartCountdown calls HiitDataStoreManager setPeriodStartCountdown with correct value `() =
        runTest {
            val testValue = 123L
            coEvery { mockSimpleHiitDataStoreManager.setPeriodStartCountdown(any()) } just Runs
            //
            simpleHiitRepository.setPeriodStartCountdown(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.setPeriodStartCountdown(testValue) }
        }

    @Test
    fun `setNumberOfCumulatedCycles calls HiitDataStoreManager setNumberOfCumulatedCycles with correct value `() =
        runTest {
            val testValue = 123
            coEvery { mockSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(any()) } just Runs
            //
            simpleHiitRepository.setTotalRepetitionsNumber(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(
                    testValue
                )
            }
        }

    @Test
    fun `setExercisesTypesSelected calls HiitDataStoreManager setExercisesTypesSelected with correct value `() =
        runTest {
            val testValue = listOf(CAT, CRAB, LUNGE, LYING, PLANK, SITTING, SQUAT, STANDING)
            coEvery { mockSimpleHiitDataStoreManager.setExercisesTypesSelected(any()) } just Runs
            //
            simpleHiitRepository.setExercisesTypesSelected(testValue)
            //
            coVerify(exactly = 1) {
                mockSimpleHiitDataStoreManager.setExercisesTypesSelected(
                    testValue
                )
            }
        }

    @Test
    fun `getGeneralSettings calls HiitDataStoreManager getGeneralSettings, return correct value and updates returned flow content`() =
        runTest {
            val datastoreOutput1 =
                SimpleHiitPreferences() //holds default values, should always be the fallback
            val datastoreOutput2 = SimpleHiitPreferences(
                workPeriodLengthMs = 123,
                restPeriodLengthMs = 234,
                numberOfWorkPeriods = 345,
                beepSoundActive = true,
                sessionCountDownLengthMs = 456,
                PeriodCountDownLengthMs = 567,
                selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                numberCumulatedCycles = 3
            )
            val datastoreOutput3 = SimpleHiitPreferences(
                workPeriodLengthMs = 987,
                restPeriodLengthMs = 876,
                numberOfWorkPeriods = 765,
                beepSoundActive = false,
                sessionCountDownLengthMs = 654,
                PeriodCountDownLengthMs = 543,
                selectedExercisesTypes = randomListOfExerciseTypesSelected(),
                numberCumulatedCycles = 5
            )
            val settingsFlow = MutableSharedFlow<SimpleHiitPreferences>()
            coEvery { mockSimpleHiitDataStoreManager.getPreferences() } answers { settingsFlow }
            //
            val settingsFlowAsList = mutableListOf<SimpleHiitPreferences>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                simpleHiitRepository.getPreferences().toList(settingsFlowAsList)
            }
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.getPreferences() }
            // first emission
            settingsFlow.emit(datastoreOutput1)
            assertEquals(1, settingsFlowAsList.size)
            val settings1 = settingsFlowAsList[0]
            assertEquals(datastoreOutput1, settings1)
            // second emission
            settingsFlow.emit(datastoreOutput2)
            assertEquals(2, settingsFlowAsList.size)
            val settings2 = settingsFlowAsList[1]
            assertEquals(datastoreOutput2, settings2)
            // third emission
            settingsFlow.emit(datastoreOutput3)
            assertEquals(3, settingsFlowAsList.size)
            val settings3 = settingsFlowAsList[2]
            assertEquals(datastoreOutput3, settings3)
            //
            collectJob.cancel()
        }

    private fun randomListOfExerciseTypesSelected() = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = Random.nextBoolean()
        )
    }

    @Test
    fun `getGeneralSettings swallows datastore exception and returns default value`() = runTest {
        val defaultSettings =
            SimpleHiitPreferences() //holds default values, should always be the fallback
        val thrownException = Exception("this is a test exception")
        coEvery { mockSimpleHiitDataStoreManager.getPreferences() } throws thrownException
        //
        val settingsFlowAsList = mutableListOf<SimpleHiitPreferences>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            simpleHiitRepository.getPreferences().toList(settingsFlowAsList)
        }
        coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.getPreferences() }
        coVerify(exactly = 1) {
            mockHiitLogger.e(
                any(),
                "failed getting general settings - returning default settings",
                thrownException
            )
        }
        assertEquals(1, settingsFlowAsList.size)
        val settingsOutput = settingsFlowAsList[0]
        assertEquals(defaultSettings, settingsOutput)
        //
        collectJob.cancel()
    }

    @Test
    fun `getGeneralSettings rethrows CancellationException when it gets thrown by datastore`() =
        runTest {
            coEvery { mockSimpleHiitDataStoreManager.getPreferences() } throws mockk<CancellationException>()
            //
            assertThrows<CancellationException> {
                simpleHiitRepository.getPreferences()
            }
            coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.getPreferences() }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

//////////////////

    @Test
    fun `resetAllSettings calls HiitDataStoreManager clearAll`() = runTest {
        coEvery { mockSimpleHiitDataStoreManager.clearAll() } just Runs
        //
        simpleHiitRepository.resetAllSettings()
        //
        coVerify(exactly = 1) { mockSimpleHiitDataStoreManager.clearAll() }
    }

}
package fr.shiningcat.simplehiit.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS
import fr.shiningcat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.io.TempDir
import java.io.File

private const val TEST_DATASTORE_NAME: String = "test_datastore.preferences_pb"

@OptIn(ExperimentalCoroutinesApi::class)
class SimpleHiitDataStoreManagerTest {

    @TempDir
    lateinit var tempFolder: File

    private val mockkLogger = mockk<HiitLogger>(relaxed = true)
    private lateinit var testDataStore: DataStore<Preferences>

    private val defaultIntValue = -1
    private val defaultLongValue = -1L
    private val defaultBoolValue = false
    private val defaultStringSetValue = emptySet<String>()

    @Test
    fun `check SetWorkPeriodLength is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testValue)

        val result = retrievePrefLong(WORK_PERIOD_LENGTH_MILLISECONDS).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetRestPeriodLength is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testValue)

        val result = retrievePrefLong(REST_PERIOD_LENGTH_MILLISECONDS).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetNumberOfWorkPeriods is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testValue)

        val result = retrievePrefInt(NUMBER_WORK_PERIODS).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetBeepSound is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = true
        testedSimpleHiitDataStoreManager.setBeepSound(testValue)

        val result = retrievePrefBool(BEEP_SOUND_ACTIVE).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetSessionStartCountdown is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testValue)

        val result = retrievePrefLong(SESSION_COUNTDOWN_LENGTH_MILLISECONDS).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetPeriodStartCountdown is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testValue)

        val result = retrievePrefLong(PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check SetExercisesTypesSelected is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValue)

        val result = retrievePrefStringSet(EXERCISE_TYPES_SELECTED).first()

        assertEquals(testValue.map { it.name }.toSet(), result)
    }

    @Test
    fun `check SetNumberOfCumulatedCycles is storing in datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        val testValue = 123
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testValue)

        val result = retrievePrefInt(NUMBER_CUMULATED_CYCLES).first()

        assertEquals(testValue, result)
    }

    @Test
    fun `check calling clear clears datastore preferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        // insert first values for all prefs:
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testValue)
        val result = retrievePrefLong(WORK_PERIOD_LENGTH_MILLISECONDS).first()
        assertEquals(testValue, result)
        val testValue2 = 234L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testValue2)
        val result2 = retrievePrefLong(REST_PERIOD_LENGTH_MILLISECONDS).first()
        assertEquals(testValue2, result2)
        val testValue3 = 345
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testValue3)
        val result3 = retrievePrefInt(NUMBER_WORK_PERIODS).first()
        assertEquals(testValue3, result3)
        val testValue4 = true
        testedSimpleHiitDataStoreManager.setBeepSound(testValue4)
        val result4 = retrievePrefBool(BEEP_SOUND_ACTIVE).first()
        assertEquals(testValue4, result4)
        val testValue5 = 456L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testValue5)
        val result5 = retrievePrefLong(SESSION_COUNTDOWN_LENGTH_MILLISECONDS).first()
        assertEquals(testValue5, result5)
        val testValue6 = 567L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testValue6)
        val result6 = retrievePrefLong(PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).first()
        assertEquals(testValue6, result6)
        val testValue7 = listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValue7)
        val result7 = retrievePrefStringSet(EXERCISE_TYPES_SELECTED).first()
        assertEquals(testValue7.map { it.name }.toSet(), result7)
        val testValue8 = 678
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testValue8)
        val result8 = retrievePrefInt(NUMBER_CUMULATED_CYCLES).first()
        assertEquals(testValue8, result8)
        // now clear
        testedSimpleHiitDataStoreManager.clearAll()
        // check default values are returned
        val resultAfterClear = retrievePrefLong(WORK_PERIOD_LENGTH_MILLISECONDS).first()
        assertEquals(defaultLongValue, resultAfterClear)
        val resultAfterClear2 = retrievePrefLong(REST_PERIOD_LENGTH_MILLISECONDS).first()
        assertEquals(defaultLongValue, resultAfterClear2)
        val resultAfterClear3 = retrievePrefInt(NUMBER_WORK_PERIODS).first()
        assertEquals(defaultIntValue, resultAfterClear3)
        val resultAfterClear4 = retrievePrefBool(BEEP_SOUND_ACTIVE).first()
        assertEquals(defaultBoolValue, resultAfterClear4)
        val resultAfterClear5 = retrievePrefLong(SESSION_COUNTDOWN_LENGTH_MILLISECONDS).first()
        assertEquals(defaultLongValue, resultAfterClear5)
        val resultAfterClear6 = retrievePrefLong(PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).first()
        assertEquals(defaultLongValue, resultAfterClear6)
        val resultAfterClear7 = retrievePrefStringSet(EXERCISE_TYPES_SELECTED).first()
        assertEquals(defaultStringSetValue, resultAfterClear7)
        val resultAfterClear8 = retrievePrefInt(NUMBER_CUMULATED_CYCLES).first()
        assertEquals(defaultIntValue, resultAfterClear8)
    }

    @Test
    fun `check GetPreferences returns expected SimpleHiitPreferences`() = runTest {
        testDataStore = PreferenceDataStoreFactory.create(
            scope = backgroundScope,
            produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
        )
        val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
            dataStore = testDataStore,
            hiitLogger = mockkLogger,
            ioDispatcher = UnconfinedTestDispatcher(),
        )
        // insert first values for all prefs:
        val testWorkPeriodLengthValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testWorkPeriodLengthValue)
        val testRestPeriodLengthValue = 234L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testRestPeriodLengthValue)
        val testNumberOfWorkPeriodsValue = 345
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testNumberOfWorkPeriodsValue)
        val testBeepSoundValue = true
        testedSimpleHiitDataStoreManager.setBeepSound(testBeepSoundValue)
        val testSessionStartCountdownValue = 456L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testSessionStartCountdownValue)
        val testPeriodStartCountdownValue = 567L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testPeriodStartCountdownValue)
        val testValueExercisesSelected =
            listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValueExercisesSelected)
        val testNumberCyclesValue = 678
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testNumberCyclesValue)
        // now retrieve SimpleHiitPreferences
        val resultPreferences = testedSimpleHiitDataStoreManager.getPreferences().first()
        //
        val expectedPreferences =
            SimpleHiitPreferences(
                workPeriodLengthMs = testWorkPeriodLengthValue,
                restPeriodLengthMs = testRestPeriodLengthValue,
                numberOfWorkPeriods = testNumberOfWorkPeriodsValue,
                beepSoundActive = testBeepSoundValue,
                sessionCountDownLengthMs = testSessionStartCountdownValue,
                PeriodCountDownLengthMs = testPeriodStartCountdownValue,
                selectedExercisesTypes =
                ExerciseType.entries.map {
                    ExerciseTypeSelected(
                        it,
                        testValueExercisesSelected.contains(it)
                    )
                },
                numberCumulatedCycles = testNumberCyclesValue,
            )
        assertEquals(expectedPreferences, resultPreferences)
    }

    @Test
    fun `check GetPreferences on empty preferences returns SimpleHiitPreferences with default values`() =
        runTest {
            testDataStore = PreferenceDataStoreFactory.create(
                scope = backgroundScope,
                produceFile = { File(tempFolder, TEST_DATASTORE_NAME) },
            )
            val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
                dataStore = testDataStore,
                hiitLogger = mockkLogger,
                ioDispatcher = UnconfinedTestDispatcher(),
            )
            // retrieve SimpleHiitPreferences
            val resultPreferences = testedSimpleHiitDataStoreManager.getPreferences().first()
            //
            val expectedPreferences =
                SimpleHiitPreferences(
                    workPeriodLengthMs = WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
                    restPeriodLengthMs = REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
                    numberOfWorkPeriods = NUMBER_WORK_PERIODS_DEFAULT,
                    beepSoundActive = BEEP_SOUND_ACTIVE_DEFAULT,
                    sessionCountDownLengthMs = SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
                    PeriodCountDownLengthMs = PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
                    selectedExercisesTypes = DEFAULT_SELECTED_EXERCISES_TYPES,
                    numberCumulatedCycles = NUMBER_CUMULATED_CYCLES_DEFAULT,
                )
            assertEquals(expectedPreferences, resultPreferences)
        }

    @Test
    fun `check GetPreferences dataStore throws returns SimpleHiitPreferences with default values`() =
        runTest {
            testDataStore = mockk<DataStore<Preferences>>(relaxed = true)
            val ioExceptionDataFlow: Flow<Preferences> = flow {
                throw IOException()
            }
            coEvery { testDataStore.data } returns ioExceptionDataFlow
            val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
                dataStore = testDataStore,
                hiitLogger = mockkLogger,
                ioDispatcher = UnconfinedTestDispatcher(),
            )
            // retrieve SimpleHiitPreferences
            val resultPreferences = testedSimpleHiitDataStoreManager.getPreferences().first()
            //
            coVerify { mockkLogger.e(any(), any()) }
            val expectedPreferences = SimpleHiitPreferences()
            assertEquals(expectedPreferences, resultPreferences)
        }

    // //////////
    private fun retrievePrefInt(key: Preferences.Key<Int>): Flow<Int> =
        testDataStore.data.map { it[key] ?: defaultIntValue }

    private fun retrievePrefLong(key: Preferences.Key<Long>): Flow<Long> =
        testDataStore.data.map { it[key] ?: defaultLongValue }

    private fun retrievePrefBool(key: Preferences.Key<Boolean>): Flow<Boolean> =
        testDataStore.data.map { it[key] ?: defaultBoolValue }

    private fun retrievePrefStringSet(key: Preferences.Key<Set<String>>): Flow<Set<String>> =
        testDataStore.data.map { it[key] ?: defaultStringSetValue }

}

package fr.shining_cat.simplehiit.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStoreFile
import androidx.test.core.app.ApplicationProvider
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManagerImpl
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.utils.HiitLoggerImpl
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.random.Random

private const val TEST_DATASTORE_NAME: String = "test_datastore"

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitDataStoreManagerImplTest {

    private val testLogger = HiitLoggerImpl(true)

    private val testContext: Context = ApplicationProvider.getApplicationContext()
    private val testCoroutineDispatcher = UnconfinedTestDispatcher()
    private val testCoroutineScope = TestScope(testCoroutineDispatcher)
    private val testDataStore: DataStore<Preferences> =
        PreferenceDataStoreFactory.create(
            scope = testCoroutineScope,
            produceFile = { testContext.preferencesDataStoreFile(TEST_DATASTORE_NAME) }
        )
    private val defaultIntValue = -1
    private val defaultLongValue = -1L
    private val defaultBoolValue = false
    private val defaultStringSetValue = emptySet<String>()

    @Before
    fun setupBefore() {
        Dispatchers.setMain(testCoroutineDispatcher)
    }

    @After
    fun wipeOut() {
        Dispatchers.resetMain()
        testCoroutineScope.runTest {
            testDataStore.edit { it.clear() }
        }
        testCoroutineScope.cancel()
    }

    private val testedSimpleHiitDataStoreManager = SimpleHiitDataStoreManagerImpl(
        dataStore = testDataStore,
        hiitLogger = testLogger
    )

    @Test
    fun checkSetWorkPeriodLengthIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Long>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultLongValue, beforeStoring)
        //insert new value
        val testValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetRestPeriodLengthIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Long>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultLongValue, beforeStoring)
        //insert new value
        val testValue = 321L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetNumberOfWorkPeriodsIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Int>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultIntValue, beforeStoring)
        //insert new value
        val testValue = 234
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetBeepSoundIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Boolean>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefBool(SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultBoolValue, beforeStoring)
        //insert new value
        val testValue = !defaultBoolValue
        testedSimpleHiitDataStoreManager.setBeepSound(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetSessionStartCountdownIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Long>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultLongValue, beforeStoring)
        //insert new value
        val testValue = 345L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetPeriodStartCountdownIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Long>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultLongValue, beforeStoring)
        //insert new value
        val testValue = 456L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetNumberOfCumulatedCyclesIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Int>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultIntValue, beforeStoring)
        //insert new value
        val testValue = 567
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1]
        assertEquals(testValue, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkSetExercisesTypesSelectedIsStoringInPrefDataStore() = runTest {
        val valueFlowAsList = mutableListOf<Set<String>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefStringSet(SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED).toList(
                valueFlowAsList
            )
        }
        assertEquals(1, valueFlowAsList.size)
        //check nothing is there first
        val beforeStoring = valueFlowAsList[0]
        assertEquals(defaultStringSetValue, beforeStoring)
        //insert new value
        val testValue = listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValue)
        //check new value is in there
        assertEquals(2, valueFlowAsList.size)
        val storedValue = valueFlowAsList[1].toList()
        val expected = testValue.map { it.name }
        assertEquals(expected, storedValue)
        //
        collectJob.cancel()
    }

    @Test
    fun checkCallingClearClearsPrefDataStore() = runTest {
        val exercisesSelectedFlowAsList = mutableListOf<Set<String>>()
        val collectJobExercisesSelected = launch(UnconfinedTestDispatcher()) {
            retrievePrefStringSet(SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED).toList(
                exercisesSelectedFlowAsList
            )
        }
        val numberCyclesFlowAsList = mutableListOf<Int>()
        val collectJobNumberOfCycles = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES).toList(
                numberCyclesFlowAsList
            )
        }
        val periodCountDownFlowAsList = mutableListOf<Long>()
        val collectPeriodCountDownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                periodCountDownFlowAsList
            )
        }
        val sessionStartCountdownFlowAsList = mutableListOf<Long>()
        val collectSessionStartCountdownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                sessionStartCountdownFlowAsList
            )
        }
        val beepSoundFlowAsList = mutableListOf<Boolean>()
        val collectBeepSoundJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefBool(SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE).toList(
                beepSoundFlowAsList
            )
        }
        val numberOfWorkPeriodsFlowAsList = mutableListOf<Int>()
        val collectNumberOfWorkPeriodsJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS).toList(
                numberOfWorkPeriodsFlowAsList
            )
        }
        val restPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectRestPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS).toList(
                restPeriodLengthFlowAsList
            )
        }
        val workPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectWorkPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS).toList(
                workPeriodLengthFlowAsList
            )
        }
        //insert new values
        val testValueExercisesSelected =
            listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValueExercisesSelected)
        val testNumberCyclesValue = 567
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testNumberCyclesValue)
        val testPeriodStartCountdownValue = 456L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testPeriodStartCountdownValue)
        val testSessionStartCountdownValue = 345L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testSessionStartCountdownValue)
        val testBeepSoundValue = !defaultBoolValue
        testedSimpleHiitDataStoreManager.setBeepSound(testBeepSoundValue)
        val testNumberOfWorkPeriodsValue = 234
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testNumberOfWorkPeriodsValue)
        val testRestPeriodLengthValue = 321L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testRestPeriodLengthValue)
        val testWorkPeriodLengthValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testWorkPeriodLengthValue)
        //clear
        testedSimpleHiitDataStoreManager.clearAll()
        //check nothing remains
        val afterClearingExercisesSelected = exercisesSelectedFlowAsList.last()
        assertEquals(defaultStringSetValue, afterClearingExercisesSelected)
        val afterClearingNumberOfCycles = numberCyclesFlowAsList.last()
        assertEquals(defaultIntValue, afterClearingNumberOfCycles)
        val afterClearingPeriodCountDown = periodCountDownFlowAsList.last()
        assertEquals(defaultLongValue, afterClearingPeriodCountDown)
        val afterClearingSessionStartCountdown = sessionStartCountdownFlowAsList.last()
        assertEquals(defaultLongValue, afterClearingSessionStartCountdown)
        val afterClearingBeepSound = beepSoundFlowAsList.last()
        assertEquals(defaultBoolValue, afterClearingBeepSound)
        val afterClearingNumberOfWorkPeriods = numberOfWorkPeriodsFlowAsList.last()
        assertEquals(defaultIntValue, afterClearingNumberOfWorkPeriods)
        val afterClearingRestPeriodLength = restPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, afterClearingRestPeriodLength)
        val afterClearingWorkPeriodLength = workPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, afterClearingWorkPeriodLength)
        //cancel all collect jobs
        collectJobExercisesSelected.cancel()
        collectJobNumberOfCycles.cancel()
        collectPeriodCountDownJob.cancel()
        collectSessionStartCountdownJob.cancel()
        collectBeepSoundJob.cancel()
        collectNumberOfWorkPeriodsJob.cancel()
        collectRestPeriodLengthJob.cancel()
        collectWorkPeriodLengthJob.cancel()
    }

    @Test
    fun checkGetPreferencesPrefDataStore() = runTest {
        val exercisesSelectedFlowAsList = mutableListOf<Set<String>>()
        val collectJobExercisesSelected = launch(UnconfinedTestDispatcher()) {
            retrievePrefStringSet(SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED).toList(
                exercisesSelectedFlowAsList
            )
        }
        val periodCountDownFlowAsList = mutableListOf<Long>()
        val collectPeriodCountDownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                periodCountDownFlowAsList
            )
        }
        val sessionStartCountdownFlowAsList = mutableListOf<Long>()
        val collectSessionStartCountdownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                sessionStartCountdownFlowAsList
            )
        }
        val beepSoundFlowAsList = mutableListOf<Boolean>()
        val collectBeepSoundJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefBool(SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE).toList(
                beepSoundFlowAsList
            )
        }
        val numberOfWorkPeriodsFlowAsList = mutableListOf<Int>()
        val collectNumberOfWorkPeriodsJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS).toList(
                numberOfWorkPeriodsFlowAsList
            )
        }
        val restPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectRestPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS).toList(
                restPeriodLengthFlowAsList
            )
        }
        val workPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectWorkPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS).toList(
                workPeriodLengthFlowAsList
            )
        }
        val numberCyclesFlowAsList = mutableListOf<Int>()
        val collectJobNumberOfCycles = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES).toList(
                numberCyclesFlowAsList
            )
        }
        //check nothing is there first
        val exercisesSelected = exercisesSelectedFlowAsList.last()
        assertEquals(defaultStringSetValue, exercisesSelected)
        val periodCountDown = periodCountDownFlowAsList.last()
        assertEquals(defaultLongValue, periodCountDown)
        val sessionStartCountdown = sessionStartCountdownFlowAsList.last()
        assertEquals(defaultLongValue, sessionStartCountdown)
        val beepSound = beepSoundFlowAsList.last()
        assertEquals(defaultBoolValue, beepSound)
        val numberOfWorkPeriods = numberOfWorkPeriodsFlowAsList.last()
        assertEquals(defaultIntValue, numberOfWorkPeriods)
        val restPeriodLength = restPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, restPeriodLength)
        val workPeriodLength = workPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, workPeriodLength)
        val numberOfCycles = numberCyclesFlowAsList.last()
        assertEquals(defaultIntValue, numberOfCycles)
        //insert new values
        val testValueExercisesSelected =
            listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValueExercisesSelected)
        val testPeriodStartCountdownValue = 456L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testPeriodStartCountdownValue)
        val testSessionStartCountdownValue = 345L
        testedSimpleHiitDataStoreManager.setSessionStartCountdown(testSessionStartCountdownValue)
        val testBeepSoundValue = !defaultBoolValue
        testedSimpleHiitDataStoreManager.setBeepSound(testBeepSoundValue)
        val testNumberOfWorkPeriodsValue = 234
        testedSimpleHiitDataStoreManager.setNumberOfWorkPeriods(testNumberOfWorkPeriodsValue)
        val testRestPeriodLengthValue = 321L
        testedSimpleHiitDataStoreManager.setRestPeriodLength(testRestPeriodLengthValue)
        val testWorkPeriodLengthValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testWorkPeriodLengthValue)
        val testNumberCyclesValue = 567
        testedSimpleHiitDataStoreManager.setNumberOfCumulatedCycles(testNumberCyclesValue)
        //get Preferences
        val preferencesFlowAsList = mutableListOf<SimpleHiitPreferences>()
        val collectPreferencesJob = launch(UnconfinedTestDispatcher()) {
            testedSimpleHiitDataStoreManager.getPreferences().toList(preferencesFlowAsList)
        }
        //
        val expectedPreferences = SimpleHiitPreferences(
            workPeriodLengthMs = testWorkPeriodLengthValue,
            restPeriodLengthMs = testRestPeriodLengthValue,
            numberOfWorkPeriods = testNumberOfWorkPeriodsValue,
            beepSoundActive = testBeepSoundValue,
            sessionCountDownLengthMs = testSessionStartCountdownValue,
            PeriodCountDownLengthMs = testPeriodStartCountdownValue,
            selectedExercisesTypes = ExerciseType.values().toList().map{ ExerciseTypeSelected(it, testValueExercisesSelected.contains(it))},
            numberCumulatedCycles = testNumberCyclesValue
        )
        val storedPreferences = preferencesFlowAsList.last()
        assertEquals(expectedPreferences, storedPreferences)
        //cancel all collect jobs
        collectJobExercisesSelected.cancel()
        collectPeriodCountDownJob.cancel()
        collectSessionStartCountdownJob.cancel()
        collectBeepSoundJob.cancel()
        collectNumberOfWorkPeriodsJob.cancel()
        collectRestPeriodLengthJob.cancel()
        collectWorkPeriodLengthJob.cancel()
        collectPreferencesJob.cancel()
        collectJobNumberOfCycles.cancel()
    }

    @Test
    fun checkGetPreferencesPrefDataStoreFailingReturnAllDefaults() = runTest {
        //TODO: how to trigger throwing exception from test dataStore?
    }
    @Test
    fun checkGetPreferencesPrefDataStoreEmptyReturnAllDefaults() = runTest {
        val exercisesSelectedFlowAsList = mutableListOf<Set<String>>()
        val collectJobExercisesSelected = launch(UnconfinedTestDispatcher()) {
            retrievePrefStringSet(SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED).toList(
                exercisesSelectedFlowAsList
            )
        }
        val periodCountDownFlowAsList = mutableListOf<Long>()
        val collectPeriodCountDownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                periodCountDownFlowAsList
            )
        }
        val sessionStartCountdownFlowAsList = mutableListOf<Long>()
        val collectSessionStartCountdownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                sessionStartCountdownFlowAsList
            )
        }
        val beepSoundFlowAsList = mutableListOf<Boolean>()
        val collectBeepSoundJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefBool(SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE).toList(
                beepSoundFlowAsList
            )
        }
        val numberOfWorkPeriodsFlowAsList = mutableListOf<Int>()
        val collectNumberOfWorkPeriodsJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS).toList(
                numberOfWorkPeriodsFlowAsList
            )
        }
        val restPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectRestPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS).toList(
                restPeriodLengthFlowAsList
            )
        }
        val workPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectWorkPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS).toList(
                workPeriodLengthFlowAsList
            )
        }
        val numberCyclesFlowAsList = mutableListOf<Int>()
        val collectJobNumberOfCycles = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES).toList(
                numberCyclesFlowAsList
            )
        }
        //check nothing is there first
        val beforeInsertionExercisesSelected = exercisesSelectedFlowAsList.last()
        assertEquals(defaultStringSetValue, beforeInsertionExercisesSelected)
        val beforeInsertionPeriodCountDown = periodCountDownFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionPeriodCountDown)
        val beforeInsertionSessionStartCountdown = sessionStartCountdownFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionSessionStartCountdown)
        val beforeInsertionBeepSound = beepSoundFlowAsList.last()
        assertEquals(defaultBoolValue, beforeInsertionBeepSound)
        val beforeInsertionNumberOfWorkPeriods = numberOfWorkPeriodsFlowAsList.last()
        assertEquals(defaultIntValue, beforeInsertionNumberOfWorkPeriods)
        val beforeInsertionRestPeriodLength = restPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionRestPeriodLength)
        val beforeInsertionWorkPeriodLength = workPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionWorkPeriodLength)
        val beforeInsertionNumberOfCycles = numberCyclesFlowAsList.last()
        assertEquals(defaultIntValue, beforeInsertionNumberOfCycles)
        //insert nothing
        //get Preferences
        val preferencesFlowAsList = mutableListOf<SimpleHiitPreferences>()
        val collectPreferencesJob = launch(UnconfinedTestDispatcher()) {
            testedSimpleHiitDataStoreManager.getPreferences().toList(preferencesFlowAsList)
        }
        //Check that non-inserted settings return actual default values defined in Domain
        val expectedPreferences = SimpleHiitPreferences(
            workPeriodLengthMs = WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
            restPeriodLengthMs = REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
            numberOfWorkPeriods = NUMBER_WORK_PERIODS_DEFAULT,
            beepSoundActive = BEEP_SOUND_ACTIVE_DEFAULT,
            sessionCountDownLengthMs = SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
            PeriodCountDownLengthMs = PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
            selectedExercisesTypes = DEFAULT_SELECTED_EXERCISES_TYPES,
            numberCumulatedCycles = NUMBER_CUMULATED_CYCLES_DEFAULT
        )
        val storedPreferences = preferencesFlowAsList.last()
        assertEquals(expectedPreferences, storedPreferences)
        //cancel all collect jobs
        collectJobExercisesSelected.cancel()
        collectPeriodCountDownJob.cancel()
        collectSessionStartCountdownJob.cancel()
        collectBeepSoundJob.cancel()
        collectNumberOfWorkPeriodsJob.cancel()
        collectRestPeriodLengthJob.cancel()
        collectWorkPeriodLengthJob.cancel()
        collectPreferencesJob.cancel()
        collectJobNumberOfCycles.cancel()
    }

    @Test
    fun checkGetPreferencesPrefDataStoreAbsentValuesReturnDefaults() = runTest {
        val exercisesSelectedFlowAsList = mutableListOf<Set<String>>()
        val collectJobExercisesSelected = launch(UnconfinedTestDispatcher()) {
            retrievePrefStringSet(SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED).toList(
                exercisesSelectedFlowAsList
            )
        }
        val periodCountDownFlowAsList = mutableListOf<Long>()
        val collectPeriodCountDownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                periodCountDownFlowAsList
            )
        }
        val sessionStartCountdownFlowAsList = mutableListOf<Long>()
        val collectSessionStartCountdownJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS).toList(
                sessionStartCountdownFlowAsList
            )
        }
        val beepSoundFlowAsList = mutableListOf<Boolean>()
        val collectBeepSoundJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefBool(SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE).toList(
                beepSoundFlowAsList
            )
        }
        val numberOfWorkPeriodsFlowAsList = mutableListOf<Int>()
        val collectNumberOfWorkPeriodsJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS).toList(
                numberOfWorkPeriodsFlowAsList
            )
        }
        val restPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectRestPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS).toList(
                restPeriodLengthFlowAsList
            )
        }
        val workPeriodLengthFlowAsList = mutableListOf<Long>()
        val collectWorkPeriodLengthJob = launch(UnconfinedTestDispatcher()) {
            retrievePrefLong(SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS).toList(
                workPeriodLengthFlowAsList
            )
        }
        val numberCyclesFlowAsList = mutableListOf<Int>()
        val collectJobNumberOfCycles = launch(UnconfinedTestDispatcher()) {
            retrievePrefInt(SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES).toList(
                numberCyclesFlowAsList
            )
        }
        //check nothing is there first
        val beforeInsertionExercisesSelected = exercisesSelectedFlowAsList.last()
        assertEquals(defaultStringSetValue, beforeInsertionExercisesSelected)
        val beforeInsertionPeriodCountDown = periodCountDownFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionPeriodCountDown)
        val beforeInsertionSessionStartCountdown = sessionStartCountdownFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionSessionStartCountdown)
        val beforeInsertionBeepSound = beepSoundFlowAsList.last()
        assertEquals(defaultBoolValue, beforeInsertionBeepSound)
        val beforeInsertionNumberOfWorkPeriods = numberOfWorkPeriodsFlowAsList.last()
        assertEquals(defaultIntValue, beforeInsertionNumberOfWorkPeriods)
        val beforeInsertionRestPeriodLength = restPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionRestPeriodLength)
        val beforeInsertionWorkPeriodLength = workPeriodLengthFlowAsList.last()
        assertEquals(defaultLongValue, beforeInsertionWorkPeriodLength)
        //insert only some new values
        val testValueExercisesSelected =
            listOf(ExerciseType.LYING, ExerciseType.SQUAT, ExerciseType.LUNGE)
        testedSimpleHiitDataStoreManager.setExercisesTypesSelected(testValueExercisesSelected)
        val testPeriodStartCountdownValue = 456L
        testedSimpleHiitDataStoreManager.setPeriodStartCountdown(testPeriodStartCountdownValue)
        val testBeepSoundValue = !defaultBoolValue
        testedSimpleHiitDataStoreManager.setBeepSound(testBeepSoundValue)
        val testWorkPeriodLengthValue = 123L
        testedSimpleHiitDataStoreManager.setWorkPeriodLength(testWorkPeriodLengthValue)
        val beforeInsertionNumberOfCycles = numberCyclesFlowAsList.last()
        assertEquals(defaultIntValue, beforeInsertionNumberOfCycles)
        //get Preferences
        val preferencesFlowAsList = mutableListOf<SimpleHiitPreferences>()
        val collectPreferencesJob = launch(UnconfinedTestDispatcher()) {
            testedSimpleHiitDataStoreManager.getPreferences().toList(preferencesFlowAsList)
        }
        //Check that non-inserted settings return actual default values defined in Domain
        val expectedPreferences = SimpleHiitPreferences(
            workPeriodLengthMs = testWorkPeriodLengthValue,
            restPeriodLengthMs = REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
            numberOfWorkPeriods = NUMBER_WORK_PERIODS_DEFAULT,
            beepSoundActive = testBeepSoundValue,
            sessionCountDownLengthMs = SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
            PeriodCountDownLengthMs = testPeriodStartCountdownValue,
            selectedExercisesTypes = ExerciseType.values().toList().map{ ExerciseTypeSelected(it, testValueExercisesSelected.contains(it))},
            numberCumulatedCycles = NUMBER_CUMULATED_CYCLES_DEFAULT
        )
        val storedPreferences = preferencesFlowAsList.last()
        assertEquals(expectedPreferences, storedPreferences)
        //cancel all collect jobs
        collectJobExercisesSelected.cancel()
        collectPeriodCountDownJob.cancel()
        collectSessionStartCountdownJob.cancel()
        collectBeepSoundJob.cancel()
        collectNumberOfWorkPeriodsJob.cancel()
        collectRestPeriodLengthJob.cancel()
        collectWorkPeriodLengthJob.cancel()
        collectPreferencesJob.cancel()
        collectJobNumberOfCycles.cancel()
    }

    ////////////
    private fun retrievePrefInt(key: Preferences.Key<Int>): Flow<Int> =
        testDataStore.data.map { it[key] ?: defaultIntValue }
    private fun retrievePrefLong(key: Preferences.Key<Long>): Flow<Long> =
        testDataStore.data.map { it[key] ?: defaultLongValue }

    private fun retrievePrefBool(key: Preferences.Key<Boolean>): Flow<Boolean> =
        testDataStore.data.map { it[key] ?: defaultBoolValue }

    private fun retrievePrefStringSet(key: Preferences.Key<Set<String>>): Flow<Set<String>> =
        testDataStore.data.map { it[key] ?: defaultStringSetValue }

}
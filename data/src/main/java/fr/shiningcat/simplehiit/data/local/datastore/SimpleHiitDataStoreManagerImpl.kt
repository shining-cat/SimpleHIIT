package fr.shiningcat.simplehiit.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.DEFAULT_APP_THEME
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

/**
 * NO @Inject - provided explicitly by LocalDataModuleHilt via @Provides
 * For Koin: constructor is called directly from localDataModule
 */
class SimpleHiitDataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val ioDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger,
) : SimpleHiitDataStoreManager {
    override suspend fun clearAll() {
        hiitLogger.d("SimpleHiitDataStoreManager", "clearAll")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }

    override suspend fun setWorkPeriodLength(durationMs: Long) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setWorkPeriodLength:: $durationMs")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS] =
                    durationMs
            }
        }
    }

    override suspend fun setRestPeriodLength(durationMs: Long) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setRestPeriodLength:: $durationMs")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS] =
                    durationMs
            }
        }
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setNumberOfWorkPeriods:: $number")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS] = number
            }
        }
    }

    override suspend fun setBeepSound(active: Boolean) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setBeepSound:: $active")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE] = active
            }
        }
    }

    override suspend fun setSessionStartCountdown(durationMs: Long) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setSessionStartCountdown:: $durationMs")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS] =
                    durationMs
            }
        }
    }

    override suspend fun setPeriodStartCountdown(durationMs: Long) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setPeriodStartCountdown:: $durationMs")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS] =
                    durationMs
            }
        }
    }

    override suspend fun setNumberOfCumulatedCycles(number: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setNumberOfCumulatedCycles:: $number")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES] = number
            }
        }
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
        val setOfStringExerciseTypes = exercisesTypes.map { it.name }.toSet()
        hiitLogger.d(
            "SimpleHiitDataStoreManager",
            "setExercisesTypesSelected:: $setOfStringExerciseTypes",
        )
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED] =
                    setOfStringExerciseTypes
            }
        }
    }

    override suspend fun setAppTheme(theme: AppTheme) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setAppTheme:: $theme")
        withContext(ioDispatcher) {
            dataStore.edit { preferences ->
                preferences[SimpleHiitDataStoreManager.Keys.APP_THEME] = theme.name
            }
        }
    }

    override fun getPreferences(): Flow<SimpleHiitPreferences> =
        dataStore.data
            .map { preferences ->
                SimpleHiitPreferences(
                    workPeriodLengthMs = retrieveWorkPeriodLength(preferences),
                    restPeriodLengthMs = retrieveRestPeriodLength(preferences),
                    numberOfWorkPeriods = retrieveNumberOfWorkPeriods(preferences),
                    beepSoundActive = retrieveBeepSoundActive(preferences),
                    sessionCountDownLengthMs = retrieveSessionCountDownLengthSeconds(preferences),
                    PeriodCountDownLengthMs = retrievePeriodCountDownLengthSeconds(preferences),
                    selectedExercisesTypes = getSelectedExerciseTypesAsList(preferences),
                    numberCumulatedCycles = retrieveNumberOfCumulatedCycles(preferences),
                    appTheme = retrieveAppTheme(preferences),
                )
            }.catch { exception ->
                // dataStore.data throws an IOException when an error is encountered when reading data
                hiitLogger.e(
                    tag = "SimpleHiitDataStoreManager",
                    msg = "getPreferences - swallowing exception, clearing whole datastore and returning default values:: $exception",
                )
                emit(SimpleHiitPreferences())
            }

    private fun retrieveWorkPeriodLength(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_MILLISECONDS]
            ?: WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT

    private fun retrieveRestPeriodLength(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_MILLISECONDS]
            ?: REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT

    private fun retrieveNumberOfWorkPeriods(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS]
            ?: NUMBER_WORK_PERIODS_DEFAULT

    private fun retrieveBeepSoundActive(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE]
            ?: BEEP_SOUND_ACTIVE_DEFAULT

    private fun retrieveSessionCountDownLengthSeconds(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_MILLISECONDS]
            ?: SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT

    private fun retrievePeriodCountDownLengthSeconds(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS]
            ?: PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT

    private fun getSelectedExerciseTypesAsList(preferences: Preferences): List<ExerciseTypeSelected> {
        val setOfStringExerciseTypes = retrieveSelectedExerciseTypes(preferences)
        val listOfExerciseTypeSelected =
            ExerciseType.entries.map {
                ExerciseTypeSelected(
                    type = it,
                    selected = setOfStringExerciseTypes.contains(it.name),
                )
            }
        return listOfExerciseTypeSelected.toList()
    }

    private fun retrieveSelectedExerciseTypes(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED]
            ?: DEFAULT_SELECTED_EXERCISES_TYPES.map { it.type.name }.toSet()

    private fun retrieveNumberOfCumulatedCycles(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES]
            ?: NUMBER_CUMULATED_CYCLES_DEFAULT

    private fun retrieveAppTheme(preferences: Preferences): AppTheme {
        val themeString = preferences[SimpleHiitDataStoreManager.Keys.APP_THEME]
        return if (themeString != null) {
            runCatching {
                AppTheme.valueOf(themeString)
            }.getOrElse { e ->
                hiitLogger.e("SimpleHiitDataStoreManager", "retrieveAppTheme:: Invalid theme value $themeString, returning default", e)
                DEFAULT_APP_THEME
            }
        } else {
            DEFAULT_APP_THEME
        }
    }
}

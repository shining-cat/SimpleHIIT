package fr.shining_cat.simplehiit.data.local.datastore

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.SimpleHiitPreferences
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

class SimpleHiitDataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>,
    private val hiitLogger: HiitLogger
) : SimpleHiitDataStoreManager {

    override suspend fun clearAll() {
        hiitLogger.d("SimpleHiitDataStoreManager", "clearAll")
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    override suspend fun setWorkPeriodLength(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setWorkPeriodLength:: $durationSeconds")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_SECONDS] = durationSeconds
        }
    }

    override suspend fun setRestPeriodLength(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setRestPeriodLength:: $durationSeconds")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_SECONDS] = durationSeconds
        }
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setNumberOfWorkPeriods:: $number")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS] = number
        }
    }

    override suspend fun setBeepSound(active: Boolean) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setBeepSound:: $active")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE] = active
        }
    }

    override suspend fun setSessionStartCountdown(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setSessionStartCountdown:: $durationSeconds")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_SECONDS] = durationSeconds
        }
    }

    override suspend fun setPeriodStartCountdown(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setPeriodStartCountdown:: $durationSeconds")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_SECONDS] = durationSeconds
        }
    }

    override suspend fun setNumberOfCumulatedCycles(number: Int) {
        hiitLogger.d("SimpleHiitDataStoreManager", "setNumberOfCumulatedCycles:: $number")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES] = number
        }
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>) {
        val setOfStringExerciseTypes = exercisesTypes.map{it.name}.toSet()
        hiitLogger.d("SimpleHiitDataStoreManager", "setExercisesTypesSelected:: $setOfStringExerciseTypes")
        dataStore.edit { preferences ->
            preferences[SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED] = setOfStringExerciseTypes
        }
    }

    override fun getPreferences(): Flow<SimpleHiitPreferences> =
        dataStore.data.catch { exception ->
            // dataStore.data throws an IOException when an error is encountered when reading data
            hiitLogger.e("SimpleHiitDataStoreManager", "getPreferences - swallowing exception, clearing whole datastore and returning default values:: $exception")
           //TODO: should we also filter out CancellationException to avoid blocking the natural handling of cancellation by the coroutine flow
            clearAll()
            SimpleHiitPreferences()
        }.map { preferences ->
            SimpleHiitPreferences(
                workPeriodLength = retrieveWorkPeriodLength(preferences),
                restPeriodLength = retrieveRestPeriodLength(preferences),
                numberOfWorkPeriods = retrieveNumberOfWorkPeriods(preferences),
                beepSoundActive = retrieveBeepSoundActive(preferences),
                sessionCountDownLengthSeconds = retrieveSessionCountDownLengthSeconds(preferences),
                PeriodCountDownLengthSeconds = retrievePeriodCountDownLengthSeconds(preferences),
                selectedExercisesTypes = getSelectedExerciseTypesAsList(preferences),
                numberCumulatedCycles = retrieveNumberOfCumulatedCycles(preferences)
            )
        }

    private fun retrieveWorkPeriodLength(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.WORK_PERIOD_LENGTH_SECONDS]
            ?: WORK_PERIOD_LENGTH_SECONDS_DEFAULT

    private fun retrieveRestPeriodLength(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.REST_PERIOD_LENGTH_SECONDS]
            ?: REST_PERIOD_LENGTH_SECONDS_DEFAULT

    private fun retrieveNumberOfWorkPeriods(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.NUMBER_WORK_PERIODS]
            ?: NUMBER_WORK_PERIODS_DEFAULT

    private fun retrieveBeepSoundActive(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.BEEP_SOUND_ACTIVE]
            ?: BEEP_SOUND_ACTIVE_DEFAULT

    private fun retrieveSessionCountDownLengthSeconds(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.SESSION_COUNTDOWN_LENGTH_SECONDS]
            ?: SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT

    private fun retrievePeriodCountDownLengthSeconds(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.PERIOD_COUNTDOWN_LENGTH_SECONDS]
            ?: PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT

    private suspend fun getSelectedExerciseTypesAsList(preferences: Preferences): List<ExerciseTypeSelected> {
        val setOfStringExerciseTypes = retrieveSelectedExerciseTypes(preferences)
        val listOfExerciseTypeSelected = ExerciseType.values().toList().map {
            ExerciseTypeSelected(
                type = it,
                selected = setOfStringExerciseTypes.contains(it.name)
            )
        }
        if (setOfStringExerciseTypes.isEmpty()) {
            //TODO: Should display warning to user that at least one must be selected
        }
        return listOfExerciseTypeSelected.toList()
    }

    private fun retrieveSelectedExerciseTypes(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.EXERCISE_TYPES_SELECTED]
            ?: DEFAULT_SELECTED_EXERCISES_TYPES.map { it.type.name }.toSet()


    private fun retrieveNumberOfCumulatedCycles(preferences: Preferences) =
        preferences[SimpleHiitDataStoreManager.Keys.NUMBER_CUMULATED_CYCLES]
            ?: NUMBER_CUMULATED_CYCLES_DEFAULT

}
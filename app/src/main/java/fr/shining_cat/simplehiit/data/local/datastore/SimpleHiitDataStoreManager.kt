package fr.shining_cat.simplehiit.data.local.datastore

import androidx.datastore.preferences.core.*
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.SimpleHiitSettings
import fr.shining_cat.simplehiit.domain.models.TotalRepetitionsSetting
import kotlinx.coroutines.flow.Flow

const val SIMPLE_HIIT_DATASTORE_FILENAME = "simple_hiit_datastore_filename"

interface SimpleHiitDataStoreManager {

    object Keys {
        val WORK_PERIOD_LENGTH_SECONDS = intPreferencesKey("work_period_length_seconds")
        val REST_PERIOD_LENGTH_SECONDS = intPreferencesKey("rest_period_length_seconds")
        val NUMBER_WORK_PERIODS = intPreferencesKey("number_work_periods")
        val BEEP_SOUND_ACTIVE = booleanPreferencesKey("beep_sound_active")
        val SESSION_COUNTDOWN_LENGTH_SECONDS = intPreferencesKey("session_countdown_length_seconds")
        val PERIOD_COUNTDOWN_LENGTH_SECONDS = intPreferencesKey("period_countdown_length_seconds")
        val EXERCISE_TYPES_SELECTED = stringSetPreferencesKey("exercise_types_selected")
        val NUMBER_CUMULATED_CYCLES = intPreferencesKey("number_cumulated_cycles")
    }
    //
    suspend fun clearAll()
    //
    suspend fun setWorkPeriodLength(durationSeconds: Int)
    suspend fun setRestPeriodLength(durationSeconds: Int)
    suspend fun setNumberOfWorkPeriods(number: Int)
    suspend fun setBeepSound(active: Boolean)
    suspend fun setSessionStartCountdown(durationSeconds: Int)
    suspend fun setPeriodStartCountdown(durationSeconds: Int)
    suspend fun setNumberOfCumulatedCycles(number: Int)
    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)
    //
    fun getPreferences(): Flow<SimpleHiitSettings>
    fun getNumberOfCumulatedCycles(): Flow<TotalRepetitionsSetting>

}
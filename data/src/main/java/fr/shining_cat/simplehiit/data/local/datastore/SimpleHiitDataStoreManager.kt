package fr.shining_cat.simplehiit.data.local.datastore

import androidx.datastore.preferences.core.*
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.SimpleHiitPreferences
import kotlinx.coroutines.flow.Flow

const val SIMPLE_HIIT_DATASTORE_FILENAME = "simple_hiit_datastore_filename"

interface SimpleHiitDataStoreManager {

    object Keys {
        val WORK_PERIOD_LENGTH_MILLISECONDS = longPreferencesKey("work_period_length_milliseconds")
        val REST_PERIOD_LENGTH_MILLISECONDS = longPreferencesKey("rest_period_length_milliseconds")
        val NUMBER_WORK_PERIODS = intPreferencesKey("number_work_periods")
        val BEEP_SOUND_ACTIVE = booleanPreferencesKey("beep_sound_active")
        val SESSION_COUNTDOWN_LENGTH_MILLISECONDS = longPreferencesKey("session_countdown_length_milliseconds")
        val PERIOD_COUNTDOWN_LENGTH_MILLISECONDS = longPreferencesKey("period_countdown_length_milliseconds")
        val EXERCISE_TYPES_SELECTED = stringSetPreferencesKey("exercise_types_selected")
        val NUMBER_CUMULATED_CYCLES = intPreferencesKey("number_cumulated_cycles")
    }
    //
    suspend fun clearAll()
    //
    suspend fun setWorkPeriodLength(durationMs: Long)
    suspend fun setRestPeriodLength(durationMs: Long)
    suspend fun setNumberOfWorkPeriods(number: Int)
    suspend fun setBeepSound(active: Boolean)
    suspend fun setSessionStartCountdown(durationMs: Long)
    suspend fun setPeriodStartCountdown(durationMs: Long)
    suspend fun setNumberOfCumulatedCycles(number: Int)
    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)
    //
    fun getPreferences(): Flow<SimpleHiitPreferences>

}
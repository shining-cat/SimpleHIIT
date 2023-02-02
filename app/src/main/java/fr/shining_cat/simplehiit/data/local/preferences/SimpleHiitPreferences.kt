package fr.shining_cat.simplehiit.data.local.preferences

import android.content.SharedPreferences
import androidx.core.content.edit
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.REST_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.DefaultValues.WORK_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.BEEP_SOUND_ACTIVE
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.EXERCISE_TYPES_SELECTED
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_CUMULATED_CYCLES
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_WORK_PERIODS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.PERIOD_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.REST_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.SESSION_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.WORK_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.utils.HiitLogger

const val SIMPLE_HIIT_PREFERENCE_FILENAME = "simple_hiit_preference_filename"

interface SimpleHiitPreferences {

    object Keys {
        const val WORK_PERIOD_LENGTH_SECONDS = "work_period_length_seconds"
        const val REST_PERIOD_LENGTH_SECONDS = "rest_period_length_seconds"
        const val NUMBER_WORK_PERIODS = "number_work_periods"
        const val BEEP_SOUND_ACTIVE = "beep_sound_active"
        const val SESSION_COUNTDOWN_LENGTH_SECONDS = "session_countdown_length_seconds"
        const val PERIOD_COUNTDOWN_LENGTH_SECONDS = "period_countdown_length_seconds"
        const val NUMBER_CUMULATED_CYCLES = "number_cumulated_cycles"
        const val EXERCISE_TYPES_SELECTED = "exercise_types_selected"
    }

    object DefaultValues {
        const val WORK_PERIOD_LENGTH_SECONDS_DEFAULT = 20
        const val REST_PERIOD_LENGTH_SECONDS_DEFAULT = 10
        const val NUMBER_WORK_PERIODS_DEFAULT = 8
        const val BEEP_SOUND_ACTIVE_DEFAULT = true
        const val SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT = 15
        const val PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT = 5
        const val NUMBER_CUMULATED_CYCLES_DEFAULT = 1
    }

    suspend fun clearAll()

    suspend fun setWorkPeriodLength(durationSeconds: Int)
    suspend fun getWorkPeriodLengthSeconds(): Int

    suspend fun setRestPeriodLength(durationSeconds: Int)
    suspend fun getRestPeriodLengthSeconds(): Int

    suspend fun setNumberOfWorkPeriods(number: Int)
    suspend fun getNumberOfWorkPeriods(): Int

    suspend fun setBeepSound(active: Boolean)
    suspend fun getBeepSoundActive(): Boolean

    suspend fun setSessionStartCountdown(durationSeconds: Int)
    suspend fun getSessionStartCountdown(): Int

    suspend fun setPeriodStartCountdown(durationSeconds: Int)
    suspend fun getPeriodStartCountdown(): Int

    suspend fun setNumberOfCumulatedCycles(number: Int)
    suspend fun getNumberOfCumulatedCycles(): Int

    suspend fun setExercisesTypesSelected(exercisesTypes: List<ExerciseType>)
    suspend fun getExercisesTypesSelected(): List<ExerciseType>

}

internal class SimpleHiitPreferencesImpl(
    private val sharedPreferences: SharedPreferences,
    private val hiitLogger: HiitLogger
) : SimpleHiitPreferences {

    override suspend fun clearAll() {
        sharedPreferences.edit { clear() }
    }

    override suspend fun setWorkPeriodLength(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setWorkPeriodLength: $durationSeconds")
        sharedPreferences.edit(commit = true) { putInt(WORK_PERIOD_LENGTH_SECONDS, durationSeconds) }
    }

    override suspend fun getWorkPeriodLengthSeconds(): Int {
        val durationSeconds = sharedPreferences.getInt(WORK_PERIOD_LENGTH_SECONDS, WORK_PERIOD_LENGTH_SECONDS_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getWorkPeriodLengthSeconds: $durationSeconds")
        return durationSeconds
    }

    override suspend fun setRestPeriodLength(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setRestPeriodLength: $durationSeconds")
        sharedPreferences.edit(commit = true) { putInt(REST_PERIOD_LENGTH_SECONDS, durationSeconds) }
    }

    override suspend fun getRestPeriodLengthSeconds(): Int {
        val durationSeconds = sharedPreferences.getInt(REST_PERIOD_LENGTH_SECONDS, REST_PERIOD_LENGTH_SECONDS_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getRestPeriodLengthSeconds: $durationSeconds")
        return durationSeconds
    }

    override suspend fun setNumberOfWorkPeriods(number: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setNumberOfWorkPeriods: $number")
        sharedPreferences.edit(commit = true) { putInt(NUMBER_WORK_PERIODS, number) }
    }

    override suspend fun getNumberOfWorkPeriods(): Int {
        val number = sharedPreferences.getInt(NUMBER_WORK_PERIODS, NUMBER_WORK_PERIODS_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getNumberOfWorkPeriods: $number")
        return number
    }

    override suspend fun setBeepSound(active: Boolean) {
        hiitLogger.d("SimpleHiitPreferences", "setBeepSound: $active")
        sharedPreferences.edit(commit = true) { putBoolean(BEEP_SOUND_ACTIVE, active) }
    }

    override suspend fun getBeepSoundActive(): Boolean {
        val active = sharedPreferences.getBoolean(BEEP_SOUND_ACTIVE, BEEP_SOUND_ACTIVE_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getBeepSound: $active")
        return active
    }

    override suspend fun setSessionStartCountdown(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setSessionStartCountdown: $durationSeconds")
        sharedPreferences.edit(commit = true) { putInt(SESSION_COUNTDOWN_LENGTH_SECONDS, durationSeconds) }
    }

    override suspend fun getSessionStartCountdown(): Int {
        val durationSeconds = sharedPreferences.getInt(SESSION_COUNTDOWN_LENGTH_SECONDS, SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getSessionStartCountdown: $durationSeconds")
        return durationSeconds
    }

    override suspend fun setPeriodStartCountdown(durationSeconds: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setPeriodStartCountdown: $durationSeconds")
        sharedPreferences.edit(commit = true) { putInt(PERIOD_COUNTDOWN_LENGTH_SECONDS, durationSeconds) }
    }

    override suspend fun getPeriodStartCountdown(): Int {
        val durationSeconds = sharedPreferences.getInt(PERIOD_COUNTDOWN_LENGTH_SECONDS, PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "getPeriodStartCountdown: $durationSeconds")
        return durationSeconds
    }

    override suspend fun setNumberOfCumulatedCycles(number: Int) {
        hiitLogger.d("SimpleHiitPreferences", "setNumberOfCumulatedCycles: $number")
        sharedPreferences.edit(commit = true) { putInt(NUMBER_CUMULATED_CYCLES, number) }
    }

    override suspend fun getNumberOfCumulatedCycles(): Int {
        val number = sharedPreferences.getInt(NUMBER_CUMULATED_CYCLES, NUMBER_CUMULATED_CYCLES_DEFAULT)
        hiitLogger.d("SimpleHiitPreferences", "setNumberOfCumulatedCycles: $number")
        return number
    }

    override suspend fun setExercisesTypesSelected(exercisesTypes:List<ExerciseType>){
        val setOfStringExerciseTypes = exercisesTypes.map{it.name}.toSet()
        hiitLogger.d("SimpleHiitPreferences", "setExercisesTypesSelected: $setOfStringExerciseTypes")
        sharedPreferences.edit(commit = true) { putStringSet(EXERCISE_TYPES_SELECTED, setOfStringExerciseTypes) }
    }
    override suspend fun getExercisesTypesSelected():List<ExerciseType>{
        val setOfStringExerciseTypes = sharedPreferences.getStringSet(EXERCISE_TYPES_SELECTED, setOf<String>())
        val listOfExerciseTypes = mutableListOf<ExerciseType>()
        if(!setOfStringExerciseTypes.isNullOrEmpty()){
            for (typeName in setOfStringExerciseTypes) {
                try {
                    val exerciseType = ExerciseType.valueOf(typeName)
                    listOfExerciseTypes.add(exerciseType)
                } catch (exception: IllegalArgumentException) {
                    hiitLogger.e(
                        "SimpleHiitPreferences",
                        "getExercisesTypesSelected corruption found, resetting stored list to complete list",
                        exception
                    )
                    listOfExerciseTypes.clear()
                    listOfExerciseTypes.addAll(ExerciseType.values())
                    val completeSet = ExerciseType.values().map { it.name }.toSet()
                    sharedPreferences.edit(commit = true) { putStringSet(EXERCISE_TYPES_SELECTED, completeSet) }
                    break
                }
            }
        }
        hiitLogger.d("SimpleHiitPreferences", "getExercisesTypesSelected: $listOfExerciseTypes")
        return listOfExerciseTypes.toList()
    }

}
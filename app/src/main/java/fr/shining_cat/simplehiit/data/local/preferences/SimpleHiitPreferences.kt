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
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_CUMULATED_CYCLES
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.NUMBER_WORK_PERIODS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.PERIOD_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.REST_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.SELECTED_USERS_IDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.SESSION_COUNTDOWN_LENGTH_SECONDS
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences.Keys.WORK_PERIOD_LENGTH_SECONDS
import fr.shining_cat.simplehiit.utils.HiitLogger

const val SIMPLE_HIIT_PREFERENCE_FILENAME = "simple_hiit_preference_filename"

interface SimpleHiitPreferences {

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

    suspend fun setUsersSelected(users: List<Long>)
    suspend fun getUsersSelected(): List<Long>

    suspend fun setNumberOfCumulatedCycles(number: Int)
    suspend fun getNumberOfCumulatedCycles(): Int

    object Keys {
        const val WORK_PERIOD_LENGTH_SECONDS = "work_period_length_seconds"
        const val REST_PERIOD_LENGTH_SECONDS = "rest_period_length_seconds"
        const val NUMBER_WORK_PERIODS = "number_work_periods"
        const val BEEP_SOUND_ACTIVE = "beep_sound_active"
        const val SESSION_COUNTDOWN_LENGTH_SECONDS = "session_countdown_length_seconds"
        const val PERIOD_COUNTDOWN_LENGTH_SECONDS = "period_countdown_length_seconds"
        const val NUMBER_CUMULATED_CYCLES = "number_cumulated_cycles"
        const val SELECTED_USERS_IDS = "selected_users_ids"
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

    override suspend fun setUsersSelected(users: List<Long>) {
        val setOfStringIds = users.map{it.toString()}.toSet()
        hiitLogger.d("SimpleHiitPreferences", "setUsersSelected: $setOfStringIds")
        sharedPreferences.edit(commit = true) { putStringSet(SELECTED_USERS_IDS, setOfStringIds) }
    }

    override suspend fun getUsersSelected(): List<Long> {
        val setOfStringIds = sharedPreferences.getStringSet(SELECTED_USERS_IDS, setOf<String>())
        val listOfLongIds = mutableListOf<Long>()
        if(!setOfStringIds.isNullOrEmpty()){
            for (userId in setOfStringIds) {
                try {
                    val userIdLong = userId.toLong()
                    listOfLongIds.add(userIdLong)
                } catch (exception: NumberFormatException) {
                    hiitLogger.e(
                        "SimpleHiitPreferences",
                        "getUsersSelected corruption found, resetting stored list to empty list",
                        exception
                    )
                    sharedPreferences.edit(commit = true) { putStringSet(SELECTED_USERS_IDS, emptySet()) }
                    listOfLongIds.clear()
                    break
                }
            }
        }
        hiitLogger.d("SimpleHiitPreferences", "getUsersSelected: $listOfLongIds")
        return listOfLongIds.toList()
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

    //TODO:add these once the exercises classes have been created:
//    suspend fun setExercisesTypesSelected(exercisesTypes:List<ExerciseType>)
//    suspend fun getExercisesTypesSelected():List<ExerciseType>


}
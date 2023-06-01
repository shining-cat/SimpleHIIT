package fr.shining_cat.simplehiit.commondomain

import fr.shining_cat.simplehiit.commondomain.models.ExerciseType
import fr.shining_cat.simplehiit.commondomain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
object Constants {
    object SettingsDefaultValues {
        const val WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT = 20000L
        const val REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT = 10000L
        const val NUMBER_WORK_PERIODS_DEFAULT = 8
        const val BEEP_SOUND_ACTIVE_DEFAULT = true
        const val SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT = 15000L
        const val PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT = 5000L
        const val NUMBER_CUMULATED_CYCLES_DEFAULT = 1
        val DEFAULT_SELECTED_EXERCISES_TYPES = ExerciseType.values().toList().map { ExerciseTypeSelected(it, true) }
    }
    const val NO_RESULTS_FOUND = "no results found"

    enum class Errors(val code:String){
        DATABASE_FETCH_FAILED("0101"),
        DATABASE_INSERT_FAILED("0102"),
        DATABASE_DELETE_FAILED("0103"),
        DATABASE_UPDATE_FAILED("0104"),
        NO_USERS_FOUND("0201"),
        NO_USER_PROVIDED("0202"),
        USER_NAME_TAKEN("0203"),
        EMPTY_RESULT("0301"),
        CONVERSION_ERROR("0401"),
        LAUNCH_SESSION("0501"),
        SESSION_NOT_FOUND("0502")
    }

    enum class InputError(){
        NONE,
        WRONG_FORMAT,
        TOO_LONG,
        TOO_SHORT,
        VALUE_TOO_SMALL,
        VALUE_TOO_BIG,
        VALUE_ALREADY_TAKEN
    }

}
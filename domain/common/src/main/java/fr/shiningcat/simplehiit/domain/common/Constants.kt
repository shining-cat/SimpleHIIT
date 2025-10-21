package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected

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
        val DEFAULT_SELECTED_EXERCISES_TYPES =
            ExerciseType.entries.map { ExerciseTypeSelected(it, true) }
    }

    const val NO_RESULTS_FOUND = "no results found"

    enum class Errors(
        val code: String,
    ) {
        DATABASE_FETCH_FAILED("0101"),
        DATABASE_INSERT_FAILED("0102"),
        DATABASE_DELETE_FAILED("0103"),
        DATABASE_UPDATE_FAILED("0104"),
        NO_USERS_FOUND("0201"),
        NO_USER_PROVIDED("0202"),
        USER_NAME_TAKEN("0203"),
        NO_SELECTED_USERS_FOUND("0204"),
        EMPTY_RESULT("0301"),
        CONVERSION_ERROR("0401"),
        SESSION_NOT_FOUND("0501"),
        LANGUAGE_SET_FAILED("0601"),
    }

    enum class InputError {
        NONE,
        WRONG_FORMAT,
        TOO_LONG,
        VALUE_EMPTY,
        VALUE_TOO_SMALL,
        VALUE_TOO_BIG,
        VALUE_ALREADY_TAKEN,
    }
}

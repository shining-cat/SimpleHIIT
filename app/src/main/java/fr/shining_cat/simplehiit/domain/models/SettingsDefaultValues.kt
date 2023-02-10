package fr.shining_cat.simplehiit.domain.models

object SettingsDefaultValues {
    const val WORK_PERIOD_LENGTH_SECONDS_DEFAULT = 20
    const val REST_PERIOD_LENGTH_SECONDS_DEFAULT = 10
    const val NUMBER_WORK_PERIODS_DEFAULT = 8
    const val BEEP_SOUND_ACTIVE_DEFAULT = true
    const val SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT = 15
    const val PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT = 5
    const val NUMBER_CUMULATED_CYCLES_DEFAULT = 1
    val DEFAULT_SELECTED_EXERCISES_TYPES = ExerciseType.values().toList()
}
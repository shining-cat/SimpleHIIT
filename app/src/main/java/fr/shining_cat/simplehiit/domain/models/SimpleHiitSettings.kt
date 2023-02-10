package fr.shining_cat.simplehiit.domain.models

data class SimpleHiitSettings(
    val workPeriodLength: Int = SettingsDefaultValues.WORK_PERIOD_LENGTH_SECONDS_DEFAULT,
    val restPeriodLength: Int = SettingsDefaultValues.REST_PERIOD_LENGTH_SECONDS_DEFAULT,
    val numberOfWorkPeriods: Int = SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT,
    val beepSoundActive: Boolean = SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT,
    val sessionCountDownLengthSeconds: Int = SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT,
    val PeriodCountDownLengthSeconds: Int = SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT,
    val selectedExercisesTypes: List<ExerciseType> = SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
)
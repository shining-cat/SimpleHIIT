package fr.shining_cat.simplehiit.domain.models

import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT
import fr.shining_cat.simplehiit.domain.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_SECONDS_DEFAULT

data class SimpleHiitPreferences(
    val workPeriodLength: Int = WORK_PERIOD_LENGTH_SECONDS_DEFAULT,
    val restPeriodLength: Int = REST_PERIOD_LENGTH_SECONDS_DEFAULT,
    val numberOfWorkPeriods: Int = NUMBER_WORK_PERIODS_DEFAULT,
    val beepSoundActive: Boolean = BEEP_SOUND_ACTIVE_DEFAULT,
    val sessionCountDownLengthSeconds: Int = SESSION_COUNTDOWN_LENGTH_SECONDS_DEFAULT,
    val PeriodCountDownLengthSeconds: Int = PERIOD_COUNTDOWN_LENGTH_SECONDS_DEFAULT,
    val selectedExercisesTypes: List<ExerciseTypeSelected> = DEFAULT_SELECTED_EXERCISES_TYPES,
    val numberCumulatedCycles: Int = NUMBER_CUMULATED_CYCLES_DEFAULT
)
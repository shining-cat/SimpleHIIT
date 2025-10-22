package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT
import fr.shiningcat.simplehiit.domain.common.Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT

data class SimpleHiitPreferences(
    val workPeriodLengthMs: Long = WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
    val restPeriodLengthMs: Long = REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
    val numberOfWorkPeriods: Int = NUMBER_WORK_PERIODS_DEFAULT,
    val beepSoundActive: Boolean = BEEP_SOUND_ACTIVE_DEFAULT,
    val sessionCountDownLengthMs: Long = SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
    val PeriodCountDownLengthMs: Long = PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
    val selectedExercisesTypes: List<ExerciseTypeSelected> = DEFAULT_SELECTED_EXERCISES_TYPES,
    val numberCumulatedCycles: Int = NUMBER_CUMULATED_CYCLES_DEFAULT,
)

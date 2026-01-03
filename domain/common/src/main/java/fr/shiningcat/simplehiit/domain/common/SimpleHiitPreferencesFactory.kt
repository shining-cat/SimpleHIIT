package fr.shiningcat.simplehiit.domain.common

import fr.shiningcat.simplehiit.domain.common.models.SimpleHiitPreferences

/**
 * Factory for creating SimpleHiitPreferences instances with domain-defined default values.
 * Centralizes default configuration for application preferences.
 */
object SimpleHiitPreferencesFactory {
    fun createDefault(): SimpleHiitPreferences =
        SimpleHiitPreferences(
            workPeriodLengthMs = Constants.SettingsDefaultValues.WORK_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
            restPeriodLengthMs = Constants.SettingsDefaultValues.REST_PERIOD_LENGTH_MILLISECONDS_DEFAULT,
            numberOfWorkPeriods = Constants.SettingsDefaultValues.NUMBER_WORK_PERIODS_DEFAULT,
            beepSoundActive = Constants.SettingsDefaultValues.BEEP_SOUND_ACTIVE_DEFAULT,
            sessionCountDownLengthMs = Constants.SettingsDefaultValues.SESSION_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
            PeriodCountDownLengthMs = Constants.SettingsDefaultValues.PERIOD_COUNTDOWN_LENGTH_MILLISECONDS_DEFAULT,
            selectedExercisesTypes = Constants.SettingsDefaultValues.DEFAULT_SELECTED_EXERCISES_TYPES,
            numberCumulatedCycles = Constants.SettingsDefaultValues.NUMBER_CUMULATED_CYCLES_DEFAULT,
            appTheme = Constants.SettingsDefaultValues.DEFAULT_APP_THEME,
        )
}

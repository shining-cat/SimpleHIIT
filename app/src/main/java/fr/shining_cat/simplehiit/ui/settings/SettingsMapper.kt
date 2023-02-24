package fr.shining_cat.simplehiit.ui.settings

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.GeneralSettings
import fr.shining_cat.simplehiit.ui.settings.SettingsViewState.SettingsError
import fr.shining_cat.simplehiit.ui.settings.SettingsViewState.SettingsNominal
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class SettingsMapper @Inject constructor(private val hiitLogger: HiitLogger) {

    fun map(generalSettingsOutput: Output<GeneralSettings>): SettingsViewState {
        return when (generalSettingsOutput) {
            is Output.Success<GeneralSettings> -> {
                val generalSettings = generalSettingsOutput.result
                val totalCycleLength =
                    generalSettings.workPeriodLengthMs * generalSettings.numberOfWorkPeriods + generalSettings.restPeriodLengthMs * (generalSettings.numberOfWorkPeriods - 1)
                SettingsNominal(
                    workPeriodLengthMs = generalSettings.workPeriodLengthMs,
                    restPeriodLengthMs = generalSettings.restPeriodLengthMs,
                    numberOfWorkPeriods = generalSettings.numberOfWorkPeriods,
                    totalCycleLengthMs = totalCycleLength,
                    beepSoundCountDownActive = generalSettings.beepSoundCountDownActive,
                    sessionStartCountDownLengthMs = generalSettings.sessionStartCountDownLengthMs,
                    periodsStartCountDownLengthMs = generalSettings.periodsStartCountDownLengthMs,
                    users = generalSettings.users,
                    exerciseTypes = generalSettings.exerciseTypes,
                )
            }
            is Output.Error -> SettingsError(generalSettingsOutput.errorCode.code)
        }
    }
}
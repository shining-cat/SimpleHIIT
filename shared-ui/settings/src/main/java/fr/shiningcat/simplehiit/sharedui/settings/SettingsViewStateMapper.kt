/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewState.Error
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewState.Nominal
import kotlin.math.roundToInt

class SettingsViewStateMapper(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val logger: HiitLogger,
) {
    fun map(generalSettingsOutput: Output<GeneralSettings>): SettingsViewState =
        when (generalSettingsOutput) {
            is Output.Success<GeneralSettings> -> {
                val generalSettings = generalSettingsOutput.result
                val cycleLengthDisplay =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        durationMs = generalSettings.cycleLengthMs,
                        formatStyle = DurationFormatStyle.SHORT,
                    )
                Nominal(
                    workPeriodLengthAsSeconds = durationMsAsSeconds(generalSettings.workPeriodLengthMs),
                    restPeriodLengthAsSeconds = durationMsAsSeconds(generalSettings.restPeriodLengthMs),
                    numberOfWorkPeriods = generalSettings.numberOfWorkPeriods.toString(),
                    totalCycleLength = cycleLengthDisplay,
                    beepSoundCountDownActive = generalSettings.beepSoundCountDownActive,
                    sessionStartCountDownLengthAsSeconds = durationMsAsSeconds(generalSettings.sessionStartCountDownLengthMs),
                    periodsStartCountDownLengthAsSeconds = durationMsAsSeconds(generalSettings.periodsStartCountDownLengthMs),
                    users = generalSettings.users,
                    exerciseTypes = generalSettings.exerciseTypes,
                    currentLanguage = generalSettings.currentLanguage,
                    currentTheme = generalSettings.currentTheme,
                )
            }
            is Output.Error -> {
                Error(generalSettingsOutput.errorCode.code)
            }
        }

    private fun durationMsAsSeconds(durationMs: Long): String {
        val asSeconds = durationMs.toDouble() / 1000L.toDouble()
        return asSeconds.roundToInt().toString()
    }
}

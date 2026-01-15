/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
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
                // these other values are only displayed as seconds to the user, as we don't expect any other format to be relevant
                val workPeriodLengthAsSeconds =
                    durationMsAsSeconds(generalSettings.workPeriodLengthMs)
                val restPeriodLengthAsSeconds =
                    durationMsAsSeconds(generalSettings.restPeriodLengthMs)
                val sessionStartCountDownLengthAsSeconds =
                    durationMsAsSeconds(generalSettings.sessionStartCountDownLengthMs)
                val periodsStartCountDownLengthAsSeconds =
                    durationMsAsSeconds(generalSettings.periodsStartCountDownLengthMs)
                if (workPeriodLengthAsSeconds == null ||
                    restPeriodLengthAsSeconds == null ||
                    sessionStartCountDownLengthAsSeconds == null ||
                    periodsStartCountDownLengthAsSeconds == null
                ) {
                    Error(DomainError.CONVERSION_ERROR.code)
                } else {
                    Nominal(
                        workPeriodLengthAsSeconds = workPeriodLengthAsSeconds,
                        restPeriodLengthAsSeconds = restPeriodLengthAsSeconds,
                        numberOfWorkPeriods = generalSettings.numberOfWorkPeriods.toString(),
                        totalCycleLength = cycleLengthDisplay,
                        beepSoundCountDownActive = generalSettings.beepSoundCountDownActive,
                        sessionStartCountDownLengthAsSeconds = sessionStartCountDownLengthAsSeconds,
                        periodsStartCountDownLengthAsSeconds = periodsStartCountDownLengthAsSeconds,
                        users = generalSettings.users,
                        exerciseTypes = generalSettings.exerciseTypes,
                        currentLanguage = generalSettings.currentLanguage,
                        currentTheme = generalSettings.currentTheme,
                    )
                }
            }
            is Output.Error -> {
                Error(generalSettingsOutput.errorCode.code)
            }
        }

    private fun durationMsAsSeconds(durationMs: Long): String? {
        val asSeconds = durationMs.toDouble() / 1000L.toDouble()
        return runCatching {
            asSeconds.roundToInt().toString()
        }.onFailure { exception ->
            // this should never happen as we can't get a NaN as a Long
            logger.e("SettingsMapper", "durationMsAsSeconds", exception)
        }.getOrNull()
    }
}

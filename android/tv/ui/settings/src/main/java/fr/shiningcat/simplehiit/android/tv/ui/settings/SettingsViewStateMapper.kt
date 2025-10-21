package fr.shiningcat.simplehiit.android.tv.ui.settings

import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsViewState.Error
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsViewState.Nominal
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import javax.inject.Inject
import kotlin.math.roundToInt

class SettingsViewStateMapper
    @Inject
    constructor(
        private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
        private val hiitLogger: HiitLogger,
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
                        Error(Constants.Errors.CONVERSION_ERROR.code)
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
                        )
                    }
                }

                is Output.Error -> Error(generalSettingsOutput.errorCode.code)
            }

        private fun durationMsAsSeconds(durationMs: Long): String? {
            val asSeconds = durationMs.toDouble() / 1000L.toDouble()
            return try {
                asSeconds.roundToInt().toString()
            } catch (exception: IllegalArgumentException) {
                // this should never happen as we can't get a NaN as a Long
                hiitLogger.e("SettingsMapper", "durationMsAsSeconds", exception)
                null
            }
        }
    }

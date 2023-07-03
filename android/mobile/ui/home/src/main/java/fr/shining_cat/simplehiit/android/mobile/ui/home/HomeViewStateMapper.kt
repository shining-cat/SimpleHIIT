package fr.shining_cat.simplehiit.android.mobile.ui.home

import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Error
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.MissingUsers
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Nominal
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.HomeSettings
import fr.shining_cat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import javax.inject.Inject

class HomeViewStateMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    @Suppress("UNUSED_PARAMETER")
    private val hiitLogger: HiitLogger
) {

    fun map(
        homeSettingsOutput: Output<HomeSettings>,
        durationStringFormatter: DurationStringFormatter
    ): HomeViewState {
        return when (homeSettingsOutput) {
            is Output.Success<HomeSettings> -> {
                val cycleLengthDisplay =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        homeSettingsOutput.result.cycleLengthMs,
                        durationStringFormatter
                    )
                val totalSessionLength = homeSettingsOutput.result.cycleLengthMs.times(homeSettingsOutput.result.numberCumulatedCycles)
                val totalSessionLengthFormatted = formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    totalSessionLength,
                    durationStringFormatter
                )

                if (homeSettingsOutput.result.users.isEmpty()) {
                    MissingUsers(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthDisplay,
                        totalSessionLengthFormatted = totalSessionLengthFormatted
                    )
                } else {
                    Nominal(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthDisplay,
                        users = homeSettingsOutput.result.users,
                        totalSessionLengthFormatted = totalSessionLengthFormatted
                    )
                }
            }

            is Output.Error -> Error(homeSettingsOutput.errorCode.code)
        }
    }
}
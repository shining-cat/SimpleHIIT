package fr.shiningcat.simplehiit.android.tv.ui.home

import fr.shiningcat.simplehiit.android.tv.ui.home.HomeViewState.Error
import fr.shiningcat.simplehiit.android.tv.ui.home.HomeViewState.MissingUsers
import fr.shiningcat.simplehiit.android.tv.ui.home.HomeViewState.Nominal
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.HomeSettings
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
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
                val cycleLengthFormatted =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        homeSettingsOutput.result.cycleLengthMs,
                        durationStringFormatter
                    )
                val totalSessionLength =
                    homeSettingsOutput.result.cycleLengthMs.times(homeSettingsOutput.result.numberCumulatedCycles)
                val totalSessionLengthFormatted =
                    formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                        totalSessionLength,
                        durationStringFormatter
                    )

                if (homeSettingsOutput.result.users.isEmpty()) {
                    MissingUsers(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthFormatted,
                        totalSessionLengthFormatted = totalSessionLengthFormatted
                    )
                } else {
                    Nominal(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthFormatted,
                        users = homeSettingsOutput.result.users,
                        totalSessionLengthFormatted = totalSessionLengthFormatted
                    )
                }
            }

            is Output.Error -> Error(homeSettingsOutput.errorCode.code)
        }
    }
}
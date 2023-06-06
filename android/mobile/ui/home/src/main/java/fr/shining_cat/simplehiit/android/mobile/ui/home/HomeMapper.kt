package fr.shining_cat.simplehiit.android.mobile.ui.home

import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.HomeSettings
import fr.shining_cat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Error
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.MissingUsers
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState.Nominal
import javax.inject.Inject

class HomeMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
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
                if (homeSettingsOutput.result.users.isEmpty()) {
                    MissingUsers(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthDisplay
                    )
                } else {
                    Nominal(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLength = cycleLengthDisplay,
                        users = homeSettingsOutput.result.users
                    )
                }
            }

            is Output.Error -> Error(homeSettingsOutput.errorCode.code)
        }
    }
}
package fr.shining_cat.simplehiit.ui.home

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.HomeSettings
import fr.shining_cat.simplehiit.ui.home.HomeViewState.*
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class HomeMapper @Inject constructor(private val hiitLogger: HiitLogger) {

    fun map(homeSettingsOutput: Output<HomeSettings>):HomeViewState{
        return when(homeSettingsOutput){
            is Output.Success<HomeSettings> -> {
                if(homeSettingsOutput.result.users.isEmpty()){
                    HomeMissingUsers(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLengthMs = homeSettingsOutput.result.cycleLengthMs
                    )
                } else{
                    HomeNominal(
                        numberCumulatedCycles = homeSettingsOutput.result.numberCumulatedCycles,
                        cycleLengthMs = homeSettingsOutput.result.cycleLengthMs,
                        users = homeSettingsOutput.result.users
                    )
                }
            }
            is Output.Error -> HomeError(homeSettingsOutput.errorCode.code)
        }
    }
}
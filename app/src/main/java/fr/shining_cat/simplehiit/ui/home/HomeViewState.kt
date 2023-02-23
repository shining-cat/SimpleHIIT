package fr.shining_cat.simplehiit.ui.home

import fr.shining_cat.simplehiit.domain.models.User

sealed class HomeViewState{
    object HomeLoading:HomeViewState()
    data class HomeSettingsNominal(val numberCumulatedCycles: Int, val users:List<User>):HomeViewState()
    data class HomeSettingsMissingUsers(val numberCumulatedCycles: Int):HomeViewState()
    data class HomeSettingsError(val errorCode: String):HomeViewState()
}

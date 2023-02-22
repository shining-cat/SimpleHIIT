package fr.shining_cat.simplehiit.ui.home

import fr.shining_cat.simplehiit.domain.models.User

sealed class HomeViewState{
    object HomeLoading:HomeViewState()
    data class HomeSettingsState(val numberCumulatedCycles: Int, val users:List<User>):HomeViewState()
    data class HomeSettingsMissingUsersState(val numberCumulatedCycles: Int):HomeViewState()
    data class HomeSettingsErrorState(val errorCode: String):HomeViewState()
}

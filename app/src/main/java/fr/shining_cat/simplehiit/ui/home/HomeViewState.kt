package fr.shining_cat.simplehiit.ui.home

import fr.shining_cat.simplehiit.domain.models.User

sealed class HomeViewState{
    object HomeLoading:HomeViewState()
    data class HomeNominal(val numberCumulatedCycles: Int, val cycleLength:String, val users:List<User>):HomeViewState()
    data class HomeMissingUsers(val numberCumulatedCycles: Int, val cycleLength:String):HomeViewState()
    data class HomeError(val errorCode: String):HomeViewState()
}

sealed class HomeDialog(){
    object None: HomeDialog()
    data class HomeDialogInputNumberCycles(val initialNumberOfCycles: Int): HomeDialog()
    data class HomeDialogConfirmWholeReset(val errorCode: String): HomeDialog()
}

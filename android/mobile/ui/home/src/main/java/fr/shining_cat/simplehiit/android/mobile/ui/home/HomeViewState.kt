package fr.shining_cat.simplehiit.android.mobile.ui.home

import fr.shining_cat.simplehiit.commondomain.models.User

sealed class HomeViewState{
    object Loading: HomeViewState()
    data class Nominal(val numberCumulatedCycles: Int, val cycleLength:String, val users:List<User>):
        HomeViewState()
    data class MissingUsers(val numberCumulatedCycles: Int, val cycleLength:String): HomeViewState()
    data class Error(val errorCode: String): HomeViewState()
}

sealed class HomeDialog(){
    object None: HomeDialog()
    data class InputNumberCycles(val initialNumberOfCycles: Int): HomeDialog()
    object ConfirmWholeReset: HomeDialog()
}

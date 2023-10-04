package fr.shining_cat.simplehiit.android.common

sealed class Screen(val route: String){
    object Home: Screen(route = "home")
    object Settings: Screen(route = "settings")
    object Statistics: Screen(route = "statistics")
    object Session: Screen(route = "session")
}

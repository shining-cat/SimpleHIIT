package fr.shining_cat.simplehiit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.shining_cat.simplehiit.Screen
import fr.shining_cat.simplehiit.ui.home.HomeScreen
import fr.shining_cat.simplehiit.ui.session.SessionScreen
import fr.shining_cat.simplehiit.ui.settings.SettingsScreen
import fr.shining_cat.simplehiit.ui.statistics.StatisticsScreen

@Composable
fun SimpleHiitNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {HomeScreen(navController)}
        composable(route = Screen.Settings.route) {SettingsScreen(navController)}
        composable(route = Screen.Statistics.route) {StatisticsScreen(navController)}
        composable(route = Screen.Session.route) {SessionScreen(navController)}
    }
}
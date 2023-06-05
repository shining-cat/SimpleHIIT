package fr.shining_cat.simplehiit.ui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeScreen
import fr.shining_cat.simplehiit.android.mobile.common.Screen
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionScreen
import fr.shining_cat.simplehiit.ui.settings.SettingsScreen
import fr.shining_cat.simplehiit.ui.statistics.StatisticsScreen
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun SimpleHiitNavigation(
    hiitLogger: HiitLogger
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navController,
                hiitLogger
            )
        }
        composable(route = Screen.Settings.route) { SettingsScreen(navController, hiitLogger) }
        composable(route = Screen.Statistics.route) { StatisticsScreen(navController, hiitLogger) }
        composable(route = Screen.Session.route) { SessionScreen(navController, hiitLogger) }
    }
}
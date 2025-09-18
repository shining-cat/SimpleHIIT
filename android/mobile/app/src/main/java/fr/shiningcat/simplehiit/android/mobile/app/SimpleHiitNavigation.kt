package fr.shiningcat.simplehiit.android.mobile.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeScreen
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionScreen
import fr.shiningcat.simplehiit.android.mobile.ui.settings.SettingsScreen
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsScreen
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@Composable
fun SimpleHiitNavigation(
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route,
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navigateTo = {
                    hiitLogger.d("SimpleHiitNavigation", "HomeScreen::navigateTo::$it")
                    navController.navigate(it)
                },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger,
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                navigateTo = {
                    hiitLogger.d("SimpleHiitNavigation", "SettingsScreen::navigateTo::$it")
                    navController.navigate(it)
                },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger,
            )
        }
        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                navigateTo = {
                    hiitLogger.d("SimpleHiitNavigation", "StatisticsScreen::navigateTo::$it")
                    navController.navigate(it)
                },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger,
            )
        }
        composable(route = Screen.Session.route) {
            SessionScreen(
                navigateUp = {
                    hiitLogger.d("SimpleHiitNavigation", "SessionScreen::navigateTo::$it")
                    navController.navigateUp()
                },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger,
            )
        }
    }
}

package fr.shining_cat.simplehiit.mobile

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.shining_cat.simplehiit.android.mobile.ui.common.Screen
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeScreen
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionScreen
import fr.shining_cat.simplehiit.android.mobile.ui.settings.SettingsScreen
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsScreen
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun SimpleHiitNavigation(
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(route = Screen.Home.route) {
            HomeScreen(
                navigateTo = { navController.navigate(it) },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                navigateUp = { navController.navigateUp() },
                navigateTo = { navController.navigate(it) },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                navigateUp = { navController.navigateUp() },
                navigateTo = { navController.navigate(it) },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Session.route) {
            SessionScreen(
                navigateUp = { navController.navigateUp() },
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger
            )
        }
    }
}
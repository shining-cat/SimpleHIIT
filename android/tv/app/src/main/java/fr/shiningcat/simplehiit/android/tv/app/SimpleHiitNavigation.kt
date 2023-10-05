package fr.shiningcat.simplehiit.android.tv.app

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.tv.material3.ExperimentalTvMaterial3Api
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.tv.ui.home.HomeScreen
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionScreen
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsScreen
import fr.shiningcat.simplehiit.android.tv.ui.statistics.StatisticsScreen
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@OptIn(ExperimentalTvMaterial3Api::class)
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
                navigateTo = { navController.navigate(it) },
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Settings.route) {
            SettingsScreen(
                navigateTo = { navController.navigate(it) },
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Statistics.route) {
            StatisticsScreen(
                navigateTo = { navController.navigate(it) },
                hiitLogger = hiitLogger
            )
        }
        composable(route = Screen.Session.route) {
            SessionScreen(
                navigateUp = { navController.navigateUp() },
                hiitLogger = hiitLogger
            )
        }
    }
}

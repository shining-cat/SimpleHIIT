package fr.shining_cat.simplehiit.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import fr.shining_cat.simplehiit.ui.home.HomeScreen
import fr.shining_cat.simplehiit.ui.session.SessionScreen
import fr.shining_cat.simplehiit.ui.settings.SettingsScreen
import fr.shining_cat.simplehiit.ui.statistics.StatisticsScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { HomeScreen(modifier = Modifier.padding(16.dp)) }
        composable("main") { SessionScreen(modifier = Modifier.padding(16.dp)) }
        composable("main") { SettingsScreen(modifier = Modifier.padding(16.dp)) }
        composable("main") { StatisticsScreen(modifier = Modifier.padding(16.dp)) }
    }
}

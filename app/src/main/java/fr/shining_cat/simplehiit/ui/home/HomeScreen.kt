package fr.shining_cat.simplehiit.ui.home

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.logD("HomeScreen", "INIT")
    val viewState = viewModel.viewState.collectAsState().value
    //
    val view = LocalView.current
    val primaryAsInt = MaterialTheme.colorScheme.primary.toArgb()
    val darkMode = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            //applying primary color to Status bar
            val window = (view.context as Activity).window
            window.statusBarColor = primaryAsInt
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
        }
    }
    //
    Scaffold(
        topBar = {
            HomeTopBar(navController = navController)
        },
        content = { paddingValues ->
            HomeContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                viewState = viewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { navController.navigate(route = Screen.Settings.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cog),
                    contentDescription = stringResource(id = R.string.settings_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { navController.navigate(route = Screen.Statistics.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.bar_chart),
                    contentDescription = stringResource(id = R.string.statictics_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
fun HomeContent(
    modifier: Any,
    navController: NavController,
    viewState: HomeViewState
) {

}

// Previews


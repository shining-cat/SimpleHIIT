package fr.shining_cat.simplehiit.ui.settings

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.Screen
import fr.shining_cat.simplehiit.ui.session.SessionViewState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.logD("SettingsScreen", "INIT")
    val viewState = viewModel.viewState.collectAsState().value
    //
    Scaffold(
        topBar = {
            SettingsTopBar(navController = navController)
        },
        content = { paddingValues ->
            SettingsContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                viewState = viewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(navController: NavController) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { navController.navigateUp() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.settings_page_title),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun SettingsContent(
    modifier: Any,
    navController: NavController,
    viewState: SettingsViewState
) {

}
// Previews

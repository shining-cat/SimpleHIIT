package fr.shining_cat.simplehiit.ui.session

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

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: SessionViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.logD("SessionScreen", "INIT")
    val viewState = viewModel.viewState.collectAsState().value
    //
    SessionScreen(
        onNavigateUp = { navController.navigateUp() },
        viewState = viewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onNavigateUp: () -> Boolean = { false },
    viewState: SessionViewState
) {
    Scaffold(
        topBar = {
            SessionTopBar()
        },
        content = { paddingValues ->
            SessionContent(
                modifier = Modifier.padding(paddingValues),
                viewState = viewState,
                onNavigateUp = onNavigateUp
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTopBar() {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { /* TODO: open Pause dialog by triggering new state from viewmodel*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.session_work_page_title), // TODO: alternate "work" and "rest" title
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
private fun SessionContent(
    modifier: Any,
    viewState: SessionViewState,
    onNavigateUp: () -> Boolean = { false }
) {

}
// Previews


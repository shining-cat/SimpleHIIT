package fr.shining_cat.simplehiit.ui.statistics

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
import fr.shining_cat.simplehiit.domain.models.User

@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    viewModel.logD("StatisticsScreen", "INIT")
    val statisticsViewState = viewModel.statisticsViewState.collectAsState().value
    //
    StatisticsScreen(
        onNavigateUp = { navController.navigateUp() },
        statisticsViewState = statisticsViewState,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    onNavigateUp: () -> Boolean = { false },
    statisticsViewState: StatisticsViewState,
) {
    Scaffold(
        topBar = {
            StatisticsTopBar(
                onNavigateUp = onNavigateUp,
                statisticsViewState = statisticsViewState
            )
        },
        content = { paddingValues ->
            StatisticsContent(
                modifier = Modifier.padding(paddingValues),
                viewState = statisticsViewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsTopBar(
    onNavigateUp: () -> Boolean = { false },
    statisticsViewState: StatisticsViewState,
) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            val title = when (statisticsViewState) {
                is StatisticsViewState.StatisticsError, StatisticsViewState.StatisticsLoading -> stringResource(
                    R.string.statistics_page_title
                )
                is StatisticsViewState.StatisticsNominal -> stringResource(
                    R.string.statistics_page_title_with_user_name,
                    statisticsViewState.userName
                )
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: open user picking dialog by triggering new viewstate in viewmodel*/ }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.people),
                    contentDescription = stringResource(id = R.string.user_pick_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
private fun StatisticsContent(
    modifier: Any,
    viewState: StatisticsViewState
) {

}
// Previews

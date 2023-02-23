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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.logD("StatisticsScreen", "INIT")
    val statisticsViewState = viewModel.statisticsViewState.collectAsState().value
    val allUsersViewState = viewModel.allUsersViewState.collectAsState().value
    //
    Scaffold(
        topBar = {
            StatisticsTopBar(
                navController = navController,
                statisticsViewState = statisticsViewState
            )
        },
        content = { paddingValues ->
            StatisticsContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                viewState = statisticsViewState
            )
        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StatisticsTopBar(navController: NavController, statisticsViewState: StatisticsViewState, ) {
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
            val title = when (statisticsViewState) {
                is StatisticsViewState.StatisticsError, StatisticsViewState.StatisticsLoading -> stringResource(R.string.statistics_page_title)
                is StatisticsViewState.StatisticsNominal -> stringResource(R.string.statistics_page_title_with_user_name, statisticsViewState.userName)
            }
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { /* TODO: open user picking dialog */ }) {
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
fun StatisticsContent(
    modifier: Any,
    navController: NavController,
    viewState: StatisticsViewState
) {

}
// Previews

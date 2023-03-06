package fr.shining_cat.simplehiit.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.ui.components.ConfirmDialog

@Composable
fun StatisticsScreen(
    navController: NavController,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    viewModel.logD("StatisticsScreen", "INIT")
    val screenViewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    StatisticsScreen(
        onNavigateUp = { navController.navigateUp() },
        openUserPicker = {viewModel.openPickUser()},
        selectUser = {viewModel.retrieveStatsForUser(it)},
        deleteAllSessionsForUser = {viewModel.deleteAllSessionsForUser(it)},
        deleteAllSessionsForUserConfirm = {viewModel.deleteAllSessionsForUserConfirmation(it)},
        cancelDialog = {viewModel.cancelDialog()},
        resetWholeApp = {viewModel.resetWholeApp()},
        resetWholeAppConfirmation = {viewModel.resetWholeAppConfirmationDeleteEverything()},
        screenViewState = screenViewState,
        dialogViewState = dialogViewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    onNavigateUp: () -> Boolean = { false },
    openUserPicker: (User) -> Unit,
    selectUser: (User) -> Unit,
    deleteAllSessionsForUser: (User) -> Unit,
    deleteAllSessionsForUserConfirm: (User) -> Unit,
    cancelDialog: () -> Unit,
    resetWholeApp: () -> Unit,
    resetWholeAppConfirmation: () -> Unit,
    screenViewState: StatisticsViewState,
    dialogViewState: StatisticsDialog
) {
    Scaffold(
        topBar = {
            StatisticsTopBar(
                onNavigateUp = onNavigateUp,
                openUserPicker = openUserPicker,
                screenViewState = screenViewState
            )
        },
        content = { paddingValues ->
            StatisticsContent(
                innerPadding = paddingValues,
                selectUser = selectUser,
                deleteAllSessionsForUser = deleteAllSessionsForUser,
                deleteAllSessionsForUserConfirm = deleteAllSessionsForUserConfirm,
                cancelDialog = cancelDialog,
                resetWholeApp = resetWholeApp,
                resetWholeAppConfirmation = resetWholeAppConfirmation,
                screenViewState = screenViewState,
                dialogViewState = dialogViewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsTopBar(
    onNavigateUp: () -> Boolean = { false },
    openUserPicker: (User) -> Unit,
    screenViewState: StatisticsViewState,
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
            val title = when (screenViewState) {
                is StatisticsViewState.StatisticsNominal -> stringResource(
                    R.string.statistics_page_title_with_user_name,
                    screenViewState.user.name
                )
                else -> stringResource(
                    R.string.statistics_page_title
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
            IconButton(onClick = { openUserPicker }) {
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
    innerPadding: PaddingValues,
    selectUser: (User) -> Unit,
    deleteAllSessionsForUser: (User) -> Unit,
    deleteAllSessionsForUserConfirm: (User) -> Unit,
    cancelDialog: () -> Unit,
    resetWholeApp: () -> Unit,
    resetWholeAppConfirmation: () -> Unit,
    screenViewState: StatisticsViewState,
    dialogViewState: StatisticsDialog
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(screenViewState){
            StatisticsViewState.StatisticsLoading -> CircularProgressIndicator()
            is StatisticsViewState.StatisticsNominal -> {}
            StatisticsViewState.StatisticsNoUsers -> {}
            is StatisticsViewState.StatisticsError -> StatisticsContentErrorState(
                errorCode = screenViewState.errorCode,
                deleteSessionsForUser = { deleteAllSessionsForUser(screenViewState.user) }
            )
            is StatisticsViewState.StatisticsFatalError -> StatisticsContentBrokenState(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp
            )
        }
        when(dialogViewState){
            StatisticsDialog.None -> {/*Do nothing*/}
            is StatisticsDialog.SelectUserDialog -> {}
            is StatisticsDialog.SettingsDialogConfirmDeleteAllSessionsForUser -> ConfirmDialog(
                message = stringResource(id = R.string.reset_statistics_confirmation_button_label, dialogViewState.user.name),
                buttonConfirmLabel = stringResource(id = R.string.delete_button_label),
                onConfirm = { deleteAllSessionsForUserConfirm(dialogViewState.user) },
                dismissAction = cancelDialog
            )
            StatisticsDialog.StatisticsDialogConfirmWholeReset -> ConfirmDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                buttonConfirmLabel = stringResource(id = R.string.delete_button_label),
                onConfirm = resetWholeAppConfirmation,
                dismissAction = cancelDialog
            )
        }
    }
}

@Composable
private fun StatisticField(
    label: String,
    value: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.headlineSmall)
        Text(text = value, style = MaterialTheme.typography.headlineSmall)
    }
}

// Previews

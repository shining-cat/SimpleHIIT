package fr.shining_cat.simplehiit.android.mobile.ui.statistics

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.android.mobile.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commondomain.models.DisplayStatisticType
import fr.shining_cat.simplehiit.commondomain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents.StatisticsErrorContent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents.StatisticsFatalErrorContent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents.StatisticsNoSessionsContent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents.StatisticsNoUsersContent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents.StatisticsNominalContent
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.dialogs.StatisticsSelectUserDialog
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun StatisticsScreen(
    navController: NavController,
    hiitLogger: HiitLogger,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
    hiitLogger.d("StatisticsScreen", "INIT")
    //
    val durationsFormatter = DurationStringFormatter(
        hoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        hoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        hoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        minutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        minutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        seconds = stringResource(id = R.string.seconds_short)
    )
    viewModel.init(durationsFormatter)
    //
    val screenViewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    StatisticsScreen(
        onNavigateUp = { navController.navigateUp() },
        openUserPicker = { viewModel.openPickUser() },
        selectUser = { viewModel.retrieveStatsForUser(it) },
        deleteAllSessionsForUser = { viewModel.deleteAllSessionsForUser(it) },
        deleteAllSessionsForUserConfirm = { viewModel.deleteAllSessionsForUserConfirmation(it) },
        cancelDialog = { viewModel.cancelDialog() },
        resetWholeApp = { viewModel.resetWholeApp() },
        resetWholeAppConfirmation = { viewModel.resetWholeAppConfirmationDeleteEverything() },
        screenViewState = screenViewState,
        dialogViewState = dialogViewState,
        hiitLogger = hiitLogger
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsScreen(
    onNavigateUp: () -> Boolean = { false },
    openUserPicker: () -> Unit,
    selectUser: (User) -> Unit,
    deleteAllSessionsForUser: (User) -> Unit,
    deleteAllSessionsForUserConfirm: (User) -> Unit,
    cancelDialog: () -> Unit,
    resetWholeApp: () -> Unit,
    resetWholeAppConfirmation: () -> Unit,
    screenViewState: StatisticsViewState,
    dialogViewState: StatisticsDialog,
    hiitLogger: HiitLogger? = null
) {
    Scaffold(
        topBar = {
            StatisticsTopBar(
                onNavigateUp = onNavigateUp,
                openUserPicker = openUserPicker,
                screenViewState = screenViewState,
                hiitLogger = hiitLogger
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
                dialogViewState = dialogViewState,
                hiitLogger = hiitLogger
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun StatisticsTopBar(
    onNavigateUp: () -> Boolean = { false },
    openUserPicker: () -> Unit,
    screenViewState: StatisticsViewState,
    hiitLogger: HiitLogger?
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
            val title = stringResource(R.string.statistics_page_title)
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            val shouldShowUserPickButton =
                screenViewState is StatisticsViewState.Error ||
                        screenViewState is StatisticsViewState.Nominal ||
                        screenViewState is StatisticsViewState.NoSessions
            if (shouldShowUserPickButton) {
                IconButton(onClick = openUserPicker) {
                    Icon(
                        imageVector = ImageVector.vectorResource(R.drawable.people),
                        contentDescription = stringResource(id = R.string.user_pick_button_content_label),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
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
    dialogViewState: StatisticsDialog,
    hiitLogger: HiitLogger?
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (screenViewState) {
            StatisticsViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is StatisticsViewState.Nominal -> {
                StatisticsNominalContent(
                    deleteAllSessionsForUser = deleteAllSessionsForUser,
                    viewState = screenViewState,
                    hiitLogger = hiitLogger
                )
            }
            is StatisticsViewState.NoSessions -> StatisticsNoSessionsContent(
                screenViewState
            )
            StatisticsViewState.NoUsers -> StatisticsNoUsersContent()
            is StatisticsViewState.Error -> StatisticsErrorContent(
                user = screenViewState.user,
                errorCode = screenViewState.errorCode,
                deleteSessionsForUser = { deleteAllSessionsForUser(screenViewState.user) }
            )
            is StatisticsViewState.FatalError -> StatisticsFatalErrorContent(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp
            )
        }
        when (dialogViewState) {
            StatisticsDialog.None -> {}/*Do nothing*/
            is StatisticsDialog.SelectUser -> StatisticsSelectUserDialog(
                users = dialogViewState.users,
                selectUser = {
                    cancelDialog()
                    selectUser(it)
                },
                dismissAction = cancelDialog
            )
            is StatisticsDialog.ConfirmDeleteAllSessionsForUser -> WarningDialog(
                message = stringResource(
                    id = R.string.reset_statistics_confirmation_button_label,
                    dialogViewState.user.name
                ),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = { deleteAllSessionsForUserConfirm(dialogViewState.user) },
                dismissAction = cancelDialog
            )
            StatisticsDialog.ConfirmWholeReset -> WarningDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = resetWholeAppConfirmation,
                dismissAction = cancelDialog
            )
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsScreenPreview(
    @PreviewParameter(StatisticsScreenPreviewParameterProvider::class) viewStates: Pair<StatisticsViewState, StatisticsDialog>
) {
    SimpleHiitTheme {
        StatisticsScreen(
            onNavigateUp = { true },
            openUserPicker = {},
            selectUser = {},
            deleteAllSessionsForUser = {},
            deleteAllSessionsForUserConfirm = {},
            cancelDialog = {},
            resetWholeApp = {},
            resetWholeAppConfirmation = {},
            screenViewState = viewStates.first,
            dialogViewState = viewStates.second
        )
    }
}

internal class StatisticsScreenPreviewParameterProvider :
    PreviewParameterProvider<Pair<StatisticsViewState, StatisticsDialog>> {
    override val values: Sequence<Pair<StatisticsViewState, StatisticsDialog>>
        get() = sequenceOf(
            Pair(StatisticsViewState.Loading, StatisticsDialog.None),
            Pair(StatisticsViewState.NoUsers, StatisticsDialog.None),
            Pair(
                StatisticsViewState.Error(
                    errorCode = "Error code",
                    user = User(name = "Sven Svensson")
                ), StatisticsDialog.None
            ),
            Pair(
                StatisticsViewState.FatalError(errorCode = "Error code"),
                StatisticsDialog.None
            ),
            Pair(
                StatisticsViewState.Nominal(
                    user = User(name = "Sven Svensson"),
                    listOf(
                        DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                        DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                        DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                        DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                        DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                        DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                    )
                ), StatisticsDialog.None
            ),
            Pair(
                StatisticsViewState.NoSessions(user = User(name = "Sven Svensson")),
                StatisticsDialog.None
            )
        )
}
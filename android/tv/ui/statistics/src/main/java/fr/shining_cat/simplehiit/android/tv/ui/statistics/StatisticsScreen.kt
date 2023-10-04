package fr.shining_cat.simplehiit.android.tv.ui.statistics

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.tv.ui.common.components.NavigationSideBar
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.statistics.contents.StatisticsContentHolder
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shining_cat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun StatisticsScreen(
    navigateTo: (String) -> Unit = {},
    hiitLogger: HiitLogger,
    viewModel: StatisticsViewModel = hiltViewModel()
) {
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
        navigateTo = navigateTo,
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

@Composable
private fun StatisticsScreen(
    navigateTo: (String) -> Unit = {},
    openUserPicker: () -> Unit = {},
    selectUser: (User) -> Unit = {},
    deleteAllSessionsForUser: (User) -> Unit = {},
    deleteAllSessionsForUserConfirm: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    resetWholeApp: () -> Unit = {},
    resetWholeAppConfirmation: () -> Unit = {},
    screenViewState: StatisticsViewState,
    dialogViewState: StatisticsDialog,
    hiitLogger: HiitLogger? = null
) {
    Row(modifier = Modifier.fillMaxSize()) {
        NavigationSideBar(
            navigateTo = navigateTo,
            currentDestination = fr.shining_cat.simplehiit.android.common.Screen.Statistics,
            showStatisticsButton = true // in this case, we are in the statistics screen, so obviously we want to show this button
        )

        StatisticsContentHolder(
            openUserPicker = openUserPicker,
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
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsScreenPreview(
    @PreviewParameter(StatisticsScreenPreviewParameterProvider::class) viewState: StatisticsViewState
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsScreen(
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None
            )
        }
    }
}

internal class StatisticsScreenPreviewParameterProvider :
    PreviewParameterProvider<StatisticsViewState> {
    override val values: Sequence<StatisticsViewState>
        get() = sequenceOf(
            StatisticsViewState.Loading,
            StatisticsViewState.NoUsers,
            StatisticsViewState.Error(
                errorCode = "Error code",
                user = User(name = "Sven Svensson"),
                showUsersSwitch = true
            ),
            StatisticsViewState.Error(
                errorCode = "Error code",
                user = User(name = "Sven Svensson"),
                showUsersSwitch = false
            ),
            StatisticsViewState.FatalError(errorCode = "Error code"),
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                statistics = listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                ),
                showUsersSwitch = true
            ),
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                statistics = listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                ),
                showUsersSwitch = false
            ),
            StatisticsViewState.NoSessions(
                user = User(name = "Sven Svensson"),
                showUsersSwitch = true
            ),
            StatisticsViewState.NoSessions(
                user = User(name = "Sven Svensson"),
                showUsersSwitch = false
            )
        )
}
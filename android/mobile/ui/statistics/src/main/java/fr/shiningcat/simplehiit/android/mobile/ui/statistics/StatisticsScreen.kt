package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.mainContentInsets
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsTopAppBar
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents.StatisticsContentHolder
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsDialog
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewModel
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun StatisticsScreen(
    navigateTo: (Screen) -> Unit = {},
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    viewModel: StatisticsViewModel = koinViewModel(),
) {
    val screenViewState = viewModel.screenViewState.collectAsStateWithLifecycle().value
    val dialogViewState = viewModel.dialogViewState.collectAsStateWithLifecycle().value
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
        uiArrangement = uiArrangement,
        screenViewState = screenViewState,
        dialogViewState = dialogViewState,
        hiitLogger = hiitLogger,
    )
}

@Composable
private fun StatisticsScreen(
    navigateTo: (Screen) -> Unit = {},
    openUserPicker: () -> Unit = {},
    selectUser: (User) -> Unit = {},
    deleteAllSessionsForUser: (User) -> Unit = {},
    deleteAllSessionsForUserConfirm: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    resetWholeApp: () -> Unit = {},
    resetWholeAppConfirmation: () -> Unit = {},
    uiArrangement: UiArrangement,
    screenViewState: StatisticsViewState,
    dialogViewState: StatisticsDialog,
    hiitLogger: HiitLogger? = null,
) {
    Row(
        modifier = Modifier.fillMaxSize(),
    ) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            // in this case, we are in the statistics screen, so obviously we want to show this button
            NavigationSideBar(
                navigateTo = navigateTo,
                currentDestination = Screen.Statistics,
                showStatisticsButton = true,
            )
        }
        Column(
            modifier =
                Modifier
                    .mainContentInsets(uiArrangement)
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                val showSwitch =
                    when (screenViewState) {
                        is StatisticsViewState.Error -> screenViewState.showUsersSwitch
                        is StatisticsViewState.NoSessions -> screenViewState.showUsersSwitch
                        is StatisticsViewState.Nominal -> screenViewState.showUsersSwitch
                        else -> false
                    }
                StatisticsTopAppBar(
                    // forcing nav to home instead of up to avoid popping the backstack(which is possible after orientation change)
                    navigateUp = {
                        navigateTo(Screen.Home)
                        true
                    },
                    showUsersSwitch = showSwitch,
                    openUserPicker = openUserPicker,
                )
            }
            StatisticsContentHolder(
                openUserPicker = openUserPicker,
                selectUser = selectUser,
                deleteAllSessionsForUser = deleteAllSessionsForUser,
                deleteAllSessionsForUserConfirm = deleteAllSessionsForUserConfirm,
                cancelDialog = cancelDialog,
                resetWholeApp = resetWholeApp,
                resetWholeAppConfirmation = resetWholeAppConfirmation,
                uiArrangement = uiArrangement,
                screenViewState = screenViewState,
                dialogViewState = dialogViewState,
                hiitLogger = hiitLogger,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun StatisticsScreenPreview(
    @PreviewParameter(StatisticsScreenPreviewParameterProvider::class) viewState: StatisticsViewState,
) {
    val previewUiArrangement = currentUiArrangement()
    SimpleHiitMobileTheme {
        Surface {
            StatisticsScreen(
                openUserPicker = {},
                uiArrangement = previewUiArrangement,
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None,
            )
        }
    }
}

internal class StatisticsScreenPreviewParameterProvider : PreviewParameterProvider<StatisticsViewState> {
    override val values: Sequence<StatisticsViewState>
        get() =
            sequenceOf(
                StatisticsViewState.Loading,
                StatisticsViewState.NoUsers,
                StatisticsViewState.Error(
                    errorCode = "Error code",
                    user = User(name = "Sven Svensson"),
                    showUsersSwitch = true,
                ),
                StatisticsViewState.Error(
                    errorCode = "Error code",
                    user = User(name = "Alice"),
                    showUsersSwitch = false,
                ),
                StatisticsViewState.FatalError(errorCode = "Error code"),
                StatisticsViewState.Nominal(
                    user = User(name = "Sven Svensson"),
                    statistics =
                        listOf(
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                        ),
                    showUsersSwitch = true,
                ),
                StatisticsViewState.Nominal(
                    user = User(name = "Bob"),
                    statistics =
                        listOf(
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                        ),
                    showUsersSwitch = false,
                ),
                StatisticsViewState.NoSessions(
                    user = User(name = "Sven Svensson"),
                    showUsersSwitch = true,
                ),
                StatisticsViewState.NoSessions(
                    user = User(name = "Sven"),
                    showUsersSwitch = false,
                ),
            )
}

package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsTopAppBar
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents.StatisticsContentHolder
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun StatisticsScreen(
    navigateTo: (String) -> Unit = {},
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    viewModel: StatisticsViewModel = hiltViewModel(),
) {
    //
    viewModel.init()
    //
    val screenViewState = viewModel.screenViewState.collectAsStateWithLifecycle().value
    val dialogViewState = viewModel.dialogViewState.collectAsStateWithLifecycle().value
    //
    StatisticsScreen(
        navigateTo = navigateTo,
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
    navigateTo: (String) -> Unit = {},
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
    Row(modifier = Modifier.fillMaxSize()) {
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
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                val allUsers: List<User>? =
                    when (screenViewState) {
                        is StatisticsViewState.Error -> screenViewState.allUsers
                        is StatisticsViewState.NoSessions -> screenViewState.allUsers
                        is StatisticsViewState.Nominal -> screenViewState.allUsers
                        StatisticsViewState.Loading, StatisticsViewState.NoUsers, is StatisticsViewState.FatalError -> null
                    }

                StatisticsTopAppBar(
                    // forcing nav to home instead of up to avoid popping the backstack(which is possible after orientation change)
                    navigateUp = {
                        navigateTo(Screen.Home.route)
                        true
                    },
                    allUsers = allUsers,
                    onUserSelected = selectUser,
                )
            }
            StatisticsContentHolder(
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
                modifier =
                    Modifier
                        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
                        .then(
                            if (uiArrangement == UiArrangement.HORIZONTAL) {
                                Modifier.windowInsetsPadding(WindowInsets.statusBars)
                            } else {
                                Modifier
                            },
                        ),
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
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            StatisticsScreen(
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
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    errorCode = "Error code",
                    selectedUser = User(name = "Sven Svensson"),
                ),
                StatisticsViewState.Error(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    errorCode = "Error code",
                    selectedUser = User(name = "Sven Svensson"),
                ),
                StatisticsViewState.FatalError(errorCode = "Error code"),
                StatisticsViewState.Nominal(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    selectedUserStatistics =
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
                ),
                StatisticsViewState.Nominal(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    selectedUserStatistics =
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
                ),
                StatisticsViewState.NoSessions(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                ),
                StatisticsViewState.NoSessions(
                    allUsers =
                        listOf(
                            User(name = "Sven"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                ),
            )
}

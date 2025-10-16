package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsDialog
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun StatisticsContentHolder(
    modifier: Modifier = Modifier,
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
    Box(modifier = modifier) {
        when (screenViewState) {
            StatisticsViewState.Loading -> BasicLoading(modifier = Modifier.fillMaxSize())

            is StatisticsViewState.Nominal -> {
                StatisticsNominalContent(
                    deleteAllSessionsForUser = deleteAllSessionsForUser,
                    nominalViewState = screenViewState,
                    uiArrangement = uiArrangement,
                    onUserSelected = selectUser,
                    hiitLogger = hiitLogger,
                    modifier = Modifier.fillMaxSize(),
                )
            }

            is StatisticsViewState.NoSessions ->
                StatisticsNoSessionsContent(
                    noSessionsViewState = screenViewState,
                    uiArrangement = uiArrangement,
                    onUserSelected = selectUser,
                    modifier = Modifier.fillMaxSize(),
                )

            StatisticsViewState.NoUsers -> StatisticsNoUsersContent(modifier = Modifier.fillMaxSize())
            is StatisticsViewState.Error ->
                StatisticsErrorContent(
                    errorViewState = screenViewState,
                    deleteSessionsForUser = { deleteAllSessionsForUser(screenViewState.selectedUser) },
                    uiArrangement = uiArrangement,
                    onUserSelected = selectUser,
                    modifier = Modifier.fillMaxSize(),
                )

            is StatisticsViewState.FatalError ->
                StatisticsFatalErrorContent(
                    errorCode = screenViewState.errorCode,
                    resetWholeApp = resetWholeApp,
                    modifier = Modifier.fillMaxSize(),
                )
        }
    }
    when (dialogViewState) {
        StatisticsDialog.None -> {} // Do nothing

        is StatisticsDialog.ConfirmDeleteAllSessionsForUser ->
            WarningDialog(
                message =
                    stringResource(
                        id = R.string.reset_statistics_confirmation_button_label,
                        dialogViewState.user.name,
                    ),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = { deleteAllSessionsForUserConfirm(dialogViewState.user) },
                dismissAction = cancelDialog,
            )

        StatisticsDialog.ConfirmWholeReset ->
            WarningDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = resetWholeAppConfirmation,
                dismissAction = cancelDialog,
            )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsContentHolderPreview(
    @PreviewParameter(StatisticsContentHolderPreviewParameterProvider::class) viewState: StatisticsViewState,
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
            StatisticsContentHolder(
                uiArrangement = previewUiArrangement,
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None,
            )
        }
    }
}

internal class StatisticsContentHolderPreviewParameterProvider : PreviewParameterProvider<StatisticsViewState> {
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

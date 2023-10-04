package fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsDialog
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.dialogs.StatisticsSelectUserDialog
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shining_cat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun StatisticsContentHolder(
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
    hiitLogger: HiitLogger? = null
) {
    when (screenViewState) {
        StatisticsViewState.Loading -> BasicLoading()

        is StatisticsViewState.Nominal -> {
            StatisticsNominalContent(
                openUserPicker = openUserPicker,
                deleteAllSessionsForUser = deleteAllSessionsForUser,
                viewState = screenViewState,
                uiArrangement = uiArrangement,
                hiitLogger = hiitLogger
            )
        }

        is StatisticsViewState.NoSessions -> StatisticsNoSessionsContent(
            userName = screenViewState.user.name,
            showUsersSwitch = screenViewState.showUsersSwitch,
            openUserPicker = openUserPicker
        )

        StatisticsViewState.NoUsers -> StatisticsNoUsersContent()
        is StatisticsViewState.Error -> StatisticsErrorContent(
            userName = screenViewState.user.name,
            errorCode = screenViewState.errorCode,
            deleteSessionsForUser = { deleteAllSessionsForUser(screenViewState.user) },
            showUsersSwitch = screenViewState.showUsersSwitch,
            openUserPicker = openUserPicker
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

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun StatisticsContentHolderPreviewPhonePortrait(
    @PreviewParameter(StatisticsContentHolderPreviewParameterProvider::class) viewState: StatisticsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsContentHolder(
                uiArrangement = UiArrangement.VERTICAL,
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsContentHolderPreviewTabletLandscape(
    @PreviewParameter(StatisticsContentHolderPreviewParameterProvider::class) viewState: StatisticsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsContentHolder(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400
)
@Composable
private fun StatisticsContentHolderPreviewPhoneLandscape(
    @PreviewParameter(StatisticsContentHolderPreviewParameterProvider::class) viewState: StatisticsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsContentHolder(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = StatisticsDialog.None
            )
        }
    }
}

internal class StatisticsContentHolderPreviewParameterProvider :
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
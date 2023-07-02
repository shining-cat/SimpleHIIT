package fr.shining_cat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsDialog
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shining_cat.simplehiit.android.mobile.ui.statistics.dialogs.StatisticsSelectUserDialog
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun StatisticsContentHolder(
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
    when (screenViewState) {
        StatisticsViewState.Loading -> BasicLoading()

        is StatisticsViewState.Nominal -> {
            StatisticsNominalContent(
                openUserPicker = openUserPicker,
                deleteAllSessionsForUser = deleteAllSessionsForUser,
                viewState = screenViewState,
                hiitLogger = hiitLogger
            )
        }

        is StatisticsViewState.NoSessions -> StatisticsNoSessionsContent(
            openUserPicker = openUserPicker,
            viewState = screenViewState
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
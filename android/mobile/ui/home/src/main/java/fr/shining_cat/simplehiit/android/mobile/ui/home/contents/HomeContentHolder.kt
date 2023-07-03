package fr.shining_cat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.Screen
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeDialog
import fr.shining_cat.simplehiit.android.mobile.ui.home.HomeViewState
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun HomeContentHolder(
    navigateTo: (String) -> Unit = {},
    resetWholeApp: () -> Unit = {},
    resetWholeAppDeleteEverything: () -> Unit = {},
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    uiArrangement: UiArrangement,
    screenViewState: HomeViewState,
    dialogViewState: HomeDialog
) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
            text = stringResource(id = R.string.hiit_description)
        )
        when (screenViewState) {
            is HomeViewState.Loading -> BasicLoading()

            is HomeViewState.Error -> HomeErrorContent(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp
            )

            is HomeViewState.MissingUsers -> HomeMissingUsersContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength,
                totalLengthFormatted = screenViewState.totalSessionLengthFormatted,
                navigateToSettings = { navigateTo(Screen.Settings.route) }
            )

            is HomeViewState.Nominal -> HomeNominalContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength,
                totalLengthFormatted = screenViewState.totalSessionLengthFormatted,
                users = screenViewState.users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = { navigateTo(Screen.Session.route) }
            )
        }
        when (dialogViewState) {
            is HomeDialog.ConfirmWholeReset -> WarningDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = resetWholeAppDeleteEverything,
                dismissAction = cancelDialog
            )

            HomeDialog.None -> {}/*do nothing*/
        }
    }
}
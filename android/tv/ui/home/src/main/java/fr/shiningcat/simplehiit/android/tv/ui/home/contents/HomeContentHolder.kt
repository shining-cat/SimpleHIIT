package fr.shiningcat.simplehiit.android.tv.ui.home.contents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.tv.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.tv.ui.common.components.DialogWarning
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.home.HomeDialog
import fr.shiningcat.simplehiit.sharedui.home.HomeViewState

@Composable
fun HomeContentHolder(
    navigateTo: (Screen) -> Unit = {},
    resetWholeApp: () -> Unit = {},
    resetWholeAppDeleteEverything: () -> Unit = {},
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    screenViewState: HomeViewState,
    dialogViewState: HomeDialog,
    hiitLogger: HiitLogger? = null,
) {
    when (screenViewState) {
        is HomeViewState.Loading -> {
            BasicLoading()
        }
        is HomeViewState.Error -> {
            HomeErrorContent(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp,
            )
        }
        is HomeViewState.MissingUsers -> {
            HomeMissingUsersContent(
                navigateToSettings = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Settings) },
            )
        }
        is HomeViewState.Nominal -> {
            HomeNominalContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength,
                totalLengthFormatted = screenViewState.totalSessionLengthFormatted,
                users = screenViewState.users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Session) },
                warning = screenViewState.warning,
                hiitLogger = hiitLogger,
            )
        }
    }
    when (dialogViewState) {
        is HomeDialog.ConfirmWholeReset -> {
            DialogWarning(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = resetWholeAppDeleteEverything,
                dismissAction = cancelDialog,
            )
        }
        HomeDialog.None -> {} // do nothing
    }
}

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
@Composable
private fun HomeContentHolderPreviewTV(
    @PreviewParameter(HomeContentHolderPreviewParameterProvider::class) viewState: HomeViewState,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            HomeContentHolder(
                screenViewState = viewState,
                dialogViewState = HomeDialog.None,
            )
        }
    }
}

internal class HomeContentHolderPreviewParameterProvider : PreviewParameterProvider<HomeViewState> {
    override val values: Sequence<HomeViewState>
        get() =
            sequenceOf(
                HomeViewState.Loading,
                HomeViewState.Error(errorCode = "12345"),
                HomeViewState.MissingUsers(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    totalSessionLengthFormatted = "total time: 20mn",
                ),
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users =
                        listOf(
                            User(123L, "User 1", selected = true),
                            User(234L, "User 2", selected = false),
                            User(345L, "User 3", selected = true),
                        ),
                    totalSessionLengthFormatted = "total time: 20mn",
                ),
                HomeViewState.Nominal(
                    numberCumulatedCycles = 5,
                    cycleLength = "4mn",
                    users =
                        listOf(
                            User(123L, "User 1", selected = true),
                            User(234L, "User 2", selected = false),
                            User(345L, "User 3", selected = true),
                        ),
                    totalSessionLengthFormatted = "total time: 20mn",
                    warning = LaunchSessionWarning.SKIPPED_EXERCISE_TYPES,
                ),
            )
}

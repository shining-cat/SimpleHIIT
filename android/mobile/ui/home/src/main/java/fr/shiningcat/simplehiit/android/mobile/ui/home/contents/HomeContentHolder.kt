package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeDialog
import fr.shiningcat.simplehiit.android.mobile.ui.home.HomeViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User

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
    dialogViewState: HomeDialog,
    hiitLogger: HiitLogger? = null,
) {
    when (screenViewState) {
        is HomeViewState.Loading -> BasicLoading()

        is HomeViewState.Error ->
            HomeErrorContent(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp,
            )

        is HomeViewState.MissingUsers ->
            HomeMissingUsersContent(
                navigateToSettings = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Settings.route) },
            )

        is HomeViewState.Nominal ->
            HomeNominalContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength,
                totalLengthFormatted = screenViewState.totalSessionLengthFormatted,
                uiArrangement = uiArrangement,
                users = screenViewState.users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = { navigateTo(fr.shiningcat.simplehiit.android.common.Screen.Session.route) },
                hiitLogger = hiitLogger,
            )
    }
    when (dialogViewState) {
        is HomeDialog.ConfirmWholeReset ->
            WarningDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = resetWholeAppDeleteEverything,
                dismissAction = cancelDialog,
            )

        HomeDialog.None -> {} // do nothing
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400,
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400,
)
@Composable
private fun HomeContentHolderPreviewPhonePortrait(
    @PreviewParameter(HomeContentHolderPreviewParameterProvider::class) viewState: HomeViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeContentHolder(
                uiArrangement = UiArrangement.VERTICAL,
                screenViewState = viewState,
                dialogViewState = HomeDialog.None,
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = "spec:width=1280dp,height=800dp,dpi=240",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun HomeContentHolderPreviewTabletLandscape(
    @PreviewParameter(HomeContentHolderPreviewParameterProvider::class) viewState: HomeViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeContentHolder(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = HomeDialog.None,
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400,
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400,
)
@Composable
private fun HomeContentHolderPreviewPhoneLandscape(
    @PreviewParameter(HomeContentHolderPreviewParameterProvider::class) viewState: HomeViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeContentHolder(
                uiArrangement = UiArrangement.HORIZONTAL,
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
            )
}

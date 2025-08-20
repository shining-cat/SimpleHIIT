package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.layout.Box // Added import
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier // Added import
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.Screen
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
    modifier: Modifier = Modifier,
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
    Box(modifier = modifier) {
        when (screenViewState) {
            is HomeViewState.Loading -> BasicLoading(modifier = Modifier.fillMaxSize())

            is HomeViewState.Error ->
                HomeErrorContent(
                    errorCode = screenViewState.errorCode,
                    resetWholeApp = resetWholeApp,
                )

            is HomeViewState.MissingUsers ->
                HomeMissingUsersContent(
                    navigateToSettings = { navigateTo(Screen.Settings.route) },
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
                    navigateToSession = { navigateTo(Screen.Session.route) },
                    hiitLogger = hiitLogger,
                )
        }
    }
    // Dialogs are typically drawn on top of everything, so they don't need the main content modifier
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

// Previews ( остальное без изменений )
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeContentHolderPreviewPhoneLandscape(
    @PreviewParameter(HomeContentHolderPreviewParameterProvider::class) viewState: HomeViewState,
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
            HomeContentHolder(
                uiArrangement = previewUiArrangement,
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

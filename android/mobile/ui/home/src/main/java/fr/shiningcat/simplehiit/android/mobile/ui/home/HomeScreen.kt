package fr.shiningcat.simplehiit.android.mobile.ui.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.mainContentInsets
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.HomeTopBarComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.contents.HomeContentHolder
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit,
    uiArrangement: UiArrangement,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger,
    viewModel: HomeViewModel = hiltViewModel(),
) {
    val viewState = viewModel.screenViewState.collectAsStateWithLifecycle().value
    val dialogViewState = viewModel.dialogViewState.collectAsStateWithLifecycle().value
    //
    HomeScreen(
        navigateTo = navigateTo,
        onResetWholeApp = { viewModel.resetWholeApp() },
        onResetWholeAppDeleteEverything = { viewModel.resetWholeAppConfirmationDeleteEverything() },
        decreaseNumberOfCycles = { viewModel.modifyNumberCycles(HomeViewModel.NumberCycleModification.DECREASE) },
        increaseNumberOfCycles = { viewModel.modifyNumberCycles(HomeViewModel.NumberCycleModification.INCREASE) },
        toggleSelectedUser = { viewModel.toggleSelectedUser(it) },
        cancelDialog = { viewModel.cancelDialog() },
        uiArrangement = uiArrangement,
        viewState = viewState,
        dialogViewState = dialogViewState,
        hiitLogger = hiitLogger,
    )
}

@Composable
private fun HomeScreen(
    navigateTo: (fr.shiningcat.simplehiit.android.common.Screen) -> Unit = {},
    onResetWholeApp: () -> Unit = {},
    onResetWholeAppDeleteEverything: () -> Unit = {},
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    uiArrangement: UiArrangement,
    viewState: HomeViewState,
    dialogViewState: HomeDialog,
    hiitLogger: HiitLogger? = null,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            NavigationSideBar(
                // This component handles its own insets
                navigateTo = navigateTo,
                currentDestination = fr.shiningcat.simplehiit.android.common.Screen.Home,
                showStatisticsButton = viewState is HomeViewState.Nominal,
            )
        }
        Column(
            modifier =
                Modifier
                    .mainContentInsets(uiArrangement)
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                HomeTopBarComponent(
                    // This component handles its own insets
                    navigateTo = navigateTo,
                    screenViewState = viewState,
                )
            }
            HomeContentHolder(
                navigateTo = navigateTo,
                resetWholeApp = onResetWholeApp,
                resetWholeAppDeleteEverything = onResetWholeAppDeleteEverything,
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                toggleSelectedUser = toggleSelectedUser,
                cancelDialog = cancelDialog,
                uiArrangement = uiArrangement,
                screenViewState = viewState,
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
private fun HomeScreenPreviewPhone(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) viewState: HomeViewState,
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
            HomeScreen(
                uiArrangement = previewUiArrangement,
                viewState = viewState,
                dialogViewState = HomeDialog.None,
            )
        }
    }
}

internal class HomeScreenPreviewParameterProvider : PreviewParameterProvider<HomeViewState> {
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
                    users = listOf(User(123L, "User 1", selected = true)),
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

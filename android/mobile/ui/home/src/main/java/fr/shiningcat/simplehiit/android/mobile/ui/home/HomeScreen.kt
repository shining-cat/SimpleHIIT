package fr.shiningcat.simplehiit.android.mobile.ui.home

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
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.mainContentInsets
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.HomeTopBarComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.contents.HomeContentHolder
import fr.shiningcat.simplehiit.android.shared.core.Screen
import fr.shiningcat.simplehiit.android.shared.home.HomeViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.home.HomeDialog
import fr.shiningcat.simplehiit.sharedui.home.HomeViewState
import org.koin.androidx.compose.koinViewModel

@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit,
    uiArrangement: UiArrangement,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger,
    viewModel: HomeViewModel = koinViewModel(),
) {
    val viewState = viewModel.screenViewState.collectAsStateWithLifecycle().value
    val dialogViewState = viewModel.dialogViewState.collectAsStateWithLifecycle().value
    //
    HomeScreen(
        navigateTo = navigateTo,
        onResetWholeApp = { viewModel.resetWholeApp() },
        onResetWholeAppDeleteEverything = { viewModel.resetWholeAppConfirmationDeleteEverything() },
        decreaseNumberOfCycles = { viewModel.modifyNumberCycles(CyclesModification.DECREASE) },
        increaseNumberOfCycles = { viewModel.modifyNumberCycles(CyclesModification.INCREASE) },
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
    navigateTo: (fr.shiningcat.simplehiit.android.shared.core.Screen) -> Unit = {},
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
                currentDestination = fr.shiningcat.simplehiit.android.shared.core.Screen.Home,
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
    val previewUiArrangement = currentUiArrangement()
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

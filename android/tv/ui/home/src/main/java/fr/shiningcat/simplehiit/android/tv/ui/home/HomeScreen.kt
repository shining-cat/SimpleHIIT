package fr.shiningcat.simplehiit.android.tv.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.common.Screen
import fr.shiningcat.simplehiit.android.tv.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.home.contents.HomeContentHolder
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User

@ExperimentalTvMaterial3Api
@Composable
fun HomeScreen(
    navigateTo: (Screen) -> Unit,
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
        viewState = viewState,
        dialogViewState = dialogViewState,
        hiitLogger = hiitLogger,
    )
}

@ExperimentalTvMaterial3Api
@Composable
private fun HomeScreen(
    navigateTo: (Screen) -> Unit = {},
    onResetWholeApp: () -> Unit = {},
    onResetWholeAppDeleteEverything: () -> Unit = {},
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    viewState: HomeViewState,
    dialogViewState: HomeDialog,
    hiitLogger: HiitLogger? = null,
) {
    //
    NavigationDrawer(
        drawerContent = {
            // we don't use the drawerValue for now
            NavigationSideBar(
                navigateTo = navigateTo,
                currentDestination = Screen.Home,
                showStatisticsButton = viewState is HomeViewState.Nominal,
            )
        },
    ) {
        HomeContentHolder(
            navigateTo = navigateTo,
            resetWholeApp = onResetWholeApp,
            resetWholeAppDeleteEverything = onResetWholeAppDeleteEverything,
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            toggleSelectedUser = toggleSelectedUser,
            cancelDialog = cancelDialog,
            screenViewState = viewState,
            dialogViewState = dialogViewState,
            hiitLogger = hiitLogger,
        )
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@PreviewTvScreens
@Composable
private fun HomeScreenPreviewTV(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) viewState: HomeViewState,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            HomeScreen(
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

package fr.shining_cat.simplehiit.android.tv.ui.home

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NavigationDrawer
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.common.Screen

import fr.shining_cat.simplehiit.android.tv.ui.common.components.NavigationSideBar
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.home.contents.HomeContentHolder
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.User


@ExperimentalTvMaterial3Api
@Composable
fun HomeScreen(
    navigateTo: (String) -> Unit,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val durationsFormatter = DurationStringFormatter(
        hoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        hoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        hoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        minutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        minutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        seconds = stringResource(id = R.string.seconds_short)
    )
    viewModel.init(durationsFormatter)
    val viewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
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
        hiitLogger = hiitLogger
    )
}

@ExperimentalTvMaterial3Api
@Composable
private fun HomeScreen(
    navigateTo: (String) -> Unit = {},
    onResetWholeApp: () -> Unit = {},
    onResetWholeAppDeleteEverything: () -> Unit = {},
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    viewState: HomeViewState,
    dialogViewState: HomeDialog,
    hiitLogger: HiitLogger? = null
) {
    val view = LocalView.current
    val primaryAsInt = MaterialTheme.colorScheme.primary.toArgb()
    val darkMode = isSystemInDarkTheme()
    if (!view.isInEditMode) {
        SideEffect {
            //applying primary color to Status bar
            val window = (view.context as Activity).window
            window.statusBarColor = primaryAsInt
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = !darkMode
        }
    }
    //
    NavigationDrawer(
        drawerContent = {
            NavigationSideBar(
                navigateTo = navigateTo,
                currentDestination = Screen.Home,
                showStatisticsButton = viewState is HomeViewState.Nominal,
                drawerValue = it
            )
        }
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
            hiitLogger = hiitLogger
        )
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@ExperimentalTvMaterial3Api
@Composable
private fun HomeScreenPreviewTV(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) viewState: HomeViewState
) {
    SimpleHiitTvTheme {
        Surface {
            HomeScreen(
                viewState = viewState,
                dialogViewState = HomeDialog.None
            )
        }
    }
}

internal class HomeScreenPreviewParameterProvider :
    PreviewParameterProvider<HomeViewState> {
    override val values: Sequence<HomeViewState>
        get() = sequenceOf(
            HomeViewState.Loading,
            HomeViewState.Error(errorCode = "12345"),
            HomeViewState.MissingUsers(
                numberCumulatedCycles = 5,
                cycleLength = "4mn",
                totalSessionLengthFormatted = "total time: 20mn"
            ),
            HomeViewState.Nominal(
                numberCumulatedCycles = 5,
                cycleLength = "4mn",
                users = listOf(User(123L, "User 1", selected = true)),
                totalSessionLengthFormatted = "total time: 20mn"
            ),
            HomeViewState.Nominal(
                numberCumulatedCycles = 5,
                cycleLength = "4mn",
                users = listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                    User(345L, "User 3", selected = true)
                ),
                totalSessionLengthFormatted = "total time: 20mn"
            )
        )
}
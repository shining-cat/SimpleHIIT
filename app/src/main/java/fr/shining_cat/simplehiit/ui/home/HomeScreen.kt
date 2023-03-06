package fr.shining_cat.simplehiit.ui.home

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.core.view.WindowCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.Screen
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.ui.components.ConfirmDialog
import fr.shining_cat.simplehiit.ui.home.HomeViewState.*
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    viewModel.logD("HomeScreen", "INIT")
    viewModel.init(
        formatStringHoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        formatStringHoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        formatStringHoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        formatStringMinutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        formatStringMinutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        formatStringSeconds = stringResource(id = R.string.seconds_short)
    )
    val viewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    HomeScreen(
        onNavigate = { navController.navigate(it) },
        onResetWholeApp = { viewModel.resetWholeApp() },
        onResetWholeAppDeleteEverything = { viewModel.resetWholeAppConfirmationDeleteEverything() },
        openInputNumberCycles = { viewModel.openInputNumberCyclesDialog(it) },
        saveInputNumberCycles = { viewModel.updateNumberCumulatedCycles(it) },
        validateInputNumberCycles = { viewModel.validateInputNumberCycles(it) },
        toggleSelectedUser = { viewModel.toggleSelectedUser(it) },
        cancelDialog = { viewModel.cancelDialog() },
        viewState = viewState,
        dialogViewState = dialogViewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    onResetWholeApp: () -> Unit = {},
    onResetWholeAppDeleteEverything: () -> Unit = {},
    openInputNumberCycles: (Int) -> Unit = {},
    saveInputNumberCycles: (String) -> Unit = {},
    validateInputNumberCycles: (String) -> Constants.InputError,
    toggleSelectedUser: (User) -> Unit = {},
    cancelDialog: () -> Unit = {},
    viewState: HomeViewState,
    dialogViewState: HomeDialog
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
    Scaffold(
        topBar = {
            HomeTopBar(onNavigate)
        },
        content = { paddingValues ->
            HomeContent(
                innerPadding = paddingValues,
                navigateTo = onNavigate,
                resetWholeApp = onResetWholeApp,
                resetWholeAppDeleteEverything = onResetWholeAppDeleteEverything,
                openInputNumberCycles = openInputNumberCycles,
                saveInputNumberCycles = saveInputNumberCycles,
                validateInputNumberCycles = validateInputNumberCycles,
                toggleSelectedUser = toggleSelectedUser,
                cancelDialog = cancelDialog,
                screenViewState = viewState,
                dialogViewState = dialogViewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeTopBar(navigateTo: (String) -> Unit = {}) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        title = {
            Text(
                text = stringResource(R.string.app_name),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        },
        actions = {
            IconButton(onClick = { navigateTo(Screen.Settings.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.cog),
                    contentDescription = stringResource(id = R.string.settings_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
            IconButton(onClick = { navigateTo(Screen.Statistics.route) }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.bar_chart),
                    contentDescription = stringResource(id = R.string.statistics_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
private fun HomeContent(
    innerPadding: PaddingValues,
    navigateTo: (String) -> Unit,
    resetWholeApp: () -> Unit,
    resetWholeAppDeleteEverything: () -> Unit,
    openInputNumberCycles: (Int) -> Unit,
    saveInputNumberCycles: (String) -> Unit,
    validateInputNumberCycles: (String) -> Constants.InputError,
    toggleSelectedUser: (User) -> Unit,
    cancelDialog: () -> Unit,
    screenViewState: HomeViewState,
    dialogViewState: HomeDialog
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            //.verticalScroll(rememberScrollState())
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(start = 16.dp, end = 16.dp, top = 24.dp),
            text = stringResource(id = R.string.hiit_description)
        )
        when (screenViewState) {
            is HomeLoading -> CircularProgressIndicator()
            is HomeError -> HomeContentBrokenState(
                errorCode = screenViewState.errorCode,
                resetWholeApp = resetWholeApp
            )
            is HomeMissingUsers -> HomeContentMissingUsers(
                openInputNumberCycles = openInputNumberCycles,
                navigateToSettings = { navigateTo(Screen.Settings.route) },
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength
            )
            is HomeNominal -> HomeContentNominal(
                openInputNumberCycles = openInputNumberCycles,
                numberOfCycles = screenViewState.numberCumulatedCycles,
                lengthOfCycle = screenViewState.cycleLength,
                users = screenViewState.users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = { navigateTo(Screen.Session.route) }
            )
        }
        when (dialogViewState) {
            is HomeDialog.HomeDialogConfirmWholeReset -> {
                ConfirmDialog(
                    message = stringResource(id = R.string.error_confirm_whole_reset),
                    buttonConfirmLabel = stringResource(id = R.string.delete_button_label),
                    onConfirm = resetWholeAppDeleteEverything,
                    dismissAction = cancelDialog
                )
            }
            is HomeDialog.HomeDialogInputNumberCycles -> HomeContentInputNumberCyclesDialog(
                saveInputNumberCycles = saveInputNumberCycles,
                validateInputNumberCycles = validateInputNumberCycles,
                numberOfCycles = dialogViewState.initialNumberOfCycles,
                onCancel = cancelDialog
            )
            HomeDialog.None -> {/*do nothing*/
            }
        }
    }
}

@Composable
private fun HomeContentNominal(
    openInputNumberCycles: (Int) -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit,
    navigateToSession: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        NumberCyclesSection(
            openInputNumberCycles = openInputNumberCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        SelectUsersSection(
            users = users,
            toggleSelectedUser = toggleSelectedUser
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = navigateToSession,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = stringResource(id = R.string.launch_session_label))
        }
        Spacer(modifier = Modifier.height(24.dp))
    }
}

@Composable
private fun HomeContentMissingUsers(
    openInputNumberCycles: (Int) -> Unit,
    navigateToSettings: () -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        NumberCyclesSection(
            openInputNumberCycles = openInputNumberCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle
        )
        SelectUsersSectionNoUsers(navigateToSettings)
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeScreenPreview(
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) pairOfStates: Pair<HomeViewState, HomeDialog>
) {
    SimpleHiitTheme {
        HomeScreen(
            validateInputNumberCycles = { Constants.InputError.NONE },
            viewState = pairOfStates.first,
            dialogViewState = pairOfStates.second
        )
    }
}

internal class HomeScreenPreviewParameterProvider :
    PreviewParameterProvider<Pair<HomeViewState, HomeDialog>> {
    override val values: Sequence<Pair<HomeViewState, HomeDialog>>
        get() = sequenceOf(
            Pair(HomeLoading, HomeDialog.None),
            Pair(HomeError(errorCode = "12345"), HomeDialog.None),
            Pair(HomeError(errorCode = "12345"), HomeDialog.HomeDialogConfirmWholeReset),
            Pair(HomeMissingUsers(4, "4mn"), HomeDialog.None),
            Pair(HomeMissingUsers(4, "4mn"), HomeDialog.HomeDialogInputNumberCycles(4)),
            Pair(
                HomeNominal(
                    4,
                    "4mn",
                    listOf(
                        User(123L, "User 1", selected = true),
                        User(234L, "User 2", selected = false),
                        User(345L, "User 3", selected = true)
                    )
                ),
                HomeDialog.None
            )
        )
}


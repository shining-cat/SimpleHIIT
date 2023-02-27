package fr.shining_cat.simplehiit.ui.home

import android.app.Activity
import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.isSystemInDarkTheme
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
import androidx.compose.ui.res.painterResource
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
import fr.shining_cat.simplehiit.ui.components.ConfirmDialog
import fr.shining_cat.simplehiit.ui.home.HomeViewState.*
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun HomeScreen(
    navController: NavController,
    viewModel: HomeViewModel = hiltViewModel()
) {
    viewModel.logD("HomeScreen", "INIT")
    val viewState = viewModel.viewState.collectAsState().value
    //
    HomeScreen(
        onNavigate = { navController.navigate(it) },
        onResetWholeApp = { viewModel.resetWholeApp(it) },
        onResetWholeAppDeleteEverything = { viewModel.resetWholeAppConfirmationDeleteEverything() },
        viewState = viewState
    )
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
private fun HomeScreen(
    onNavigate: (String) -> Unit = {},
    onResetWholeApp: (String) -> Unit = {},
    onResetWholeAppDeleteEverything: () -> Unit = {},
    viewState: HomeViewState
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
                viewState = viewState
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
                    contentDescription = stringResource(id = R.string.statictics_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    )
}

@Composable
private fun HomeContent(
    innerPadding: PaddingValues,
    navigateTo: (String) -> Unit = {},
    resetWholeApp: (String) -> Unit = {},
    resetWholeAppDeleteEverything: () -> Unit = {},
    viewState: HomeViewState
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 24.dp),
            text = stringResource(id = R.string.hiit_description)
        )
        when (viewState) {
            is HomeLoading -> CircularProgressIndicator()
            is HomeNominal -> HomeContentNominal(
                navigateToSession = { navigateTo(Screen.Settings.route) },
                viewState = viewState
            )
            is HomeMissingUsers -> HomeContentMissingUsers(
                navigateToSettings = { navigateTo(Screen.Settings.route) },
                numberOfCycles = viewState.numberCumulatedCycles,
                lengthOfCycle = viewState.cycleLength
            )
            is HomeError -> HomeContentBrokenState(
                errorCode = viewState.errorCode,
                resetWholeApp = resetWholeApp
            )
            is HomeDialogConfirmWholeReset -> HomeContentBrokenState(
                errorCode = viewState.errorCode,
                resetWholeApp = resetWholeApp,
                showConfirmationDialog = true,
                resetWholeAppDeleteEverything = resetWholeAppDeleteEverything
            )
        }
    }
}

@Composable
private fun HomeContentBrokenState(
    errorCode: String,
    resetWholeApp: (String) -> Unit = {},
    showConfirmationDialog: Boolean = false,
    resetWholeAppDeleteEverything: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 0.dp, vertical = 48.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 48.dp),
            text = stringResource(id = R.string.error_irrecoverable_state),
            style = MaterialTheme.typography.bodyMedium
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 48.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.error_code, errorCode),
                style = MaterialTheme.typography.bodySmall
            )
        }
        if (!showConfirmationDialog) {
            Button(
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 48.dp)
                    .align(Alignment.CenterHorizontally),
                onClick = { resetWholeApp(errorCode) }) {
                Text(text = stringResource(id = R.string.reset_app_button_label))
            }
        } else {
            ConfirmDialog(
                message = stringResource(id = R.string.error_confirm_whole_reset),
                buttonConfirmLabel = stringResource(id = R.string.delete_button_label),
                onConfirm = resetWholeAppDeleteEverything
            ) {}
        }
    }
}

@Composable
private fun HomeContentNominal(
    navigateToSession: () -> Unit,
    viewState: HomeNominal
) {

}

@Composable
private fun HomeContentMissingUsers(
    navigateToSettings: () -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 0.dp, vertical = 24.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
            text = stringResource(id = R.string.number_of_cycle_setting_title),
            style = MaterialTheme.typography.headlineMedium
        )
        Row(
            Modifier
                .fillMaxWidth()
                .padding(horizontal = 0.dp, vertical = 24.dp)
        ) {

        }
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
    @PreviewParameter(HomeScreenPreviewParameterProvider::class) homeViewState: HomeViewState
) {
    SimpleHiitTheme {
        HomeScreen(viewState = homeViewState)
    }
}

internal class HomeScreenPreviewParameterProvider :
    PreviewParameterProvider<HomeViewState> {
    override val values: Sequence<HomeViewState>
        get() = sequenceOf(
            HomeLoading,
            HomeViewState.HomeError(errorCode = "12345"),
            HomeDialogConfirmWholeReset(errorCode = "12345"),
        )
}


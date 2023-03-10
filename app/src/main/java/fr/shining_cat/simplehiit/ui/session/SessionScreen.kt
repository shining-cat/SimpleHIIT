package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.*
import fr.shining_cat.simplehiit.ui.components.ConfirmDialog
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: SessionViewModel = hiltViewModel()
) {
    viewModel.logD("SessionScreen", "INIT")
    val durationsFormatter = DurationStringFormatter(
        hoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        hoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        hoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        minutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        minutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        seconds = stringResource(id = R.string.seconds_short)
    )
    viewModel.init(durationsFormatter)
    //
    val screenViewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    SessionScreen(
        onNavigateUp = { navController.navigateUp() },
        pause = { viewModel.pause() },
        resume = { viewModel.resume() },
        dialogViewState = dialogViewState,
        screenViewState = screenViewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onNavigateUp: () -> Boolean = { false },
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    pause: () -> Unit,
    resume: () -> Unit
) {
    Scaffold(
        topBar = {
            SessionTopBar(pause = pause, screenViewState = screenViewState)
        },
        content = { paddingValues ->
            SessionContent(
                innerPadding = paddingValues,
                dialogViewState = dialogViewState,
                screenViewState = screenViewState,
                resume = resume,
                onNavigateUp = onNavigateUp
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTopBar(
    pause: () -> Unit,
    screenViewState: SessionViewState
) {
    val title = when (screenViewState) {
        is SessionViewState.SessionRestNominal -> stringResource(R.string.session_rest_page_title)
        is SessionViewState.SessionWorkNominal -> stringResource(R.string.session_work_page_title)
        else -> ""
    }
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = pause) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
private fun SessionContent(
    innerPadding: PaddingValues,
    screenViewState: SessionViewState,
    dialogViewState: SessionDialog,
    onNavigateUp: () -> Boolean = { false },
    resume: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (screenViewState) {
            SessionViewState.SessionLoading -> CircularProgressIndicator()
            is SessionViewState.SessionErrorState -> SessionErrorStateContent(screenViewState)
            is SessionViewState.SessionRestNominal -> SessionRestNominalContent(screenViewState)
            is SessionViewState.SessionWorkNominal -> SessionWorkNominalContent(screenViewState)
            is SessionViewState.SessionFinished -> SessionFinishedContent(screenViewState)
        }
        when (dialogViewState) {
            SessionDialog.None -> {}/*Do nothing*/
            SessionDialog.PauseDialog -> ConfirmDialog(
                message = stringResource(id = R.string.pause),
                primaryButtonLabel = stringResource(id = R.string.resume_button_label),
                primaryAction = resume,
                secondaryButtonLabel = stringResource(R.string.exit_button_label),
                secondaryAction = { onNavigateUp() },
                dismissAction = resume
            )
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
private fun SessionScreenPreview(
    @PreviewParameter(SessionScreenPreviewParameterProvider::class) viewStates: Pair<SessionViewState, SessionDialog>
) {
    SimpleHiitTheme {
        SessionScreen(
            onNavigateUp = { true },
            pause = {},
            resume = {},
            screenViewState = viewStates.first,
            dialogViewState = viewStates.second
        )
    }
}

internal class SessionScreenPreviewParameterProvider :
    PreviewParameterProvider<Pair<SessionViewState, SessionDialog>> {
    override val values: Sequence<Pair<SessionViewState, SessionDialog>>
        get() = sequenceOf(
            Pair(SessionViewState.SessionLoading, SessionDialog.None),
            Pair(SessionViewState.SessionErrorState("Blabla error code"), SessionDialog.None),
            Pair(
                SessionViewState.SessionRestNominal(
                    nextExercise = Exercise.CatBackLegLift,
                    restRemainingTime = "25s",
                    restProgress = 53,
                    sessionRemainingTime = "16mn 23s",
                    sessionProgress = 24
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.SessionRestNominal(
                    nextExercise = Exercise.CatBackLegLift,
                    restRemainingTime = "25s",
                    restProgress = 53,
                    sessionRemainingTime = "16mn 23s",
                    sessionProgress = 24
                ),
                SessionDialog.PauseDialog
            ),
            Pair(
                SessionViewState.SessionWorkNominal(
                    currentExercise = Exercise.CrabAdvancedBridge,
                    exerciseRemainingTime = "3s",
                    exerciseProgress = 10,
                    sessionRemainingTime = "5mn 12s",
                    sessionProgress = 3
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.SessionWorkNominal(
                    currentExercise = Exercise.CrabAdvancedBridge,
                    exerciseRemainingTime = "3s",
                    exerciseProgress = 10,
                    sessionRemainingTime = "5mn 12s",
                    sessionProgress = 3
                ),
                SessionDialog.PauseDialog
            ),
            Pair(SessionViewState.SessionFinished(
                "16mn",
                exercisesDone = listOf(
                    "Lunges : Alternate to the Front Raising Arms in Front",
                    "Lunges : Alternate to the Back Squeezing Shoulder Blades",
                    "Lunges : Alternate Lunge and Back Kick One Leg",
                    "Lying : Superman Twist",
                    "Sitting : Hands on the Floor Fold and Extend Legs",
                    "Squat : Basic Squat",
                    "Standing : Alternate Raise Extended Leg to the Front and Touching Foot with Opposite Hand"
                )
            ), SessionDialog.None),
        )
}
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
import fr.shining_cat.simplehiit.ui.components.ChoiceDialog
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionScreen(
    navController: NavController,
    viewModel: SessionViewModel = hiltViewModel()
) {
    //TODO: we want to capture BACK navigation to open pause dialog!
    //TODO: we need to pause the session in onResume equivalent (then, when coming back the state will be paused with dialog open so we should need nothing for the "coming back" part)
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
        onAbortSession = { viewModel.abortSession() },
        pause = { viewModel.pause() },
        navigateUp = { navController.navigateUp() },
        resume = { viewModel.resume() },
        dialogViewState = dialogViewState,
        screenViewState = screenViewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionScreen(
    onAbortSession: () -> Unit,
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    pause: () -> Unit,
    resume: () -> Unit,
    navigateUp: () -> Boolean,
) {
    Scaffold(
        topBar = {
            SessionTopBar(pause = pause, navigateUp = navigateUp, screenViewState = screenViewState)
        },
        content = { paddingValues ->
            SessionContent(
                innerPadding = paddingValues,
                dialogViewState = dialogViewState,
                screenViewState = screenViewState,
                resume = resume,
                onAbortSession = onAbortSession,
                navigateUp = navigateUp
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SessionTopBar(
    pause: () -> Unit,
    screenViewState: SessionViewState,
    navigateUp: () -> Boolean
) {
    val (title, clickOnButton) = when (screenViewState) {
        is SessionViewState.RestNominal -> Pair(stringResource(R.string.session_rest_page_title), pause)
        is SessionViewState.WorkNominal -> Pair(stringResource(R.string.session_work_page_title), pause)
        is SessionViewState.InitialCountDownSession -> Pair(stringResource(id = R.string.session_prepare_page_title), pause)
        is SessionViewState.Finished -> Pair(stringResource(id = R.string.finish_page_topbar), navigateUp)
        is SessionViewState.Error -> Pair(stringResource(id = R.string.error), navigateUp)
        SessionViewState.Loading -> Pair(stringResource(id = R.string.session_loading_page_topBar), navigateUp)
    }
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { clickOnButton() }) {
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
    onAbortSession: () -> Unit,
    resume: () -> Unit,
    navigateUp: () -> Boolean
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (screenViewState) {
            SessionViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is SessionViewState.Error -> SessionErrorStateContent(screenViewState)
            is SessionViewState.InitialCountDownSession -> SessionPrepareContent(screenViewState)
            is SessionViewState.RestNominal -> SessionRestNominalContent(screenViewState)
            is SessionViewState.WorkNominal -> SessionWorkNominalContent(screenViewState)
            is SessionViewState.Finished -> {
                if(screenViewState.workingStepsDone.isEmpty()){
                    navigateUp()
                }else {
                    SessionFinishedContent(screenViewState)
                }
            }
        }
        when (dialogViewState) {
            SessionDialog.None -> {}/*Do nothing*/
            SessionDialog.Pause -> ChoiceDialog(
                title = stringResource(id = R.string.pause),
                message = stringResource(id = R.string.pause_explanation),
                primaryButtonLabel = stringResource(id = R.string.resume_button_label),
                primaryAction = resume,
                secondaryButtonLabel = stringResource(R.string.abort_session_button_label),
                secondaryAction = onAbortSession,
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
            onAbortSession = {},
            dialogViewState = viewStates.second,
            screenViewState = viewStates.first,
            pause = {},
            resume = {},
            navigateUp = {true}
        )
    }
}

internal class SessionScreenPreviewParameterProvider :
    PreviewParameterProvider<Pair<SessionViewState, SessionDialog>> {
    override val values: Sequence<Pair<SessionViewState, SessionDialog>>
        get() = sequenceOf(
            Pair(SessionViewState.Loading, SessionDialog.None),
            Pair(SessionViewState.Error("Blabla error code"), SessionDialog.None),
            Pair(SessionViewState.InitialCountDownSession(
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .5f,
                    playBeep = true
                )
            ), SessionDialog.None),
            Pair(
                SessionViewState.RestNominal(
                    nextExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    restRemainingTime = "25s",
                    restProgress = .53f,
                    sessionRemainingTime = "16mn 23s",
                    sessionProgress = .24f
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.RestNominal(
                    nextExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    restRemainingTime = "25s",
                    restProgress = .53f,
                    sessionRemainingTime = "16mn 23s",
                    sessionProgress = .24f,
                    countDown = CountDown(
                        secondsDisplay = "3",
                        progress = .5f,
                        playBeep = true
                    )
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.RestNominal(
                    nextExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    restRemainingTime = "25s",
                    restProgress = .23f,
                    sessionRemainingTime = "16mn 23s",
                    sessionProgress = .57f
                ),
                SessionDialog.Pause
            ),
            Pair(
                SessionViewState.WorkNominal(
                    currentExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.NONE,
                    exerciseRemainingTime = "3s",
                    exerciseRemainingPercentage = .7f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .3f
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.WorkNominal(
                    currentExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.NONE,
                    exerciseRemainingTime = "3s",
                    exerciseRemainingPercentage = .7f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .3f,
                    countDown = CountDown(
                        secondsDisplay = "5",
                        progress = 0f,
                        playBeep = false
                    )
                ),
                SessionDialog.None
            ),
            Pair(
                SessionViewState.WorkNominal(
                    currentExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.LEFT,
                    exerciseRemainingTime = "3s",
                    exerciseRemainingPercentage = .5f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .2f
                ),
                SessionDialog.Pause
            ),
            Pair(SessionViewState.Finished(
                "16mn",
                workingStepsDone = listOf(
                    SessionStepDisplay(Exercise.CatBackLegLift, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.CatKneePushUp, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.LungesArmsCrossSide, ExerciseSide.LEFT),
                    SessionStepDisplay(Exercise.LungesArmsCrossSide, ExerciseSide.RIGHT),
                    SessionStepDisplay(Exercise.LungesTwist, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.LyingStarToeTouchSitUp, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.LyingSupermanTwist, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.StandingMountainClimber, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.PlankMountainClimber, ExerciseSide.LEFT),
                    SessionStepDisplay(Exercise.PlankMountainClimber, ExerciseSide.RIGHT),
                    SessionStepDisplay(Exercise.StandingKickCrunches, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.SquatBasic, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.PlankShoulderTap, ExerciseSide.NONE),
                    SessionStepDisplay(Exercise.PlankBirdDogs, ExerciseSide.NONE)
                )
            ), SessionDialog.None),
        )
}
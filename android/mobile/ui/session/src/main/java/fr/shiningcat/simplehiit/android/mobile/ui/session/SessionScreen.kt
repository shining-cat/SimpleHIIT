package fr.shiningcat.simplehiit.android.mobile.ui.session

import android.content.res.Configuration
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigateUpTopBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.SessionSideBarComponent
import fr.shiningcat.simplehiit.android.mobile.ui.session.contents.SessionContentHolder
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay

@Composable
fun SessionScreen(
    navigateUp: () -> Boolean,
    uiArrangement: UiArrangement,
    hiitLogger: HiitLogger,
    viewModel: SessionViewModel = hiltViewModel(),
    lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current,
) {
    val durationsFormatter =
        DurationStringFormatter(
            hoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
            hoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
            hoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
            minutesSeconds = stringResource(id = R.string.minutes_seconds_short),
            minutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
            seconds = stringResource(id = R.string.seconds_short),
        )
    viewModel.init(durationsFormatter)
    // Handling the sound loading in the viewModel's soundPool:
    if (viewModel.noSoundLoadingRequestedYet) {
        hiitLogger.d("SessionScreen", "loading beep sound in SoundPool")
        // we want this loading to only happen once, to benefit from the pooling and avoid playback latency, but SideEffects wouldn't let us access the context we need
        val beepSoundLoadedInPool =
            viewModel.getSoundPool()?.load(LocalContext.current, R.raw.sound_beep, 0)
        viewModel.noSoundLoadingRequestedYet = false
        if (beepSoundLoadedInPool == null) {
            hiitLogger.e("SessionScreen", "beepSoundLoadedInPool is null, no sound will be played")
        } else {
            viewModel.setLoadedSound(beepSoundLoadedInPool)
        }
    }
    // Setting up a LifeCycle observer to catch the onPause event of the Android Lifecycle so we can pause the session
    var lifecycleEvent by remember { mutableStateOf(Lifecycle.Event.ON_ANY) }
    DisposableEffect(lifecycleOwner) {
        val lifecycleObserver =
            LifecycleEventObserver { _, event ->
                lifecycleEvent = event
            }

        lifecycleOwner.lifecycle.addObserver(lifecycleObserver)

        onDispose {
            // Don't forget to clean up the LifeCycle observer when we're disposed
            lifecycleOwner.lifecycle.removeObserver(lifecycleObserver)
        }
    }

    LaunchedEffect(lifecycleEvent) {
        if (lifecycleEvent == Lifecycle.Event.ON_PAUSE) {
            viewModel.pause()
        }
    }
    //
    val screenViewState by viewModel.screenViewState.collectAsState()
    val dialogViewState by viewModel.dialogViewState.collectAsState()
    //
    SessionScreen(
        onAbortSession = { viewModel.abortSession() },
        uiArrangement = uiArrangement,
        dialogViewState = dialogViewState,
        screenViewState = screenViewState,
        pause = { viewModel.pause() },
        resume = { viewModel.resume() },
        navigateUp = navigateUp,
        hiitLogger = hiitLogger,
    )
}

@Composable
private fun SessionScreen(
    navigateUp: () -> Boolean = { true },
    onAbortSession: () -> Unit = {},
    pause: () -> Unit = {},
    resume: () -> Unit = {},
    uiArrangement: UiArrangement,
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    BackHandler(enabled = screenViewState is SessionViewState.RunningNominal) {
        pause()
    }
    val (title, buttonLabel, clickOnButton) =
        when (screenViewState) {
            is SessionViewState.RunningNominal -> {
                when (screenViewState.periodType) {
                    RunningSessionStepType.REST ->
                        Triple(
                            R.string.session_rest_page_title,
                            R.string.pause,
                            pause,
                        )

                    RunningSessionStepType.WORK ->
                        Triple(
                            R.string.session_work_page_title,
                            R.string.pause,
                            pause,
                        )
                }
            }

            is SessionViewState.InitialCountDownSession ->
                Triple(
                    R.string.session_prepare_page_title,
                    R.string.back_button_content_label,
                    navigateUp,
                )

            is SessionViewState.Finished ->
                Triple(
                    R.string.finish_page_topbar,
                    R.string.back_button_content_label,
                    navigateUp,
                )

            is SessionViewState.Error ->
                Triple(
                    R.string.error,
                    R.string.back_button_content_label,
                    navigateUp,
                )

            SessionViewState.Loading ->
                Triple(
                    R.string.session_loading_page_topBar,
                    R.string.back_button_content_label,
                    navigateUp,
                )
        }
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            SessionSideBarComponent(
                title = title,
                onBackButtonClick = { clickOnButton.invoke() },
                backButtonLabel = buttonLabel,
            )
        }
        Column(
            modifier =
                Modifier
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                NavigateUpTopBar(
                    navigateUp = {
                        clickOnButton.invoke()
                        true
                    },
                    title = title,
                    overrideBackLabel = buttonLabel,
                )
            }
            SessionContentHolder(
                dialogViewState = dialogViewState,
                screenViewState = screenViewState,
                uiArrangement = uiArrangement,
                onAbortSession = onAbortSession,
                resume = resume,
                navigateUp = navigateUp,
                hiitLogger = hiitLogger,
            )
        }
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
private fun SessionScreenPreviewPhonePortrait(
    @PreviewParameter(SessionScreenPreviewParameterProvider::class) viewState: SessionViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionScreen(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.VERTICAL,
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SessionScreenPreviewTabletLandscape(
    @PreviewParameter(SessionScreenPreviewParameterProvider::class) viewState: SessionViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionScreen(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.HORIZONTAL,
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
private fun SessionScreenPreviewPhoneLandscape(
    @PreviewParameter(SessionScreenPreviewParameterProvider::class) viewState: SessionViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionScreen(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.HORIZONTAL,
            )
        }
    }
}

internal class SessionScreenPreviewParameterProvider : PreviewParameterProvider<SessionViewState> {
    override val values: Sequence<SessionViewState>
        get() =
            sequenceOf(
                SessionViewState.Loading,
                SessionViewState.Error("Blabla error code"),
                SessionViewState.InitialCountDownSession(
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = .5f,
                            playBeep = true,
                        ),
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    stepRemainingTime = "25s",
                    stepRemainingPercentage = .53f,
                    sessionRemainingTime = "16mn 23s",
                    sessionRemainingPercentage = .24f,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    stepRemainingTime = "25s",
                    stepRemainingPercentage = .53f,
                    sessionRemainingTime = "16mn 23s",
                    sessionRemainingPercentage = .24f,
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = .5f,
                            playBeep = true,
                        ),
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.CatBackLegLift,
                    side = ExerciseSide.RIGHT,
                    stepRemainingTime = "25s",
                    stepRemainingPercentage = .23f,
                    sessionRemainingTime = "16mn 23s",
                    sessionRemainingPercentage = .57f,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .7f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .3f,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .7f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .3f,
                    countDown =
                        CountDown(
                            secondsDisplay = "5",
                            progress = 0f,
                            playBeep = false,
                        ),
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.LEFT,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .5f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .2f,
                ),
                SessionViewState.Finished(
                    "16mn",
                    workingStepsDone =
                        listOf(
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
                            SessionStepDisplay(Exercise.PlankBirdDogs, ExerciseSide.NONE),
                        ),
                ),
            )
}

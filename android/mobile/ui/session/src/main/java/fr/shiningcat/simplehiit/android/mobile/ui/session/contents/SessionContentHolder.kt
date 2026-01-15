/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import android.view.View
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.PauseDialog
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shiningcat.simplehiit.sharedui.session.CountDown
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.sharedui.session.SessionDialog
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState

@Composable
fun SessionContentHolder(
    modifier: Modifier = Modifier,
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    uiArrangement: UiArrangement,
    onAbortSession: () -> Unit = {},
    onResume: () -> Unit = {},
    navigateUp: () -> Boolean = { true },
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val currentView = LocalView.current
    val currentScreenViewState by rememberUpdatedState(screenViewState)

    LaunchedEffect(currentScreenViewState::class, dialogViewState::class) {
        handleScreenLock(
            currentView = currentView,
            dialogViewState = dialogViewState,
            screenViewState = screenViewState,
            hiitLogger = hiitLogger,
        )
    }
    DisposableEffect(Unit) {
        onDispose {
            // ensure we don't keep the screen on locked when leaving here
            currentView.keepScreenOn = false
            hiitLogger?.d("SessionContentHolder", "onDispose:: removing lock screen")
        }
    }
    Box(modifier = modifier) {
        when (screenViewState) {
            SessionViewState.Loading -> {
                BasicLoading(modifier = Modifier.fillMaxSize())
            }
            is SessionViewState.Error -> {
                SessionErrorStateContent(
                    modifier = Modifier.fillMaxSize(),
                    screenViewState = screenViewState,
                    navigateUp = navigateUp,
                    onAbort = onAbortSession,
                    hiitLogger = hiitLogger,
                )
            }
            is SessionViewState.InitialCountDownSession -> {
                SessionPrepareContent(
                    modifier = Modifier.fillMaxSize(),
                    viewState = screenViewState,
                    hiitLogger = hiitLogger,
                )
            }
            is SessionViewState.RunningNominal -> {
                SessionRunningNominalContent(
                    modifier = Modifier.fillMaxSize(),
                    uiArrangement = uiArrangement,
                    viewState = screenViewState,
                    isPaused = dialogViewState is SessionDialog.Pause,
                    hiitLogger = hiitLogger,
                )
            }
            is SessionViewState.Finished -> {
                if (screenViewState.workingStepsDone.isEmpty()) {
                    // ensure navigateUp is never triggered more than once:
                    LaunchedEffect(Unit) {
                        hiitLogger?.d(
                            "SessionContentHolder",
                            "SessionViewState.workingStepsDone is empty, invoke navigateUp",
                        )
                        navigateUp()
                    }
                } else {
                    SessionFinishedContent(
                        modifier = Modifier.fillMaxSize(),
                        viewState = screenViewState,
                        hiitLogger = hiitLogger,
                    )
                }
            }
        }
    }
    when (dialogViewState) {
        SessionDialog.None -> {} // Do nothing
        SessionDialog.Pause -> {
            PauseDialog(
                onResume = onResume,
                onAbort = onAbortSession,
            )
        }
    }
}

private fun handleScreenLock(
    currentView: View,
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    hiitLogger: HiitLogger?,
) {
    val lockScreenOn =
        // if a dialog is showing, do not keep the screen on
        dialogViewState is SessionDialog.None &&
            (
                // screenViewStates for which the screen should be locked on
                screenViewState is SessionViewState.Loading ||
                    screenViewState is SessionViewState.InitialCountDownSession ||
                    screenViewState is SessionViewState.RunningNominal
            )
    hiitLogger?.d("SessionContentHolder", "handleScreenLock:: lock screen: $lockScreenOn")
    currentView.keepScreenOn = lockScreenOn
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SessionContentHolderPreview(
    @PreviewParameter(SessionContentHolderPreviewParameterProvider::class) viewState: SessionViewState,
) {
    val previewUiArrangement = currentUiArrangement()
    SimpleHiitMobileTheme {
        Surface {
            SessionContentHolder(
                modifier = Modifier.fillMaxSize(),
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = previewUiArrangement,
            )
        }
    }
}

internal class SessionContentHolderPreviewParameterProvider : PreviewParameterProvider<SessionViewState> {
    override val values: Sequence<SessionViewState>
        get() =
            sequenceOf(
                SessionViewState.Loading,
                SessionViewState.Error("Tralala error code"),
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
                    periodType = RunningSessionStepType.WORK,
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
                    side = ExerciseSide.RIGHT,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .7f,
                    sessionRemainingTime = "5mn 12s",
                    sessionRemainingPercentage = .3f,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.CrabAdvancedBridge,
                    side = ExerciseSide.RIGHT,
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

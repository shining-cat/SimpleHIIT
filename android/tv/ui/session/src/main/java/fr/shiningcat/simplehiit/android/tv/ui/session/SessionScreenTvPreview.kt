package fr.shiningcat.simplehiit.android.tv.ui.session

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay
import fr.shiningcat.simplehiit.sharedui.session.CountDown
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.sharedui.session.SessionDialog
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
@Composable
fun SessionScreenTvPreview(
    @PreviewParameter(SessionScreenPreviewParameterProvider::class) viewState: SessionViewState,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionScreen(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
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

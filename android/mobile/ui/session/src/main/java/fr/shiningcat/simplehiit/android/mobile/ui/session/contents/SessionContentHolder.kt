package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ChoiceDialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionDialog
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.SessionStepDisplay

@Composable
fun SessionContentHolder(
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    uiArrangement: UiArrangement,
    onAbortSession: () -> Unit = {},
    resume: () -> Unit = {},
    navigateUp: () -> Boolean = { true },
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    when (screenViewState) {
        SessionViewState.Loading -> BasicLoading()

        is SessionViewState.Error -> SessionErrorStateContent(
            screenViewState = screenViewState,
            navigateUp = navigateUp,
            onAbort = onAbortSession,
            hiitLogger = hiitLogger
        )

        is SessionViewState.InitialCountDownSession -> SessionPrepareContent(
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.RunningNominal -> SessionRunningNominalContent(
            uiArrangement = uiArrangement,
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.Finished -> {
            if (screenViewState.workingStepsDone.isEmpty()) {
                navigateUp()
            } else {
                SessionFinishedContent(
                    viewState = screenViewState,
                    hiitLogger = hiitLogger
                )
            }
        }
    }
    when (dialogViewState) {
        SessionDialog.None -> {} /*Do nothing*/
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

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun SessionContentHolderPreviewPhonePortrait(
    @PreviewParameter(SessionContentHolderPreviewParameterProvider::class) viewState: SessionViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionContentHolder(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.VERTICAL
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SessionContentHolderPreviewTabletLandscape(
    @PreviewParameter(SessionContentHolderPreviewParameterProvider::class) viewState: SessionViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionContentHolder(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.HORIZONTAL
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400
)
@Composable
private fun SessionContentHolderPreviewPhoneLandscape(
    @PreviewParameter(SessionContentHolderPreviewParameterProvider::class) viewState: SessionViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SessionContentHolder(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.HORIZONTAL
            )
        }
    }
}

internal class SessionContentHolderPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState> {
    override val values: Sequence<SessionViewState>
        get() = sequenceOf(
            SessionViewState.Loading,
            SessionViewState.Error("Tralala error code"),
            SessionViewState.InitialCountDownSession(
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .5f,
                    playBeep = true
                )
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.REST,
                displayedExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                stepRemainingTime = "25s",
                stepRemainingPercentage = .53f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .24f
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.REST,
                displayedExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                stepRemainingTime = "25s",
                stepRemainingPercentage = .53f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .24f,
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .5f,
                    playBeep = true
                )
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.REST,
                displayedExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                stepRemainingTime = "25s",
                stepRemainingPercentage = .23f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .57f
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.WORK,
                displayedExercise = Exercise.CrabAdvancedBridge,
                side = ExerciseSide.NONE,
                stepRemainingTime = "3s",
                stepRemainingPercentage = .7f,
                sessionRemainingTime = "5mn 12s",
                sessionRemainingPercentage = .3f
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.WORK,
                displayedExercise = Exercise.CrabAdvancedBridge,
                side = ExerciseSide.NONE,
                stepRemainingTime = "3s",
                stepRemainingPercentage = .7f,
                sessionRemainingTime = "5mn 12s",
                sessionRemainingPercentage = .3f,
                countDown = CountDown(
                    secondsDisplay = "5",
                    progress = 0f,
                    playBeep = false
                )
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.WORK,
                displayedExercise = Exercise.CrabAdvancedBridge,
                side = ExerciseSide.LEFT,
                stepRemainingTime = "3s",
                stepRemainingPercentage = .5f,
                sessionRemainingTime = "5mn 12s",
                sessionRemainingPercentage = .2f
            ),
            SessionViewState.Finished(
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
            )
        )
}

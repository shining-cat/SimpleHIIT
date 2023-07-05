package fr.shining_cat.simplehiit.android.mobile.ui.session.contents

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.ChoiceDialog
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.session.CountDown
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionDialog
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.Exercise
import fr.shining_cat.simplehiit.domain.common.models.ExerciseSide
import fr.shining_cat.simplehiit.domain.common.models.SessionStepDisplay

@Composable
fun SessionContentHolder(
    dialogViewState: SessionDialog,
    screenViewState: SessionViewState,
    uiArrangement: UiArrangement,
    onAbortSession: () -> Unit = {},
    resume: () -> Unit = {},
    navigateUp: () -> Boolean = {true},
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

        is SessionViewState.RestNominal -> SessionRestNominalContent(
            viewState = screenViewState,
            hiitLogger = hiitLogger
        )

        is SessionViewState.WorkNominal -> SessionWorkNominalContent(
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
    SimpleHiitTheme {
        Surface {
            SessionContentHolder(
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
    SimpleHiitTheme {
        Surface {
            SessionContentHolder(
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
    SimpleHiitTheme {
        Surface {
            SessionContentHolder(
                dialogViewState = SessionDialog.None,
                screenViewState = viewState,
                uiArrangement = UiArrangement.HORIZONTAL,
            )
        }
    }
}

internal class SessionContentHolderPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState> {
    override val values: Sequence<SessionViewState>
        get() = sequenceOf(
            SessionViewState.Loading,
            SessionViewState.Error("Blabla error code"),
            SessionViewState.InitialCountDownSession(
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .5f,
                    playBeep = true
                )
            ),
            SessionViewState.RestNominal(
                nextExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                restRemainingTime = "25s",
                restRemainingPercentage = .53f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .24f
            ),
            SessionViewState.RestNominal(
                nextExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                restRemainingTime = "25s",
                restRemainingPercentage = .53f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .24f,
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .5f,
                    playBeep = true
                )
            ),
            SessionViewState.RestNominal(
                nextExercise = Exercise.CatBackLegLift,
                side = ExerciseSide.RIGHT,
                restRemainingTime = "25s",
                restRemainingPercentage = .23f,
                sessionRemainingTime = "16mn 23s",
                sessionRemainingPercentage = .57f
            ),
            SessionViewState.WorkNominal(
                currentExercise = Exercise.CrabAdvancedBridge,
                side = ExerciseSide.NONE,
                exerciseRemainingTime = "3s",
                exerciseRemainingPercentage = .7f,
                sessionRemainingTime = "5mn 12s",
                sessionRemainingPercentage = .3f
            ),
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
            SessionViewState.WorkNominal(
                currentExercise = Exercise.CrabAdvancedBridge,
                side = ExerciseSide.LEFT,
                exerciseRemainingTime = "3s",
                exerciseRemainingPercentage = .5f,
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
            ),
        )
}
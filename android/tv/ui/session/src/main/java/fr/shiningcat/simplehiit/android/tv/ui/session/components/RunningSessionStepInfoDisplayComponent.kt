package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.android.tv.ui.session.R
import fr.shiningcat.simplehiit.android.tv.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun RunningSessionStepInfoDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))
        Row(
            modifier = Modifier.weight(2f, true),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
        }
        Spacer(modifier = Modifier.weight(.2f))
        val remainingPercentageStringRes =
            when (periodType) {
                RunningSessionStepType.REST -> CommonResourcesR.string.rest_remaining_in_s
                RunningSessionStepType.WORK -> CommonResourcesR.string.exercise_remaining_in_s
            }
        Row(
            modifier = Modifier.weight(1f, true),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RemainingPercentageComponent(
                modifier =
                    Modifier
                        .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_8)),
                label =
                    stringResource(
                        id = remainingPercentageStringRes,
                        viewState.stepRemainingTime,
                    ),
                percentage = viewState.stepRemainingPercentage,
                thickness = dimensionResource(R.dimen.running_session_step_remaining_progress_thickness),
                bicolor = false,
            )
        }
        Spacer(modifier = Modifier.weight(.2f))
        Row(
            modifier = Modifier.weight(1f, true),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            RemainingPercentageComponent(
                modifier =
                    Modifier
                        .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_8)),
                label =
                    stringResource(
                        id = CommonResourcesR.string.session_time_remaining,
                        viewState.sessionRemainingTime,
                    ),
                percentage = viewState.sessionRemainingPercentage,
                thickness = dimensionResource(R.dimen.running_session_session_remaining_progress_thickness),
                bicolor = true,
            )
        }
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun RunningSessionStepInfoDisplayComponentPreview(
    @PreviewParameter(RunningSessionStepInfoDisplayComponentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            RunningSessionStepInfoDisplayComponent(
                exercise = viewState.displayedExercise,
                periodType = viewState.periodType,
                exerciseSide = viewState.side,
                viewState = viewState,
            )
        }
    }
}

internal class RunningSessionStepInfoDisplayComponentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.RunningNominal> {
    override val values: Sequence<SessionViewState.RunningNominal>
        get() =
            sequenceOf(
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    stepRemainingTime = "25s",
                    stepRemainingPercentage = .2f,
                    sessionRemainingTime = "3mn 25s",
                    sessionRemainingPercentage = .75f,
                    countDown = null,
                ),
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    stepRemainingTime = "3s",
                    stepRemainingPercentage = .02f,
                    sessionRemainingTime = "3mn 3s",
                    sessionRemainingPercentage = .745f,
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = .2f,
                            playBeep = true,
                        ),
                ),
            )
}

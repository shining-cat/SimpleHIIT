package fr.shiningcat.simplehiit.android.tv.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.android.tv.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun RunningSessionStepInfoDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    viewState: SessionViewState.RunningNominal,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(32.dp))
        Row(
            modifier = Modifier.weight(2f, true),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
        }
        Spacer(modifier = Modifier.weight(.2f))
        val remainingPercentageStringRes = when (periodType) {
            RunningSessionStepType.REST -> R.string.rest_remaining_in_s
            RunningSessionStepType.WORK -> R.string.exercise_remaining_in_s
        }
        Row(
            modifier = Modifier.weight(1f, true),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RemainingPercentageComponent(
                modifier = Modifier
                    .padding(horizontal = 64.dp)
                    .height(100.dp),
                label = stringResource(
                    id = remainingPercentageStringRes,
                    viewState.stepRemainingTime
                ),
                percentage = viewState.stepRemainingPercentage,
                thickness = 16.dp,
                bicolor = false
            )
        }
        Spacer(modifier = Modifier.weight(.2f))
        Row(
            modifier = Modifier.weight(1f, true),
            verticalAlignment = Alignment.CenterVertically
        ) {
            RemainingPercentageComponent(
                modifier = Modifier
                    .padding(horizontal = 64.dp)
                    .height(100.dp),
                label = stringResource(
                    id = R.string.session_time_remaining,
                    viewState.sessionRemainingTime
                ),
                percentage = viewState.sessionRemainingPercentage,
                thickness = 8.dp,
                bicolor = true
            )
        }
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun RunningSessionStepInfoDisplayComponentPreview(
    @PreviewParameter(RunningSessionStepInfoDisplayComponentPreviewParameterProvider::class) viewState: SessionViewState.RunningNominal
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            RunningSessionStepInfoDisplayComponent(
                exercise = viewState.displayedExercise,
                periodType = viewState.periodType,
                exerciseSide = viewState.side,
                viewState = viewState
            )
        }
    }
}

internal class RunningSessionStepInfoDisplayComponentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.RunningNominal> {
    override val values: Sequence<SessionViewState.RunningNominal>
        get() = sequenceOf(
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.REST,
                displayedExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                stepRemainingTime = "25s",
                stepRemainingPercentage = .2f,
                sessionRemainingTime = "3mn 25s",
                sessionRemainingPercentage = .75f,
                countDown = null
            ),
            SessionViewState.RunningNominal(
                periodType = RunningSessionStepType.WORK,
                displayedExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                stepRemainingTime = "3s",
                stepRemainingPercentage = .02f,
                sessionRemainingTime = "3mn 3s",
                sessionRemainingPercentage = .745f,
                countDown = CountDown(
                    secondsDisplay = "3",
                    progress = .2f,
                    playBeep = true
                )
            )
        )
}
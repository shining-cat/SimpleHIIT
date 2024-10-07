package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
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
    hiitLogger: HiitLogger? = null,
) {
    Column(modifier = modifier) {
        ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
        Spacer(modifier = Modifier.weight(1f))
        val remainingPercentageStringRes =
            when (periodType) {
                RunningSessionStepType.REST -> R.string.rest_remaining_in_s
                RunningSessionStepType.WORK -> R.string.exercise_remaining_in_s
            }
        RemainingPercentageComponent(
            modifier =
                Modifier
                    .padding(horizontal = 64.dp)
                    .height(100.dp),
            label = stringResource(id = remainingPercentageStringRes, viewState.stepRemainingTime),
            percentage = viewState.stepRemainingPercentage,
            thickness = 16.dp,
            bicolor = false,
        )
        Spacer(modifier = Modifier.height(32.dp))
        RemainingPercentageComponent(
            modifier =
                Modifier
                    .padding(horizontal = 64.dp)
                    .height(100.dp),
            label =
                stringResource(
                    id = R.string.session_time_remaining,
                    viewState.sessionRemainingTime,
                ),
            percentage = viewState.sessionRemainingPercentage,
            thickness = 8.dp,
            bicolor = true,
        )
        Spacer(modifier = Modifier.height(32.dp))
    }
}

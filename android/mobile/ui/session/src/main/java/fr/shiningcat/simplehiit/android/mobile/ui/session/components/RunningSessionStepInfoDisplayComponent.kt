package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.commonutils.HiitLogger
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
    Column(
        modifier = modifier.verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        if (periodType == RunningSessionStepType.REST) {
            Text(
                text = stringResource(id = CommonResourcesR.string.coming_next),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_2)),
            )
        }
        ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))
        val remainingPercentageStringRes =
            when (periodType) {
                RunningSessionStepType.REST -> CommonResourcesR.string.rest_remaining_in_s
                RunningSessionStepType.WORK -> CommonResourcesR.string.exercise_remaining_in_s
            }
        RemainingPercentageComponent(
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
            label = stringResource(id = remainingPercentageStringRes, viewState.stepRemainingTime),
            percentage = viewState.stepRemainingPercentage,
            thickness = dimensionResource(R.dimen.step_remaining_progress_thickness),
            bicolor = false,
        )
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))
        RemainingPercentageComponent(
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
            label =
                stringResource(
                    id = CommonResourcesR.string.session_time_remaining,
                    viewState.sessionRemainingTime,
                ),
            percentage = viewState.sessionRemainingPercentage,
            thickness = dimensionResource(R.dimen.session_remaining_progress_thickness),
            bicolor = true,
        )
        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_4)))
    }
}

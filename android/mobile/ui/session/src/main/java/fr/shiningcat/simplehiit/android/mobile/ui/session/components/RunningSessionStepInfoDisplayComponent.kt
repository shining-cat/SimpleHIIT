/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.sharedui.session.SessionViewState
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
        modifier = modifier,
    ) {
        // Progress indicators for better visibility - session timer first
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

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

        val remainingPercentageStringRes =
            when (periodType) {
                RunningSessionStepType.REST -> CommonResourcesR.string.rest_remaining_in_s
                RunningSessionStepType.WORK -> CommonResourcesR.string.exercise_remaining_in_s
            }
        StepRemainingComponent(
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
            label = stringResource(id = remainingPercentageStringRes, viewState.stepRemainingTime),
            percentage = viewState.stepRemainingPercentage,
            thickness = dimensionResource(R.dimen.step_remaining_progress_thickness),
            periodType = periodType,
        )

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

        // Always reserve space for "coming next" to keep exercise description in fixed position
        Text(
            text =
                if (periodType == RunningSessionStepType.REST) {
                    stringResource(id = CommonResourcesR.string.rest_coming_next)
                } else {
                    "" // Empty text to reserve space
                },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.primary,
            modifier = Modifier.fillMaxWidth(),
        )

        Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

        // Exercise description always in same fixed position
        ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
    }
}

@Composable
fun StepRemainingComponent(
    modifier: Modifier,
    label: String,
    percentage: Float,
    thickness: Dp,
    periodType: RunningSessionStepType,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            maxLines = 1,
        )
        LinearProgressIndicator(
            progress = { percentage },
            modifier =
                Modifier
                    .height(thickness)
                    .fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = MaterialTheme.colorScheme.onBackground,
            strokeCap = StrokeCap.Butt,
            gapSize = 0.dp,
            drawStopIndicator = {},
        )
    }
}

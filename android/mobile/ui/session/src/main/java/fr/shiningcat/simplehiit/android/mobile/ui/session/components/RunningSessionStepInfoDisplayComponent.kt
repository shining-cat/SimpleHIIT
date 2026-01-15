/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
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
        // Top section - always reserve space for "coming next" to keep exercise description in fixed position
        Column {
            Text(
                text =
                    if (periodType == RunningSessionStepType.REST) {
                        stringResource(id = CommonResourcesR.string.coming_next)
                    } else {
                        "" // Empty text to reserve space
                    },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth(),
            )

            Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

            // Exercise description always in same fixed position
            ExerciseDescriptionComponent(exercise = exercise, side = exerciseSide)
        }

        // Flexible spacer pushes progress indicators to bottom while keeping top content at top
        Spacer(modifier = Modifier.weight(1f))

        // Bottom section - progress indicators
        Column {
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

            Spacer(modifier = Modifier.height(dimensionResource(CommonResourcesR.dimen.spacing_2)))

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
        }
    }
}

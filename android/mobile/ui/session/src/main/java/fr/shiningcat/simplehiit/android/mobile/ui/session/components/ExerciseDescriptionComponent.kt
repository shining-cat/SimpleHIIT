/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ExerciseDescriptionComponent(
    exercise: Exercise,
    side: ExerciseSide,
) {
    Column {
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        val exerciseName = stringResource(id = exerciseNameResMapper.map(exercise))
        Text(
            text = exerciseName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleSmall,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .basicMarquee(),
            maxLines = 2,
        )
        // Always reserve space for side mention to keep layout stable
        Text(
            text =
                when (side) {
                    ExerciseSide.LEFT -> stringResource(id = R.string.exercise_side_left)
                    ExerciseSide.RIGHT -> stringResource(id = R.string.exercise_side_right)
                    ExerciseSide.NONE -> "" // Empty string to reserve space
                },
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleMedium,
            color = MaterialTheme.colorScheme.secondary,
            modifier = Modifier.fillMaxWidth(),
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitMobileTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                ExerciseDescriptionComponent(Exercise.LungesBackKick, ExerciseSide.NONE)
                ExerciseDescriptionComponent(
                    Exercise.CrabKicks,
                    AsymmetricalExerciseSideOrder.FIRST.side,
                )
                ExerciseDescriptionComponent(
                    Exercise.CrabKicks,
                    AsymmetricalExerciseSideOrder.SECOND.side,
                )
            }
        }
    }
}

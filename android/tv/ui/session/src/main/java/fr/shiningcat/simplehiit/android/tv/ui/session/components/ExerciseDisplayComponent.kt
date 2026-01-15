/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.components.GifImage
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseGifMapper
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.sharedui.session.CountDown
import fr.shiningcat.simplehiit.sharedui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun ExerciseDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    isPaused: Boolean = false,
    hiitLogger: HiitLogger? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val exerciseGifResMapper = ExerciseGifMapper()
        val exerciseGifRes = exerciseGifResMapper.map(exercise)
        val exerciseDisplayNameMapper = ExerciseDisplayNameMapper()
        val exerciseDescriptionRes = exerciseDisplayNameMapper.map(exercise)
        GifImage(
            gifResId = exerciseGifRes,
            contentDescription = stringResource(exerciseDescriptionRes),
            mirrored = exerciseSide == AsymmetricalExerciseSideOrder.SECOND.side,
            paused = isPaused,
        )
        if (periodType == RunningSessionStepType.REST) {
            Text(
                text = stringResource(id = CommonResourcesR.string.coming_next),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.TopCenter)
                        .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_4)),
            )
        }
        if (countDown != null) {
            CountDownComponent(
                modifier = Modifier.fillMaxSize(),
                heightFraction = 0.33f,
                countDown = countDown,
                hiitLogger = hiitLogger,
            )
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ExerciseDisplayComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Box(modifier = Modifier.fillMaxSize()) {
                ExerciseDisplayComponent(
                    modifier = Modifier.fillMaxSize(),
                    exercise = Exercise.LungesSideToCurtsy,
                    periodType = RunningSessionStepType.REST,
                    exerciseSide = AsymmetricalExerciseSideOrder.SECOND.side,
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = .2f,
                            playBeep = true,
                        ),
                )
            }
        }
    }
}

package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.common.ui.components.GifImage
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseGifMapper
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun ExerciseDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    hiitLogger: HiitLogger? = null,
) {
    Box(
        modifier = modifier,
    ) {
        val exerciseGifResMapper = ExerciseGifMapper()
        val exerciseGifRes = exerciseGifResMapper.map(exercise)
        val exerciseDisplayNameMapper = ExerciseDisplayNameMapper()
        val exerciseDescriptionRes = exerciseDisplayNameMapper.map(exercise)
        GifImage(
            gifResId = exerciseGifRes,
            contentDescription = stringResource(exerciseDescriptionRes),
            mirrored = exerciseSide == AsymmetricalExerciseSideOrder.SECOND.side,
        )
        if (countDown != null) {
            CountDownComponent(
                baseSize = dimensionResource(R.dimen.countdown_size),
                countDown = countDown,
                hiitLogger = hiitLogger,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ExerciseDisplayComponentPreview() {
    SimpleHiitMobileTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            ExerciseDisplayComponent(
                modifier = Modifier.fillMaxSize(),
                exercise = Exercise.SquatBasic,
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

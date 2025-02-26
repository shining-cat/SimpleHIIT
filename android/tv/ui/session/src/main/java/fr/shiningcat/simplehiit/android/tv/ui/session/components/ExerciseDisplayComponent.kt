package fr.shiningcat.simplehiit.android.tv.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.components.GifImage
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.android.tv.ui.session.RunningSessionStepType
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseGifMapper
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun ExerciseDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    hiitLogger: HiitLogger? = null,
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val exerciseGifResMapper = ExerciseGifMapper()
        val exerciseGifRes = exerciseGifResMapper.map(exercise)
        GifImage(
            gifResId = exerciseGifRes,
            mirrored = exerciseSide == AsymmetricalExerciseSideOrder.SECOND.side,
        )
        if (periodType == RunningSessionStepType.REST) {
            Text(
                text = stringResource(id = R.string.coming_next),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .align(Alignment.BottomEnd),
            )
        }
        if (countDown != null) {
            CountDownComponent(
                size = 230.dp,
                countDown = countDown,
                hiitLogger = hiitLogger,
            )
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun ExerciseDisplayComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
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

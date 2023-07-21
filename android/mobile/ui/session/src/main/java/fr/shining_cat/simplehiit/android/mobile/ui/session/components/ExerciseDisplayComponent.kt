package fr.shining_cat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.GifImage
import fr.shining_cat.simplehiit.android.mobile.ui.session.CountDown
import fr.shining_cat.simplehiit.android.mobile.ui.session.RunningSessionStepType
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonresources.helpers.ExerciseGifMapper
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.domain.common.models.Exercise
import fr.shining_cat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun ExerciseDisplayComponent(
    modifier: Modifier = Modifier,
    exercise: Exercise,
    periodType: RunningSessionStepType,
    exerciseSide: ExerciseSide,
    countDown: CountDown? = null,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    Box(
        modifier = modifier
    ) {
        val exerciseGifResMapper = ExerciseGifMapper()
        val exerciseGifRes = exerciseGifResMapper.map(exercise)
        GifImage(
            gifResId = exerciseGifRes,
            mirrored = exerciseSide == AsymmetricalExerciseSideOrder.SECOND.side
        )
        if (periodType == RunningSessionStepType.REST) {
            Text(
                text = stringResource(id = R.string.coming_next),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        if (countDown != null) {
            CountDownComponent(
                size = 48.dp,
                countDown = countDown,
                hiitLogger = hiitLogger
            )
        }
    }
}
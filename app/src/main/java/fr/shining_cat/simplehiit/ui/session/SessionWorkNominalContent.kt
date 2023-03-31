package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseSide
import fr.shining_cat.simplehiit.ui.components.GifImage
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.helpers.ExerciseGifMapper
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionWorkNominalContent(viewState: SessionViewState.WorkNominal) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        val exerciseGifResMapper = ExerciseGifMapper()
        val exerciseGifRes = exerciseGifResMapper.map(viewState.currentExercise)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 8.dp, vertical = 24.dp)
        ) {
            GifImage(
                gifResId = exerciseGifRes,
                mirrored = viewState.side == AsymmetricalExerciseSideOrder.SECOND.side
            )
            if (viewState.countDown != null) {
                CountDownComponent(size = 48.dp, countDown = viewState.countDown)
            }
        }
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        val exerciseName = stringResource(id = exerciseNameResMapper.map(viewState.currentExercise))
        Text(
            text = exerciseName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        if(viewState.side != ExerciseSide.NONE){
            Text(
                text = when(viewState.side){
                    ExerciseSide.LEFT -> stringResource(id = R.string.exercise_side_left)
                    ExerciseSide.RIGHT -> stringResource(id = R.string.exercise_side_right)
                    else -> "" //unreachable
                },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        RemainingPercentageComponent(
            modifier = Modifier
                .padding(horizontal = 64.dp)
                .height(100.dp),
            label = stringResource(id = R.string.next_rest_in_s, viewState.exerciseRemainingTime),
            percentage = viewState.exerciseRemainingPercentage,
            thickness = 16.dp,
            bicolor = false
        )
        Spacer(modifier = Modifier.height(32.dp))
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
        Spacer(modifier = Modifier.height(32.dp))
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SessionWorkNominalContentPreview(
    @PreviewParameter(SessionWorkNominalContentPreviewParameterProvider::class) viewState: SessionViewState.WorkNominal
) {
    SimpleHiitTheme {
        SessionWorkNominalContent(
            viewState = viewState
        )
    }
}

internal class SessionWorkNominalContentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.WorkNominal> {
    override val values: Sequence<SessionViewState.WorkNominal>
        get() = sequenceOf(
            SessionViewState.WorkNominal(
                currentExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                exerciseRemainingTime = "25s",
                exerciseRemainingPercentage = .2f,
                sessionRemainingTime = "3mn 25s",
                sessionRemainingPercentage = .75f,
                countDown = null
            ),
            SessionViewState.WorkNominal(
                currentExercise = Exercise.LungesSideToCurtsy,
                side = AsymmetricalExerciseSideOrder.SECOND.side,
                exerciseRemainingTime = "3s",
                exerciseRemainingPercentage = .02f,
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
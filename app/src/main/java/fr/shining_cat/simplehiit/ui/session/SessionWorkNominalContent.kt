package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shining_cat.simplehiit.domain.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.ui.components.GifImage
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.helpers.ExerciseGifMapper
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionWorkNominalContent(viewState: SessionViewState.WorkNominal) {
    val exerciseGifResMapper = ExerciseGifMapper()
    val exerciseGifRes = exerciseGifResMapper.map(viewState.currentExercise)
    GifImage(gifResId = exerciseGifRes)
    val exerciseNameResMapper = ExerciseDisplayNameMapper()
    //TODO: do not forget to handle eventual side of exercise: display it and also mirror the gif
    val exerciseName = stringResource(id = exerciseNameResMapper.map(viewState.currentExercise))
    Text(text = exerciseName)
    Row {
        Text(text = "exerciseRemainingTime:")
        Text(text = viewState.exerciseRemainingTime.toString())
    }
    LinearProgressIndicator(progress = viewState.exerciseRemainingPercentage)
    Spacer(modifier = Modifier.height(32.dp))
    Row {
        Text(text = "sessionRemainingTime:")
        Text(text = viewState.sessionRemainingTime.toString())
    }
    LinearProgressIndicator(progress = viewState.sessionRemainingPercentage)
    Spacer(modifier = Modifier.height(32.dp))
    if(viewState.countDown != null) {
        Row {
            Text(text = "countdown:")
            Text(text = viewState.countDown.toString())
        }
        CircularProgressIndicator(progress = viewState.countDown.progress)
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
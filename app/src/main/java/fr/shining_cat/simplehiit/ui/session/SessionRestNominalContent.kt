package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.ui.components.GifImage
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.helpers.ExerciseGifMapper
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun SessionRestNominalContent(viewState: SessionViewState.RestNominal) {
    Text(text = stringResource(id = R.string.coming_next))
    val exerciseGifResMapper = ExerciseGifMapper()
    //TODO: do not forget to handle eventual side of exercise: display it and also mirror the gif
    val exerciseGifRes = exerciseGifResMapper.map(viewState.nextExercise)
    GifImage(gifResId = exerciseGifRes)
    val exerciseNameResMapper = ExerciseDisplayNameMapper()
    val exerciseName = stringResource(id = exerciseNameResMapper.map(viewState.nextExercise))
    Text(text = exerciseName)
    Row {
        Text(text = "restRemainingTime:")
        Text(text = viewState.restRemainingTime.toString())
    }
    LinearProgressIndicator(progress = viewState.restRemainingPercentage)
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
private fun SessionRestNominalContentPreview(
    @PreviewParameter(SessionRestNominalContentPreviewParameterProvider::class) viewState: SessionViewState.RestNominal
) {
    SimpleHiitTheme {
        SessionRestNominalContent(
            viewState = viewState
        )
    }
}

internal class SessionRestNominalContentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.RestNominal> {
    override val values: Sequence<SessionViewState.RestNominal>
        get() = sequenceOf(

        )
}
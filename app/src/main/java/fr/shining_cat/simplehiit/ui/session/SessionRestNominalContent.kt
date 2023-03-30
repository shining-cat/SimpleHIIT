package fr.shining_cat.simplehiit.ui.session

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.ui.components.GifImage
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.helpers.ExerciseGifMapper

@Composable
fun SessionRestNominalContent(screenViewState: SessionViewState.RestNominal) {
    Text(text = stringResource(id = R.string.coming_next))
    val exerciseGifResMapper = ExerciseGifMapper()
    //TODO: do not forget to handle eventual side of exercise: display it and also mirror the gif
    val exerciseGifRes = exerciseGifResMapper.map(screenViewState.nextExercise)
    GifImage(gifResId = exerciseGifRes)
    val exerciseNameResMapper = ExerciseDisplayNameMapper()
    val exerciseName = stringResource(id = exerciseNameResMapper.map(screenViewState.nextExercise))
    Text(text = exerciseName)
    Row {
        Text(text = "restRemainingTime:")
        Text(text = screenViewState.restRemainingTime.toString())
    }
    LinearProgressIndicator(progress = screenViewState.restRemainingPercentage)
    Spacer(modifier = Modifier.height(32.dp))
    Row {
        Text(text = "sessionRemainingTime:")
        Text(text = screenViewState.sessionRemainingTime.toString())
    }
    LinearProgressIndicator(progress = screenViewState.sessionRemainingPercentage)
    Spacer(modifier = Modifier.height(32.dp))
    if(screenViewState.countDown != null) {
        Row {
            Text(text = "countdown:")
            Text(text = screenViewState.countDown.toString())
        }
        CircularProgressIndicator(progress = screenViewState.countDown.progress)
    }

}
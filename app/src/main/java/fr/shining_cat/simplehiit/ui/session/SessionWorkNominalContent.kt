package fr.shining_cat.simplehiit.ui.session

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
import fr.shining_cat.simplehiit.ui.components.GifImage
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.helpers.ExerciseGifMapper

@Composable
fun SessionWorkNominalContent(screenViewState: SessionViewState.WorkNominal) {
    val exerciseGifResMapper = ExerciseGifMapper()
    val exerciseGifRes = exerciseGifResMapper.map(screenViewState.currentExercise)
    GifImage(gifResId = exerciseGifRes)
    val exerciseNameResMapper = ExerciseDisplayNameMapper()
    //TODO: do not forget to handle eventual side of exercise: display it and also mirror the gif
    val exerciseName = stringResource(id = exerciseNameResMapper.map(screenViewState.currentExercise))
    Text(text = exerciseName)
    Row {
        Text(text = "exerciseRemainingTime:")
        Text(text = screenViewState.exerciseRemainingTime.toString())
    }
    LinearProgressIndicator(progress = screenViewState.exerciseRemainingPercentage)
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
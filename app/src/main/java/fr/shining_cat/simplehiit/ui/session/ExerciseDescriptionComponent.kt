package fr.shining_cat.simplehiit.ui.session

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.domain.models.Exercise
import fr.shining_cat.simplehiit.domain.models.ExerciseSide
import fr.shining_cat.simplehiit.ui.helpers.ExerciseDisplayNameMapper
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun ExerciseDescriptionComponent(exercise: Exercise, side: ExerciseSide) {
    Column() {
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        val exerciseName = stringResource(id = exerciseNameResMapper.map(exercise))
        Text(
            text = exerciseName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth()
        )
        if (side != ExerciseSide.NONE) {
            Text(
                text = when (side) {
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
private fun CountDownCircularProgressPreview() {
    SimpleHiitTheme {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            ExerciseDescriptionComponent(Exercise.LungesBackKick, ExerciseSide.NONE)
            ExerciseDescriptionComponent(Exercise.CrabKicks, AsymmetricalExerciseSideOrder.FIRST.side)
            ExerciseDescriptionComponent(Exercise.CrabKicks, AsymmetricalExerciseSideOrder.SECOND.side)
        }
    }
}
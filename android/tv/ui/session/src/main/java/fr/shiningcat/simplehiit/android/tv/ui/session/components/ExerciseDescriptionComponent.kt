package fr.shiningcat.simplehiit.android.tv.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonresources.helpers.ExerciseDisplayNameMapper
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide

@Composable
fun ExerciseDescriptionComponent(
    exercise: Exercise,
    side: ExerciseSide,
) {
    Column {
        val exerciseNameResMapper = ExerciseDisplayNameMapper()
        val exerciseName = stringResource(id = exerciseNameResMapper.map(exercise))
        Text(
            text = exerciseName,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.titleLarge,
            modifier = Modifier.fillMaxWidth(),
        )
        if (side != ExerciseSide.NONE) {
            Text(
                text =
                    when (side) {
                        ExerciseSide.LEFT -> stringResource(id = R.string.exercise_side_left)
                        ExerciseSide.RIGHT -> stringResource(id = R.string.exercise_side_right)
                        else -> "" // unreachable
                    },
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth(),
            )
        }
    }
}

// Previews
@ExperimentalTvMaterial3Api
@Preview(
    name = "light mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                ExerciseDescriptionComponent(Exercise.LungesBackKick, ExerciseSide.NONE)
                ExerciseDescriptionComponent(
                    Exercise.CrabKicks,
                    AsymmetricalExerciseSideOrder.FIRST.side,
                )
                ExerciseDescriptionComponent(
                    Exercise.CrabKicks,
                    AsymmetricalExerciseSideOrder.SECOND.side,
                )
            }
        }
    }
}

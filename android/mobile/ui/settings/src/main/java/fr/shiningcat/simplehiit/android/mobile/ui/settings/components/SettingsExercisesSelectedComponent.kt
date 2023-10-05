package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import kotlin.math.roundToInt

@Composable
fun SettingsExercisesSelectedComponent(
    exerciseTypes: List<ExerciseTypeSelected>,
    onToggle: (ExerciseTypeSelected) -> Unit = {},
) {
    Column {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.selected_exercise_types_list_setting_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val itemHeight = 56.dp
        val numberOfColumns = 3
        val spacing = 8.dp
        val rowsCount = (exerciseTypes.size.toFloat() / numberOfColumns.toFloat()).roundToInt()
        val gridHeight = (itemHeight + spacing) * rowsCount
        LazyVerticalGrid(
            modifier = Modifier.height(gridHeight),
            columns = GridCells.Fixed(numberOfColumns),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false
        ) {
            items(exerciseTypes.size) {
                val exerciseTypeSelected = exerciseTypes[it]
                ToggleButton(
                    label = exerciseTypeSelected.type.name,
                    selected = exerciseTypeSelected.selected,
                    onToggle = { onToggle(exerciseTypeSelected) },
                    modifier = Modifier.height(itemHeight)
                )
            }
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsExercisesSelectedComponentPreview(
    @PreviewParameter(SettingsExercisesSelectedComponentPreviewParameterProvider::class) exercises: List<ExerciseTypeSelected>
) {
    SimpleHiitMobileTheme {
        Surface {
            SettingsExercisesSelectedComponent(exercises)
        }
    }
}

internal class SettingsExercisesSelectedComponentPreviewParameterProvider :
    PreviewParameterProvider<List<ExerciseTypeSelected>> {

    private val exerciseTypeSelectedAllTrue = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = true
        )
    }
    private val exerciseTypeSelectedAllFalse = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = false
        )
    }
    private val exerciseTypeSelectedMixed = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = (ExerciseType.values().indexOf(it) % 2 == 0)
        )
    }

    override val values: Sequence<List<ExerciseTypeSelected>>
        get() = sequenceOf(
            exerciseTypeSelectedAllTrue,
            exerciseTypeSelectedAllFalse,
            exerciseTypeSelectedMixed
        )
}

package fr.shining_cat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonToggle
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import kotlin.math.ceil

@Composable
fun SettingsExercisesSelectedComponent(
    exerciseTypes: List<ExerciseTypeSelected>,
    onToggle: (ExerciseTypeSelected) -> Unit = {},
    exerciseButtonsFocusRequesters: Map<String, FocusRequester> = emptyMap(),
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
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
        val spacing = 24.dp
        // this is to avoid the zoomed-in focused buttons of the first row to be clipped:
        val forcedTopMargin = 8.dp
        val rowsCount = ceil(exerciseTypes.size.toFloat() / numberOfColumns.toFloat()).toInt()
        // adding forcedMargin on top and bottom for symmetry, rather than a last spacing:
        val gridHeight = 2 * forcedTopMargin + (itemHeight) * rowsCount + spacing * (rowsCount - 1)
        TvLazyVerticalGrid(
            columns = TvGridCells.Fixed(numberOfColumns),
            modifier = Modifier.height(gridHeight),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false
        ) {
            items(exerciseTypes.size) {
                val exerciseTypeSelected = exerciseTypes[it]
                val exerciseTypeFocusRequester =
                    exerciseButtonsFocusRequesters[exerciseTypeSelected.type.name]
                ButtonToggle(
                    modifier = Modifier
                        .height(itemHeight)
                        // offset has to be applied to all items to avoid irregular spacing. It does not override the spacedBy of the LazyGrid:
                        .offset(y = forcedTopMargin)
                        .then(
                            if (exerciseTypeFocusRequester != null) Modifier.focusRequester(
                                exerciseTypeFocusRequester
                            ) else Modifier
                        ),
                    label = exerciseTypeSelected.type.name,
                    selected = exerciseTypeSelected.selected,
                    onToggle = { onToggle(exerciseTypeSelected) }
                )
            }
        }
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
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
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
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

    private fun tripleList(): List<ExerciseTypeSelected> {
        val tripleList = mutableListOf<ExerciseTypeSelected>()
        tripleList.addAll(exerciseTypeSelectedAllTrue)
        tripleList.addAll(exerciseTypeSelectedAllFalse)
//        tripleList.addAll(exerciseTypeSelectedMixed)
        return tripleList
    }

    override val values: Sequence<List<ExerciseTypeSelected>>
        get() = sequenceOf(
            tripleList(),
            exerciseTypeSelectedAllTrue,
            exerciseTypeSelectedAllFalse,
            exerciseTypeSelectedMixed
        )
}

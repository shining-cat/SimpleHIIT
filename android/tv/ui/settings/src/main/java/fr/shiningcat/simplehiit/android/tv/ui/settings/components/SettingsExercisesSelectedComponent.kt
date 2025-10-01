package fr.shiningcat.simplehiit.android.tv.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.times
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonToggle
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import kotlin.math.ceil
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsExercisesSelectedComponent(
    exerciseTypes: List<ExerciseTypeSelected>,
    onToggle: (ExerciseTypeSelected) -> Unit = {},
    exerciseButtonsFocusRequesters: Map<String, FocusRequester> = emptyMap(),
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Column {
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1)),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = CommonResourcesR.string.selected_exercise_types_list_setting_label),
        )
        val itemHeight =
            adaptDpToFontScale(dimensionResource(R.dimen.settings_select_exercise_height))
        val numberOfColumns = 3
        val spacing = dimensionResource(CommonResourcesR.dimen.spacing_3)
        // this is to avoid the zoomed-in focused buttons of the first row to be clipped:
        val forcedTopMargin = dimensionResource(CommonResourcesR.dimen.spacing_1)
        val rowsCount = ceil(exerciseTypes.size.toFloat() / numberOfColumns.toFloat()).toInt()
        // adding forcedMargin on top and bottom for symmetry, rather than a last spacing:
        val gridHeight = 2 * forcedTopMargin + (itemHeight) * rowsCount + spacing * (rowsCount - 1)
        LazyVerticalGrid(
            columns = GridCells.Fixed(numberOfColumns),
            modifier = Modifier.height(gridHeight),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false,
        ) {
            items(exerciseTypes.size) {
                val exerciseTypeSelected = exerciseTypes[it]
                val exerciseTypeFocusRequester =
                    exerciseButtonsFocusRequesters[exerciseTypeSelected.type.name]
                ButtonToggle(
                    modifier =
                        Modifier
                            .height(itemHeight)
                            // offset has to be applied to all items to avoid irregular spacing. It does not override the spacedBy of the LazyGrid:
                            .offset(y = forcedTopMargin)
                            .then(
                                if (exerciseTypeFocusRequester != null) {
                                    Modifier.focusRequester(
                                        exerciseTypeFocusRequester,
                                    )
                                } else {
                                    Modifier
                                },
                            ),
                    label = exerciseTypeSelected.type.name,
                    selected = exerciseTypeSelected.selected,
                    onToggle = { onToggle(exerciseTypeSelected) },
                )
            }
        }
    }
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun SettingsExercisesSelectedComponentPreview(
    @PreviewParameter(SettingsExercisesSelectedComponentPreviewParameterProvider::class) exercises: List<ExerciseTypeSelected>,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsExercisesSelectedComponent(exercises)
        }
    }
}

internal class SettingsExercisesSelectedComponentPreviewParameterProvider : PreviewParameterProvider<List<ExerciseTypeSelected>> {
    private val exerciseTypeSelectedAllTrue =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = true,
            )
        }
    private val exerciseTypeSelectedAllFalse =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = false,
            )
        }
    private val exerciseTypeSelectedMixed =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = (ExerciseType.entries.indexOf(it) % 2 == 0),
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
        get() =
            sequenceOf(
                tripleList(),
                exerciseTypeSelectedAllTrue,
                exerciseTypeSelectedAllFalse,
                exerciseTypeSelectedMixed,
            )
}

package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.toggleButtonLostWidthDp
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
            text = stringResource(id = R.string.selected_exercise_types_list_setting_label),
        )
        Spacer(modifier = Modifier.height(8.dp))
        val availableWidthPix = LocalWindowInfo.current.containerSize.width
        val spacingDp = 8.dp
        val density = LocalDensity.current
        val spacingPix = with(density) { spacingDp.toPx() }
        val toggleButtonLostWidthPix = with(density) { toggleButtonLostWidthDp.toPx() }
        val oneThirdColumnAvailableWidth =
            (availableWidthPix - 2 * spacingPix) / 3f - toggleButtonLostWidthPix
        val use3Columns =
            exerciseTypes.all {
                fitsOnXLines(
                    textLayoutInfo =
                        TextLayoutInfo(
                            text = it.type.name,
                            style = MaterialTheme.typography.labelMedium,
                        ),
                    numberOfLines = 1,
                    availableWidthPx = oneThirdColumnAvailableWidth.roundToInt(),
                )
            }
        val numberOfColumns = if (use3Columns) 3 else 2
        val itemHeightDp = 56.dp
        val rowsCount = (exerciseTypes.size.toFloat() / numberOfColumns.toFloat()).roundToInt()
        val gridHeightDp = (itemHeightDp + spacingDp) * rowsCount
        LazyVerticalGrid(
            modifier = Modifier.height(gridHeightDp),
            columns = GridCells.Fixed(numberOfColumns),
            verticalArrangement = Arrangement.spacedBy(spacingDp),
            horizontalArrangement = Arrangement.spacedBy(spacingDp),
            userScrollEnabled = false,
        ) {
            items(exerciseTypes.size) {
                val exerciseTypeSelected = exerciseTypes[it]
                ToggleButton(
                    label = exerciseTypeSelected.type.name,
                    labelStyle = MaterialTheme.typography.labelMedium,
                    selected = exerciseTypeSelected.selected,
                    onToggle = { onToggle(exerciseTypeSelected) },
                    modifier = Modifier.height(itemHeightDp),
                )
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SettingsExercisesSelectedComponentPreview(
    @PreviewParameter(SettingsExercisesSelectedComponentPreviewParameterProvider::class) exercises: List<ExerciseTypeSelected>,
) {
    SimpleHiitMobileTheme {
        Surface {
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

    override val values: Sequence<List<ExerciseTypeSelected>>
        get() =
            sequenceOf(
                exerciseTypeSelectedAllTrue,
                exerciseTypeSelectedAllFalse,
                exerciseTypeSelectedMixed,
            )
}

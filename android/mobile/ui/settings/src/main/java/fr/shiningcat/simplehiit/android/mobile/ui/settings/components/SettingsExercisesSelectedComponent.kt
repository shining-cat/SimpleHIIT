package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.getToggleButtonLostWidthPix
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.settings.R
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import kotlin.math.roundToInt
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsExercisesSelectedComponent(
    exerciseTypes: List<ExerciseTypeSelected>,
    onToggle: (ExerciseTypeSelected) -> Unit = {},
) {
    BoxWithConstraints {
        val availableWidthDp = maxWidth
        val density = LocalDensity.current
        val availableWidthPix = with(density) { availableWidthDp.toPx() }

        Column {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth().padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1)),
                style = MaterialTheme.typography.headlineMedium,
                text = stringResource(id = CommonResourcesR.string.selected_exercise_types_list_setting_label),
            )

            val spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_1)
            val spacingPix = with(density) { spacingDp.toPx() }
            val toggleButtonLostWidthPix = getToggleButtonLostWidthPix()

            // Try 3 columns first
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
                        availableWidthPix = oneThirdColumnAvailableWidth.roundToInt(),
                    )
                }

            // If 3 columns doesn't work, try 2 columns
            val numberOfColumns =
                if (use3Columns) {
                    3
                } else {
                    val oneHalfColumnAvailableWidth =
                        (availableWidthPix - 1 * spacingPix) / 2f - toggleButtonLostWidthPix
                    val use2Columns =
                        exerciseTypes.all {
                            fitsOnXLines(
                                textLayoutInfo =
                                    TextLayoutInfo(
                                        text = it.type.name,
                                        style = MaterialTheme.typography.labelMedium,
                                    ),
                                numberOfLines = 1,
                                availableWidthPix = oneHalfColumnAvailableWidth.roundToInt(),
                            )
                        }
                    if (use2Columns) 2 else 1
                }

            val itemHeightDp = dimensionResource(R.dimen.exercise_select_button_height)
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
}

// Previews
@PreviewLightDark
@PreviewFontScale
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

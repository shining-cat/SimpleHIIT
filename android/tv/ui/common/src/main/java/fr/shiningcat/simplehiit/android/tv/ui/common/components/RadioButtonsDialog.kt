package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.RadioButton
import androidx.tv.material3.RadioButtonDefaults
import androidx.tv.material3.SelectableSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R as TvCommonR
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * A dialog that presents a list of options with radio buttons for selection.
 *
 * This component provides a standardized dialog for selecting one option from a list,
 * using radio buttons for visual indication of the current selection. The dialog includes
 * a title, a list of selectable options, an explanation text, and action buttons.
 *
 * @param title The title text to display at the top of the dialog
 * @param options The list of option labels to display
 * @param selectedIndex The index of the currently selected option (0-based)
 * @param explanationText Text displayed below the options to provide additional context
 * @param onOptionSelected Callback invoked when an option is selected, providing the index of the selected option
 * @param onSelectionChanged Optional callback invoked when the selection changes (before save), providing the new index
 * @param onFocusChanged Optional callback invoked when focus moves to a different option, providing the new index
 * @param onCancel Callback invoked when the cancel button is clicked or dialog is dismissed
 */
@Composable
fun RadioButtonsDialog(
    title: String,
    options: List<String>,
    selectedIndex: Int,
    explanationText: String,
    onOptionSelected: (Int) -> Unit,
    onSelectionChanged: ((Int) -> Unit)? = null,
    onFocusChanged: ((Int) -> Unit)? = null,
    onCancel: () -> Unit,
) {
    var currentSelectedIndex by remember { mutableIntStateOf(selectedIndex) }

    DialogContentLayout(
        onDismissRequest = onCancel,
        title = title,
        content = {
            Column(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(CommonResourcesR.dimen.spacing_2))
                        .selectableGroup(),
            ) {
                options.forEachIndexed { index, label ->
                    RadioButtonOption(
                        label = label,
                        selected = currentSelectedIndex == index,
                        onSelected = {
                            currentSelectedIndex = index
                            onSelectionChanged?.invoke(index)
                        },
                        onFocused = {
                            onFocusChanged?.invoke(index)
                        },
                    )
                }

                Text(
                    text = explanationText,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    textAlign = TextAlign.Center,
                )
            }
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_3)),
            ) {
                ButtonBordered(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(TvCommonR.dimen.dialog_standard_button_height)))
                            .weight(1f),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = onCancel,
                    label = stringResource(id = CommonResourcesR.string.cancel_button_label),
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(TvCommonR.dimen.dialog_standard_button_height)))
                            .weight(1f),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = { onOptionSelected(currentSelectedIndex) },
                    label = stringResource(id = CommonResourcesR.string.save_settings_button_label),
                )
            }
        },
    )
}

@Composable
private fun RadioButtonOption(
    label: String,
    selected: Boolean,
    onSelected: () -> Unit,
    onFocused: (() -> Unit)? = null,
) {
    val verticalPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)
    val horizontalPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

    Surface(
        onClick = onSelected,
        selected = selected,
        modifier =
            Modifier
                .fillMaxWidth()
                .padding(vertical = verticalPadding)
                .onFocusChanged { focusState ->
                    if (focusState.isFocused) {
                        onFocused?.invoke()
                    }
                },
        colors =
            SelectableSurfaceDefaults.colors(
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.secondary,
                selectedContainerColor = Color.Transparent,
                selectedContentColor = MaterialTheme.colorScheme.primary,
                focusedSelectedContainerColor = Color.Transparent,
                focusedSelectedContentColor = MaterialTheme.colorScheme.secondary,
            ),
        scale =
            SelectableSurfaceDefaults.scale(
                focusedScale = 1f,
            ),
    ) {
        Row(
            modifier = Modifier.padding(horizontalPadding),
        ) {
            RadioButton(
                selected = selected,
                onClick = null,
                colors =
                    RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.primary,
                    ),
            )
            Text(
                text = label,
                style = MaterialTheme.typography.bodyLarge,
                modifier = Modifier.padding(start = horizontalPadding),
            )
        }
    }
}

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.IconButtonDefaults.MediumIconSize
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * Helper function to calculate the width consumed by icon and padding in a ButtonFilled when selected.
 *
 * This calculation is useful for layout computations where you need to know how much horizontal space
 * is occupied by the button's internal components (icon, spacing, and padding).
 *
 * @return The total width in pixels consumed by:
 *         - Leading icon size (24dp)
 *         - Icon spacing (8dp)
 *         - Horizontal padding (32dp total: 16dp start + 16dp end)
 *         Total: 64dp converted to pixels
 */
@Composable
fun getToggleButtonLostWidthPix(hasIcon: Boolean): Float {
    val density = LocalDensity.current
    return with(density) {
        // ButtonFilled internal width components (when selected, i.e. with leading icon):
        // - Leading icon size: 24dp (MediumIconSize)
        // - Icon spacing: 8dp (ButtonDefaults.IconSpacing)
        // - Start padding: 16dp (spacing_2)
        // - End padding: 16dp (spacing_2)
        // Total lost width = 24 + 8 + 16 + 16 = 64dp
        val iconSize = if (hasIcon) MediumIconSize else 0.dp // 24dp
        val iconSpacing = if (hasIcon) ButtonDefaults.IconSpacing else 0.dp // 8dp
        val horizontalPadding = dimensionResource(CommonResourcesR.dimen.spacing_2) * 2 // 16dp * 2 = 32dp
        (iconSize + iconSpacing + horizontalPadding).toPx()
    }
}

/**
 * A Composable function that creates a toggle button that switches appearance based on selection state.
 *
 * When selected, displays as a ButtonFilled with a checkmark icon.
 * When not selected, displays as a ButtonBordered with no icon.
 * This provides clear visual feedback for toggleable options.
 *
 * @param modifier The modifier to be applied to the button.
 * @param fillWidth Whether the button should try to occupy the full width of its parent.
 *                  If true, the inner Row will use `Modifier.fillMaxWidth()`.
 *                  As a general rule, set this to true if the button is constrained horizontally, false otherwise, to preserve proper centering.
 * @param fillHeight Whether the button should try to occupy the full height of its parent.
 *                   If true, the inner Row will use `Modifier.fillMaxHeight()`.
 *                   As a general rule, set this to true if the button is constrained vertically, false otherwise, to preserve proper centering.
 * @param label The text to display on the button.
 * @param selected The current selection state. When true, shows ButtonFilled with checkmark; when false, shows ButtonBordered.
 * @param reserveIconSpace When true, reserves space for an icon even when not selected, ensuring consistent width between states.
 * @param onToggle Called when this button is clicked to toggle its state.
 */
@Composable
fun ButtonToggle(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    label: String,
    selected: Boolean,
    reserveIconSpace: Boolean = false,
    onToggle: () -> Unit,
) {
    if (selected) {
        ButtonFilled(
            modifier = modifier,
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            onClick = { onToggle() },
            label = label,
            icon = Icons.Filled.Done,
            iconContentDescription = CommonResourcesR.string.active,
            reserveIconSpace = reserveIconSpace,
        )
    } else {
        ButtonBordered(
            modifier = modifier,
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            onClick = { onToggle() },
            label = label,
            reserveIconSpace = reserveIconSpace,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonTogglePreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.width(adaptDpToFontScale(300.dp)),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
            ) {
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    fillHeight = true,
                    label = "I'm selected",
                    selected = true,
                    onToggle = {},
                )
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    fillHeight = true,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
                ButtonToggle(
                    // causing a truncation
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                            .width(adaptDpToFontScale(128.dp)),
                    fillHeight = true,
                    fillWidth = true,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
            }
        }
    }
}

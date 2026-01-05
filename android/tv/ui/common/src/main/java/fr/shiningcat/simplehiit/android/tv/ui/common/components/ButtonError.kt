package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * A Composable function that creates an error button with an optional icon and label.
 *
 * This button uses error color scheme and has the same structure as ButtonFilled.
 * The content (icon and label) is arranged in a Row and centered.
 * The button can be configured to fill the available width and/or height.
 *
 * @param modifier The modifier to be applied to the button.
 * @param fillWidth Whether the button should try to occupy the full width of its parent.
 *                  If true, the inner Row will use `Modifier.fillMaxWidth()`.
 *                  As a general rule, set this to true if the button is constrained horizontally, false otherwise, to preserve proper centering.
 * @param fillHeight Whether the button should try to occupy the full height of its parent.
 *                   If true, the inner Row will use `Modifier.fillMaxHeight()`.
 *                   As a general rule, set this to true if the button is constrained vertically, false otherwise, to preserve proper centering.
 * @param onClick Called when this button is clicked.
 * @param label The text to display on the button. If null, no text is displayed.
 * @param icon The icon to display on the button. If null, no icon is displayed.
 * @param iconContentDescription The content description resource ID for the icon. Defaults to -1 (no description).
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param reserveIconSpace When true, reserves space for an icon even when icon is null, ensuring consistent width with buttons that have icons.
 */
@Composable
fun ButtonError(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    onClick: () -> Unit = {},
    label: String? = null,
    icon: ImageVector? = null,
    @StringRes
    iconContentDescription: Int = -1,
    enabled: Boolean = true,
    reserveIconSpace: Boolean = false,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
        colors =
            ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.error,
                focusedContentColor = MaterialTheme.colorScheme.secondary,
                pressedContainerColor = MaterialTheme.colorScheme.error,
                pressedContentColor = MaterialTheme.colorScheme.onError,
                disabledContainerColor = MaterialTheme.colorScheme.error.copy(alpha = .6f),
                disabledContentColor = MaterialTheme.colorScheme.onError,
            ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        // Set to none, ButtonContentLayout will handle padding
        contentPadding = PaddingValues(),
    ) {
        ButtonContentLayout(
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            label = label,
            icon = icon,
            iconContentDescription = iconContentDescription,
            reserveIconSpace = reserveIconSpace,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonErrorPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier =
                    Modifier
                        .width(adaptDpToFontScale(350.dp))
                        .padding(dimensionResource(CommonResourcesR.dimen.spacing_2))
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
            ) {
                Text(text = "Default (wrap content):")
                ButtonError(
                    label = "Error",
                )

                Text(text = "fillWidth = true:")
                ButtonError(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    label = "Error Message",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cross_x),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "fillHeight = true (fixed width):")
                ButtonError(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Error",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cross_x),
                    fillHeight = true,
                )

                Text(text = "fillWidth & fillHeight = true:")
                ButtonError(
                    modifier = Modifier.height(adaptDpToFontScale(64.dp)),
                    label = "Critical Error",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cross_x),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "Disabled:")
                ButtonError(
                    label = "Disabled Error",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cross_x),
                    enabled = false,
                    fillWidth = true,
                )

                Text(text = "Icon only, wrap:")
                ButtonError(
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cross_x),
                )

                Text(text = "Label only:")
                ButtonError(
                    label = "No Icon Error",
                    fillWidth = true,
                )

                Text(text = "With icon space reserved:")
                ButtonError(
                    label = "Reserved Space",
                    reserveIconSpace = true,
                    fillWidth = true,
                )
            }
        }
    }
}

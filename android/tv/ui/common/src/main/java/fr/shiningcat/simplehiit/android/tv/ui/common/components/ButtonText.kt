package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * A Composable function that creates a text button with an optional icon and label.
 *
 * This button has a transparent background and shows focus through background/content color changes.
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
 * @param iconContentDescription The content description resource ID for the icon. Null if no description.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param reserveIconSpace When true, reserves space for an icon even when icon is null, ensuring consistent width with buttons that have icons.
 */
@Composable
fun ButtonText(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    onClick: () -> Unit = {},
    label: String? = null,
    icon: ImageVector? = null,
    @StringRes
    iconContentDescription: Int? = null,
    enabled: Boolean = true,
    reserveIconSpace: Boolean = false,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
        colors = transparentButtonTextColors(),
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

@Composable
fun transparentButtonTextColors() =
    ButtonDefaults.colors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onSurface,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedContentColor = MaterialTheme.colorScheme.secondary,
        pressedContainerColor = MaterialTheme.colorScheme.primary,
        pressedContentColor = MaterialTheme.colorScheme.secondary,
        disabledContainerColor = Color.Transparent,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f),
    )

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonTextPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier =
                    Modifier
                        .width(adaptDpToFontScale(400.dp))
                        .height(adaptDpToFontScale(600.dp))
                        .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(text = "Default (wrap content):")
                ButtonText(
                    label = "Short",
                )
                ButtonText(
                    label = "Longer Label",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                )

                Text(text = "fillWidth = true:")
                ButtonText(
                    label = "Fill Width",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                )
                ButtonText(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Fill Width (Fixed 200dp)",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                )

                Text(text = "fillHeight = true (fixed height):")
                ButtonText(
                    modifier = Modifier.height(adaptDpToFontScale(100.dp)),
                    label = "Fill Height (100dp)",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillHeight = true,
                )

                Text(text = "fillWidth & fillHeight:")
                ButtonText(
                    modifier =
                        Modifier
                            .width(adaptDpToFontScale(250.dp))
                            .height(adaptDpToFontScale(120.dp)),
                    label = "Fill Both (250x120)",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "Disabled:")
                ButtonText(
                    label = "Disabled",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    enabled = false,
                )
                ButtonText(
                    label = "Disabled Fill Width",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    enabled = false,
                    fillWidth = true,
                )

                Text(text = "Icon only:")
                ButtonText(
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                )

                Text(text = "With icon space reserved:")
                ButtonText(
                    label = "Reserved Space",
                    reserveIconSpace = true,
                    fillWidth = true,
                )
            }
        }
    }
}

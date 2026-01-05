package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * A Composable function that creates an outlined button with an optional icon and label.
 *
 * This button has a border that changes color based on its state (default, focused, pressed, disabled).
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
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonBordered(
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
    OutlinedButton(
        modifier =
            modifier
                // Minimal padding to ensure border visibility
                .padding(dimensionResource(R.dimen.border_thin_stroke)),
        onClick = onClick,
        enabled = enabled,
        colors =
            ButtonDefaults.colors(
                // this is mostly to allow for a more visible focus state
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onBackground,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.secondary,
                pressedContainerColor = MaterialTheme.colorScheme.primary,
                pressedContentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
            ),
        shape = ButtonDefaults.shape(MaterialTheme.shapes.small),
        border =
            ButtonDefaults.border(
                border =
                    Border(
                        BorderStroke(
                            width = dimensionResource(CommonResourcesR.dimen.stroke_025),
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                focusedBorder =
                    Border(
                        BorderStroke(
                            width = dimensionResource(CommonResourcesR.dimen.stroke_025),
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                pressedBorder =
                    Border(
                        BorderStroke(
                            width = dimensionResource(CommonResourcesR.dimen.stroke_025),
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                disabledBorder =
                    Border(
                        BorderStroke(
                            width = dimensionResource(CommonResourcesR.dimen.stroke_025),
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = .6f),
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
            ),
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
private fun BorderedButtonPreview() {
    SimpleHiitTvTheme {
        Surface(
            shape = MaterialTheme.shapes.extraSmall,
            modifier = Modifier.padding(dimensionResource(CommonResourcesR.dimen.spacing_2)),
        ) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Default (wrap content):")
                ButtonBordered(
                    label = "Short Label",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    onClick = {},
                )

                Text("fillWidth = true (fixed height):")
                ButtonBordered(
                    label = "Fill Width",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    onClick = {},
                )

                Text("fillHeight = true (fixed width):")
                ButtonBordered(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Fill Height",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillHeight = true,
                    fillWidth = true,
                    onClick = {},
                )

                Text("fillWidth & fillHeight (explicit size):")
                ButtonBordered(
                    modifier =
                        Modifier
                            .width(adaptDpToFontScale(250.dp))
                            .height(adaptDpToFontScale(64.dp)),
                    label = "Fill Both",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = {},
                )

                Text("Wrapped content, long text (explicit height):")
                ButtonBordered(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    label = "This is a very long label that should wrap or ellipsis",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    onClick = {},
                )

                Text("Wrapped content, icon only (explicit height):")
                ButtonBordered(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    onClick = {},
                )

                Text("fillWidth=true, no explicit height (should take parent width, wrap height):")
                ButtonBordered(
                    modifier = Modifier.width(adaptDpToFontScale(300.dp)),
                    label = "Label for fillWidth",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    onClick = {},
                )
            }
        }
    }
}

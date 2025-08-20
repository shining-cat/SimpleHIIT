package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

/**
 * A Composable function that creates an outlined button with an optional icon and label.
 *
 * This button has a border that changes color based on its state (default, focused, pressed, disabled).
 * The content (icon and label) is arranged in a Row and centered.
 * The button can be configured to fill the available width and/or height.
 *
 * @param modifier The modifier to be applied to the button.
 * @param label The text to display on the button. If null, no text is displayed.
 * @param icon The icon to display on the button. If null, no icon is displayed.
 * @param enabled Controls the enabled state of the button. When `false`, this button will not be clickable.
 * @param fillWidth Whether the button should try to occupy the full width of its parent.
 *                  If true, the inner Row will use `Modifier.fillMaxWidth()`.
 *                  as a general rule, set this to true if the button is constrained horizontally, false otherwise, to preserve proper centering
 * @param fillHeight Whether the button should try to occupy the full height of its parent.
 *                   If true, the inner Row will use `Modifier.fillMaxHeight()`.
 *                  as a general rule, set this to true if the button is constrained vertically, false otherwise, to preserve proper centering
 * @param maxLines The maximum number of lines to be used for the label.
 * @param textAlign The alignment of the label text.
 * @param onClick Called when this button is clicked.
 */
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonBordered(
    modifier: Modifier = Modifier,
    label: String? = null,
    icon: ImageVector? = null,
    enabled: Boolean = true,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    maxLines: Int = 1,
    textAlign: TextAlign = TextAlign.Center,
    onClick: () -> Unit,
) {
    OutlinedButton(
        modifier =
            modifier
                // Minimal padding to ensure border visibility
                .padding(1.dp),
        onClick = onClick,
        enabled = enabled,
        colors =
            ButtonDefaults.colors(
                // this is mostly to allow for a more visible focus state
                containerColor = Color.Transparent,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.onSurface,
                pressedContainerColor = MaterialTheme.colorScheme.primary,
                pressedContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f),
            ),
        shape = ButtonDefaults.shape(MaterialTheme.shapes.small),
        border =
            ButtonDefaults.border(
                border =
                    Border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                focusedBorder =
                    Border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.secondary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                pressedBorder =
                    Border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.primary,
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
                disabledBorder =
                    Border(
                        BorderStroke(
                            width = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f),
                        ),
                        shape = MaterialTheme.shapes.small,
                    ),
            ),
    ) {
        val rowModifier =
            Modifier
                .then(
                    if (fillWidth) {
                        Modifier.fillMaxWidth()
                    } else {
                        Modifier
                    },
                )
                .then(
                    if (fillHeight) {
                        Modifier.fillMaxHeight()
                    } else {
                        Modifier
                    },
                )
                .padding(horizontal = 8.dp, vertical = 4.dp)

        Row(
            modifier = rowModifier,
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            if (icon != null) {
                Icon(
                    modifier = Modifier.size(adaptDpToFontScale(24.dp)),
                    imageVector = icon,
                    contentDescription = null,
                )
                if (label != null) {
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
            if (label != null) {
                Text(
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                    overflow = TextOverflow.Ellipsis,
                    maxLines = maxLines,
                    textAlign = textAlign,
                )
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun BorderedButtonPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall, modifier = Modifier.padding(16.dp)) {
            Column(
                modifier = Modifier.verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text("Default (wrap content):")
                ButtonBordered(
                    label = "Short Label",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    onClick = {},
                )

                Text("fillWidth = true (fixed height):")
                ButtonBordered(
                    modifier = Modifier.height(adaptDpToFontScale(48.dp)),
                    label = "Fill Width",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    fillWidth = true,
                    onClick = {},
                )

                Text("fillHeight = true (fixed width):")
                ButtonBordered(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Fill Height",
                    icon = ImageVector.vectorResource(R.drawable.cog),
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
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = {},
                )

                Text("Wrapped content, long text (explicit height):")
                ButtonBordered(
                    modifier = Modifier.height(adaptDpToFontScale(48.dp)),
                    label = "This is a very long label that should wrap or ellipsis",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    onClick = {},
                )

                Text("Wrapped content, icon only (explicit height):")
                ButtonBordered(
                    modifier = Modifier.height(adaptDpToFontScale(48.dp)),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    onClick = {},
                )

                Text("fillWidth=true, no explicit height (should take parent width, wrap height):")
                ButtonBordered(
                    modifier = Modifier.width(adaptDpToFontScale(300.dp)),
                    label = "Label for fillWidth",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    fillWidth = true,
                    onClick = {},
                )
            }
        }
    }
}

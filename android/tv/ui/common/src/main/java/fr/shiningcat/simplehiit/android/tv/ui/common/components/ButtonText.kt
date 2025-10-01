package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButtonDefaults.MediumIconSize
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

/**
 * Composable function that displays a button with text and an optional icon.
 *
 * @param modifier The modifier to be applied to the button.
 * @param onClick The callback to be invoked when the button is clicked.
 * @param label The text to be displayed on the button.
 * @param icon The drawable resource ID for the icon to be displayed on the button. Defaults to -1 (no icon).
 * @param iconContentDescription The string resource ID for the content description of the icon. Defaults to -1.
 * @param enabled Whether the button is enabled or not. Defaults to true.
 * @param fillWidth Whether the button should fill the available width. Defaults to false.
 * @param fillHeight Whether the button should fill the available height. Defaults to false.
 */
@Composable
fun ButtonText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String,
    @DrawableRes
    icon: Int = -1,
    @StringRes
    iconContentDescription: Int = -1,
    enabled: Boolean = true,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = transparentButtonTextColors(),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        // Set to zero, inner Row handles padding
        contentPadding = PaddingValues(),
    ) {
        val rowModifier =
            Modifier
                .then(if (fillWidth) Modifier.fillMaxWidth() else Modifier)
                .then(if (fillHeight) Modifier.fillMaxHeight() else Modifier)
                .padding(
                    horizontal = dimensionResource(R.dimen.spacing_2),
                    vertical = dimensionResource(R.dimen.spacing_1),
                )

        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != -1) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription =
                        if (iconContentDescription != -1) {
                            stringResource(id = iconContentDescription)
                        } else {
                            ""
                        },
                    modifier = Modifier.size(adaptDpToFontScale(MediumIconSize)),
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center,
            )
        }
    }
}

@Composable
fun transparentButtonTextColors() =
    ButtonDefaults.colors(
        containerColor = Color.Transparent,
        contentColor = MaterialTheme.colorScheme.onPrimary,
        focusedContainerColor = MaterialTheme.colorScheme.surface,
        focusedContentColor = MaterialTheme.colorScheme.onSurface,
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
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2)),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(color = Color.Blue, text = "Default (wrap content):")
                ButtonText(
                    label = "Short",
                )
                ButtonText(
                    label = "Longer Label Button Text",
                    icon = R.drawable.cog,
                )

                Text(color = Color.Blue, text = "fillWidth = true:")
                ButtonText(
                    label = "Fill Width",
                    icon = R.drawable.cog,
                    fillWidth = true,
                )
                ButtonText(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Fill Width (Fixed 200dp)",
                    icon = R.drawable.cog,
                    fillWidth = true,
                )

                Text(
                    color = Color.Blue,
                    text = "fillHeight = true (needs fixed height on ButtonText):",
                )
                ButtonText(
                    modifier = Modifier.height(adaptDpToFontScale(100.dp)),
                    label = "Fill Height (100dp)",
                    icon = R.drawable.cog,
                    fillHeight = true,
                )

                Text(
                    color = Color.Blue,
                    text = "fillWidth & fillHeight (needs fixed size on ButtonText):",
                )
                ButtonText(
                    modifier =
                        Modifier
                            .width(adaptDpToFontScale(250.dp))
                            .height(adaptDpToFontScale(120.dp)),
                    label = "Fill Both (250x120)",
                    icon = R.drawable.cog,
                    fillWidth = true,
                    fillHeight = true,
                )
                ButtonText(
                    modifier = Modifier.fillMaxWidth(),
                    label = "ButtonText fillMaxWidth()",
                    icon = R.drawable.cog,
                    fillWidth = true,
                )

                Text(color = Color.Blue, text = "Disabled:")
                ButtonText(
                    label = "Disabled",
                    icon = R.drawable.cog,
                    enabled = false,
                )
                ButtonText(
                    label = "Disabled Fill Width",
                    icon = R.drawable.cog,
                    enabled = false,
                    fillWidth = true,
                )
            }
        }
    }
}

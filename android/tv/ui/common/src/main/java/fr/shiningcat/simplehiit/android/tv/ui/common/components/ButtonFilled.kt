package fr.shiningcat.simplehiit.android.tv.ui.common.components

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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
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
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun ButtonFilled(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    onClick: () -> Unit = {},
    label: String? = null,
    icon: ImageVector? = null,
    @StringRes
    iconContentDescription: Int = -1,
    accentColor: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
        colors =
            ButtonDefaults.colors(
                containerColor = if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                contentColor = if (accentColor) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
                focusedContentColor = if (accentColor) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
                pressedContainerColor = MaterialTheme.colorScheme.primary,
                pressedContentColor = MaterialTheme.colorScheme.secondary,
                disabledContainerColor =
                    (if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary).copy(
                        alpha = .6f,
                    ),
                disabledContentColor = MaterialTheme.colorScheme.onPrimary,
            ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        // Set to none, inner Row will handle padding
        contentPadding = PaddingValues(),
    ) {
        val rowModifier =
            Modifier
                .run { if (fillWidth) fillMaxWidth() else this }
                .run { if (fillHeight) fillMaxHeight() else this }
                .padding(
                    horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2),
                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                )

        Row(
            modifier = rowModifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    modifier = Modifier.size(adaptDpToFontScale(MediumIconSize)),
                    contentDescription =
                        if (iconContentDescription != -1) {
                            stringResource(id = iconContentDescription)
                        } else {
                            ""
                        },
                )
            }
            if (icon != null && label != null) {
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            if (label != null) {
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonFilledPreview() {
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
                ButtonFilled(
                    label = "Wrap Me",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                )

                Text(text = "fillWidth = true:")
                ButtonFilled(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    label = "Fill Width",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "fillHeight = true (fixed width):")
                ButtonFilled(
                    modifier = Modifier.width(adaptDpToFontScale(200.dp)),
                    label = "Fill Height",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillHeight = true,
                )

                Text(text = "fillHeight = true (wrap width):")
                ButtonFilled(
                    label = "Fill Height Wrap Width",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillHeight = true,
                )

                Text(text = "fillWidth & fillHeight = true:")
                ButtonFilled(
                    modifier = Modifier.height(adaptDpToFontScale(64.dp)),
                    label = "Fill Both",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "Explicit size (content should center):")
                ButtonFilled(
                    modifier =
                        Modifier
                            .width(adaptDpToFontScale(250.dp))
                            .height(adaptDpToFontScale(72.dp)),
                    label = "Fixed Size",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                    fillHeight = true,
                )

                Text(text = "Explicit width, wrap height (content should center vertically within its natural height):")
                ButtonFilled(
                    modifier = Modifier.width(adaptDpToFontScale(250.dp)),
                    label = "Fixed Width Wrap Height",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillWidth = true,
                )

                Text(text = "Accent Color & fillWidth:")
                ButtonFilled(
                    label = "Accent Fill",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    accentColor = true,
                    fillWidth = true,
                )

                Text(text = "Disabled & fillWidth:")
                ButtonFilled(
                    label = "Disabled Fill",
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    enabled = false,
                    fillWidth = true,
                )

                Text(text = "Icon only, wrap:")
                ButtonFilled(
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                )

                Text(text = "Icon only, fillHeight (fixed width):")
                ButtonFilled(
                    modifier = Modifier.width(adaptDpToFontScale(80.dp)),
                    icon = ImageVector.vectorResource(CommonResourcesR.drawable.cog),
                    fillHeight = true,
                    fillWidth = true,
                )
            }
        }
    }
}

package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import fr.shiningcat.simplehiit.android.common.ui.utils.MINIMUM_TOUCH_SIZE_DP
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun ButtonFilled(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String? = null,
    icon: ImageVector? = null,
    @StringRes
    iconContentDescription: Int = -1,
    accentColor: Boolean = false,
    enabled: Boolean = true,
) {
    Button(
        modifier =
            modifier.defaultMinSize(
                minWidth = MINIMUM_TOUCH_SIZE_DP,
                minHeight = MINIMUM_TOUCH_SIZE_DP,
            ),
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
        contentPadding = PaddingValues(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
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
                modifier = Modifier.width(300.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    enabled = false,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                    accentColor = true,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                    enabled = false,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    enabled = false,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    accentColor = true,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                )
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(44.dp))
                            .width(adaptDpToFontScale(150.dp)),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                    enabled = false,
                )
            }
        }
    }
}

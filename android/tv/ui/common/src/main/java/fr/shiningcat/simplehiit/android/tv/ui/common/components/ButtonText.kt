package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = transparentButtonTextColors(),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        contentPadding = PaddingValues(12.dp),
    ) {
        Row(
            modifier = Modifier.fillMaxSize(),
            verticalAlignment = Alignment.CenterVertically,
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
                modifier = Modifier.weight(weight = 1f, fill = true),
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
                modifier = Modifier.width(adaptDpToFontScale(300.dp)),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonText(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(48.dp))
                            .width(adaptDpToFontScale(120.dp)),
                    label = "I'm a button",
                )
                ButtonText(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(48.dp))
                            .width(adaptDpToFontScale(120.dp)),
                    label = "I'm a button",
                    icon = R.drawable.cog,
                )
                ButtonText(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(48.dp))
                            .width(adaptDpToFontScale(120.dp)),
                    label = "I'm a button",
                    icon = R.drawable.cog,
                    enabled = false,
                )
            }
        }
    }
}

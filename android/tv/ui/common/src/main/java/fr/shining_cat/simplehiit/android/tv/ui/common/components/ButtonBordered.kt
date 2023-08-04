package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonBordered(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String,
    @DrawableRes
    icon: Int = -1,
    @StringRes
    iconContentDescription: Int = -1,
    enabled: Boolean = true
) {
    OutlinedButton(
        modifier = modifier.padding(1.dp), //padding to ensure the border is not truncated,
        contentPadding = PaddingValues(12.dp),
        onClick = { onClick() },
        enabled = enabled,
        colors = ButtonDefaults.colors( // this is mostly to allow for a more visible focus stsate
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContentColor = MaterialTheme.colorScheme.primary,
            pressedContainerColor = MaterialTheme.colorScheme.primary,
            pressedContentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = Color.Transparent,
            disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f)
        ),
        shape = ButtonDefaults.shape(MaterialTheme.shapes.small),
        border = ButtonDefaults.border(
            border = Border(
                BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.small
            ),
            focusedBorder = Border(
                BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.small
            ),
            pressedBorder = Border(
                BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.primary
                ),
                shape = MaterialTheme.shapes.small
            ),
            disabledBorder = Border(
                BorderStroke(
                    width = 2.dp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f)
                ),
                shape = MaterialTheme.shapes.small
            )
        )
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != -1) {
                Icon(
                    imageVector = ImageVector.vectorResource(icon),
                    contentDescription = if (iconContentDescription != -1) {
                        stringResource(id = iconContentDescription)
                    } else "",
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun FilledButtonPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonBordered(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button"
                )
                ButtonBordered(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = R.drawable.cog
                )
                ButtonBordered(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = R.drawable.cog,
                    enabled = false
                )
            }
        }
    }
}

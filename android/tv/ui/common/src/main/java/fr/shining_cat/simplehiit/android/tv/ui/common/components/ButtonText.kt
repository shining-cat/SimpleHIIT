package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonText(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String,
    @DrawableRes
    icon: Int = -1,
    @StringRes
    iconContentDescription: Int = -1,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        enabled = enabled,
        colors = TransparentButtonTextColors(),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        contentPadding = PaddingValues(12.dp)
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

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun TransparentButtonTextColors() = ButtonDefaults.colors(
    containerColor = Color.Transparent,
    contentColor = MaterialTheme.colorScheme.onPrimary,
    focusedContainerColor = MaterialTheme.colorScheme.surface,
    focusedContentColor = MaterialTheme.colorScheme.onSurface,
    pressedContainerColor = MaterialTheme.colorScheme.surface,
    pressedContentColor = MaterialTheme.colorScheme.secondary,
    disabledContainerColor = Color.Transparent,
    disabledContentColor = MaterialTheme.colorScheme.onPrimary.copy(alpha = .6f)
)

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
                ButtonText(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button"
                )
                ButtonText(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = R.drawable.cog
                )
                ButtonText(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = R.drawable.cog,
                    enabled = false
                )
            }
        }
    }
}

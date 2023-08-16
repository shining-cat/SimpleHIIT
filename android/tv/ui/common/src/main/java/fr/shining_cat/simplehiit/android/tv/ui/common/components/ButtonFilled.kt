package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
fun ButtonFilled(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String? = null,
    icon: ImageVector? = null,
    @StringRes
    iconContentDescription: Int = -1,
    accentColor: Boolean = false,
    enabled: Boolean = true
) {
    Button(
        modifier = modifier,
        enabled = enabled,
        onClick = { onClick() },
        colors = ButtonDefaults.colors(
            containerColor = if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            contentColor = if (accentColor) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
            focusedContainerColor =if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary,
            focusedContentColor =if (accentColor) MaterialTheme.colorScheme.onSecondary else MaterialTheme.colorScheme.onPrimary,
            pressedContainerColor = MaterialTheme.colorScheme.primary,
            pressedContentColor = MaterialTheme.colorScheme.secondary,
            disabledContainerColor = (if (accentColor) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.primary).copy(alpha = .6f),
            disabledContentColor = MaterialTheme.colorScheme.onPrimary
        ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        contentPadding = PaddingValues(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxHeight(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = if (iconContentDescription != -1) {
                        stringResource(id = iconContentDescription)
                    } else "",
                )
            }
            if(icon != null && label != null){
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
            }
            if(label != null){
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
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
private fun ButtonFilledPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button"
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog)
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    enabled = false
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    accentColor = true
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    label = "I'm a button",
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                    enabled = false
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    enabled = false
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    accentColor = true
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true
                )
                ButtonFilled(
                    modifier = Modifier.height(48.dp),
                    icon = ImageVector.vectorResource(R.drawable.cog),
                    accentColor = true,
                    enabled = false
                )
            }
        }
    }
}

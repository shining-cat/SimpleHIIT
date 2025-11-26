package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButton
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

/**
 * Custom icon-only button for TV with proper focus handling and larger icons.
 * Uses primary color for the icon with proper focus states.
 */
@Composable
fun ButtonIcon(
    onClick: () -> Unit,
    icon: ImageVector,
    iconContentDescription: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
) {
    val interactionSource = remember { MutableInteractionSource() }

    IconButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        interactionSource = interactionSource,
        colors = transparentButtonTextColors(),
        scale =
            ButtonDefaults.scale(
                scale = 1f,
                focusedScale = 1.1f,
            ),
    ) {
        Icon(
            imageVector = icon,
            contentDescription =
                androidx.compose.ui.res
                    .stringResource(id = iconContentDescription),
            modifier = Modifier.size(40.dp),
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonIconPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            ButtonIcon(
                onClick = {},
                icon = Icons.Default.Add,
                iconContentDescription = android.R.string.ok,
            )
        }
    }
}

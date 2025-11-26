package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

/**
 * A wrapper around Material 3's TextButton that uses onSurface color for better contrast.
 *
 * This provides a consistent, high-contrast text button across the mobile app,
 * matching the visual hierarchy where emphasis comes from visual weight (border presence)
 * rather than color variation.
 *
 * Visual hierarchy:
 * - FilledButton (Amber/Teal) - Highest emphasis
 * - OutlinedButton (onSurface with border) - Medium emphasis
 * - OnSurfaceTextButton (onSurface, no border) - Low emphasis
 */
@Composable
fun OnSurfaceTextButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    content: @Composable RowScope.() -> Unit,
) {
    TextButton(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        colors =
            ButtonDefaults.textButtonColors(
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        content()
    }
}

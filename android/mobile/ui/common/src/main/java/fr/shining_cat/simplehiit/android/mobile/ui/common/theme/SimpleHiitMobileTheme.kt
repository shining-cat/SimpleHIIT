package fr.shining_cat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun SimpleHiitMobileTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> mobileDarkColorScheme
        else -> mobileLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = mobileType,
        shapes = mobileShapes,
        content = content
    )
}

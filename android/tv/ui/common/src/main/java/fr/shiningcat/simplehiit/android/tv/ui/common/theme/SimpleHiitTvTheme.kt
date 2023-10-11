package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SimpleHiitTvTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> tvDarkColorScheme
        else -> tvLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = tvType,
        shapes = tvShapes,
        content = content
    )
}

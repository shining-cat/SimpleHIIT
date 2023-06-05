package fr.shining_cat.simplehiit.android.mobile.commonui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = Teal600,
    secondary = Amber800,
    background = Grey900,
    surface = Grey800,
    onPrimary = Grey50,
    onSecondary = Grey900,
    onBackground = Grey50,
    onSurface = Grey50,
    error = Red_200,
    onError = Black
)

private val LightColorScheme = lightColorScheme(
    primary = Teal300,
    secondary = Amber500,
    background = Grey50,
    surface = Grey100,
    onPrimary = Grey900,
    onSecondary = Grey900,
    onBackground = Black,
    onSurface = Grey900,
    error = Red_600,
    onError = White
)

@Composable
fun SimpleHiitTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        shapes = shapes,
        content = content
    )
}

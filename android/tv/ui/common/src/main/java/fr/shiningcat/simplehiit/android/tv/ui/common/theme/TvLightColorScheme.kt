package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.common.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
val tvLightColorScheme = lightColorScheme(
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
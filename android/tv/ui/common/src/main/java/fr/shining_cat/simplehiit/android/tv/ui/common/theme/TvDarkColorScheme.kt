package fr.shining_cat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.darkColorScheme
import fr.shining_cat.simplehiit.android.common.theme.*

@OptIn(ExperimentalTvMaterial3Api::class)
val tvDarkColorScheme = darkColorScheme(
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
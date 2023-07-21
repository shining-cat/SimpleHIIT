package fr.shining_cat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.lightColorScheme
import fr.shining_cat.simplehiit.android.common.theme.Amber500
import fr.shining_cat.simplehiit.android.common.theme.Black
import fr.shining_cat.simplehiit.android.common.theme.Grey100
import fr.shining_cat.simplehiit.android.common.theme.Grey50
import fr.shining_cat.simplehiit.android.common.theme.Grey900
import fr.shining_cat.simplehiit.android.common.theme.Red_600
import fr.shining_cat.simplehiit.android.common.theme.Teal300
import fr.shining_cat.simplehiit.android.common.theme.White

val mobileLightColorScheme = lightColorScheme(
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
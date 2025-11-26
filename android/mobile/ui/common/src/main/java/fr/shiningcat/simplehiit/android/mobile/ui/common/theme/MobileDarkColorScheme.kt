package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.darkColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber200
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_200
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal200
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal700
import fr.shiningcat.simplehiit.android.common.ui.theme.White

val mobileDarkColorScheme =
    darkColorScheme(
        primary = Teal700,
        onPrimary = White,
        primaryContainer = Teal200,
        onPrimaryContainer = Black,
        secondary = Amber700,
        onSecondary = White,
        secondaryContainer = Amber200,
        onSecondaryContainer = Black,
        background = Black,
        onBackground = White,
        surface = Grey900,
        onSurface = White,
        onSurfaceVariant = White,
        outline = White,
        outlineVariant = White,
        error = Red_200,
        onError = Black,
    )

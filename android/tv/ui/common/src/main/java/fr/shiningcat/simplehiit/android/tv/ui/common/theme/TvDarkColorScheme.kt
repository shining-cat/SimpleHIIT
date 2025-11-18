package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.darkColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_200
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal300
import fr.shiningcat.simplehiit.android.common.ui.theme.White

val tvDarkColorScheme =
    darkColorScheme(
        primary = Teal300,
        secondary = Amber700,
        background = Black,
        surface = Grey900,
        surfaceVariant = Teal300,
        onPrimary = Black,
        onSecondary = Black,
        onBackground = White,
        onSurface = White,
        inverseOnSurface = Black,
        error = Red_200,
        onError = Black,
    )

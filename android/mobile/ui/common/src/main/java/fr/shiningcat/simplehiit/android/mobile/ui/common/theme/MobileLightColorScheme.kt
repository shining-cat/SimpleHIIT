package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber400
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey100
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey50
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_600
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal400
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal700
import fr.shiningcat.simplehiit.android.common.ui.theme.White

val mobileLightColorScheme =
    lightColorScheme(
        primary = Teal400,
        onPrimary = Black,
        primaryContainer = Teal700,
        onPrimaryContainer = White,
        secondary = Amber400,
        onSecondary = Black,
        secondaryContainer = Amber700,
        onSecondaryContainer = White,
        background = Grey50,
        onBackground = Black,
        surface = Grey100,
        onSurface = Grey900,
        onSurfaceVariant = Grey900,
        outline = Grey900,
        outlineVariant = Grey900,
        error = Red_600,
        onError = White,
    )

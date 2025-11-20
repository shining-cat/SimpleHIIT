package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey100
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey50
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_600
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal700
import fr.shiningcat.simplehiit.android.common.ui.theme.White

val mobileLightColorScheme =
    lightColorScheme(
        primary = Teal700,
        secondary = Amber700,
        background = Grey50,
        surface = Grey100,
        onPrimary = White,
        onSecondary = Black,
        onBackground = Black,
        onSurface = Grey900,
        error = Red_600,
        onError = White,
    )

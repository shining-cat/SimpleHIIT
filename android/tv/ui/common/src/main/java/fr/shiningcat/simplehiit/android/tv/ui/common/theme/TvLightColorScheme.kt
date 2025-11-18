package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.ColorScheme
import androidx.tv.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey50
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_600
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal700
import fr.shiningcat.simplehiit.android.common.ui.theme.White

val tvLightColorScheme: ColorScheme
    get() =
        lightColorScheme(
            primary = Teal700,
            secondary = Amber700,
            background = Grey50,
            surface = White,
            onPrimary = White,
            onSecondary = Black,
            onBackground = Black,
            onSurface = Black,
            error = Red_600,
            onError = White,
        )

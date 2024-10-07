package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.ColorScheme
import androidx.tv.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.common.theme.Amber500
import fr.shiningcat.simplehiit.android.common.theme.Black
import fr.shiningcat.simplehiit.android.common.theme.Grey100
import fr.shiningcat.simplehiit.android.common.theme.Grey50
import fr.shiningcat.simplehiit.android.common.theme.Grey900
import fr.shiningcat.simplehiit.android.common.theme.Red_600
import fr.shiningcat.simplehiit.android.common.theme.Teal300
import fr.shiningcat.simplehiit.android.common.theme.White

val tvLightColorScheme: ColorScheme
    get() =
        lightColorScheme(
            primary = Teal300,
            secondary = Amber500,
            background = Grey50,
            surface = Grey100,
            onPrimary = Grey900,
            onSecondary = Grey900,
            onBackground = Black,
            onSurface = Grey900,
            error = Red_600,
            onError = White,
        )

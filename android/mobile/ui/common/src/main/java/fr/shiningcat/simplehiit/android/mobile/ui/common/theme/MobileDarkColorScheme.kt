package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.darkColorScheme
import fr.shiningcat.simplehiit.android.common.theme.Amber800
import fr.shiningcat.simplehiit.android.common.theme.Black
import fr.shiningcat.simplehiit.android.common.theme.Grey50
import fr.shiningcat.simplehiit.android.common.theme.Grey800
import fr.shiningcat.simplehiit.android.common.theme.Grey900
import fr.shiningcat.simplehiit.android.common.theme.Red_200
import fr.shiningcat.simplehiit.android.common.theme.Teal600

val mobileDarkColorScheme =
    darkColorScheme(
        primary = Teal600,
        secondary = Amber800,
        background = Grey900,
        surface = Grey800,
        onPrimary = Grey50,
        onSecondary = Grey900,
        onBackground = Grey50,
        onSurface = Grey50,
        error = Red_200,
        onError = Black,
    )

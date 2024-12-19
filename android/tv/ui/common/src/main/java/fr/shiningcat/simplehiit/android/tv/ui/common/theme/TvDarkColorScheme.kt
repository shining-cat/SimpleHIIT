package fr.shiningcat.simplehiit.android.tv.ui.common.theme

import androidx.tv.material3.darkColorScheme
import fr.shiningcat.simplehiit.android.common.ui.theme.Amber800
import fr.shiningcat.simplehiit.android.common.ui.theme.Black
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey50
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey800
import fr.shiningcat.simplehiit.android.common.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.common.ui.theme.Red_200
import fr.shiningcat.simplehiit.android.common.ui.theme.Teal600

val tvDarkColorScheme =
    darkColorScheme(
        primary = Teal600,
        secondary = Amber800,
        background = Grey900,
        surface = Grey800,
        surfaceVariant = Teal600,
        onPrimary = Grey50,
        onSecondary = Grey900,
        onBackground = Grey50,
        onSurface = Grey50,
        inverseOnSurface = Grey50,
        error = Red_200,
        onError = Black,
    )

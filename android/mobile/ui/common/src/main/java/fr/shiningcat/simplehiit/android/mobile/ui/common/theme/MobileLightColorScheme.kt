/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.common.theme

import androidx.compose.material3.lightColorScheme
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Amber400
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Amber700
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Black
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Grey100
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Grey50
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Grey600
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Grey900
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Red_600
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Teal400
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.Teal700
import fr.shiningcat.simplehiit.android.shared.core.ui.theme.White

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
        outlineVariant = Grey600,
        error = Red_600,
        onError = White,
    )

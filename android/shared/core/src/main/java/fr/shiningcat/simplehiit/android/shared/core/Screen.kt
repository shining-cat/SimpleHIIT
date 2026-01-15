/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForCompose

package fr.shiningcat.simplehiit.android.shared.core

import androidx.navigation3.runtime.NavKey
import fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose
import kotlinx.serialization.Serializable

/**
 * Navigation destinations for the SimpleHIIT app.
 * Uses Navigation 3's type-safe NavKey system with Kotlin serialization.
 */
sealed interface Screen : NavKey {
    @Serializable
    data object Home : Screen

    @Serializable
    data object Settings : Screen

    @Serializable
    data object Statistics : Screen

    @Serializable
    data object Session : Screen

    @Serializable
    data object About : Screen
}

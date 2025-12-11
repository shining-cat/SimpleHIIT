package fr.shiningcat.simplehiit.android.common

import androidx.navigation3.runtime.NavKey
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
}

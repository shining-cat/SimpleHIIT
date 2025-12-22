package fr.shiningcat.simplehiit.android.mobile.ui.common.helpers

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement

/**
 * Determines the UI arrangement based on the current window size class.
 *
 * Uses Material Design 3 standard breakpoints:
 * - Width ≥ EXPANDED (840dp): Tablet/large screen in landscape → HORIZONTAL
 * - Height < MEDIUM (< 480dp, i.e. COMPACT): Phone in landscape → HORIZONTAL
 * - Otherwise: Phone/tablet in portrait → VERTICAL
 */
@Composable
fun currentUiArrangement(): UiArrangement {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass

    return if (windowSizeClass.isWidthAtLeastBreakpoint(WindowSizeClass.WIDTH_DP_EXPANDED_LOWER_BOUND)) {
        // Typically, a tablet or bigger in landscape
        UiArrangement.HORIZONTAL
    } else {
        if (!windowSizeClass.isHeightAtLeastBreakpoint(WindowSizeClass.HEIGHT_DP_MEDIUM_LOWER_BOUND)) {
            // Typically, a phone in landscape (COMPACT height < 480dp)
            UiArrangement.HORIZONTAL
        } else {
            // Typically, a phone or tablet in portrait
            UiArrangement.VERTICAL
        }
    }
}

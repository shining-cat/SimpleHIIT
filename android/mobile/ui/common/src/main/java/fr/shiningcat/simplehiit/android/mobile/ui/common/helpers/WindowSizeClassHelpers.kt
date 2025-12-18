package fr.shiningcat.simplehiit.android.mobile.ui.common.helpers

import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement

/**
 * Determines the UI arrangement based on the current window size class.
 *
 * Note: Uses deprecated windowWidthSizeClass and windowHeightSizeClass properties.
 * TODO: Update to use non-deprecated API when migrating to newer adaptive layout APIs.
 */
@Composable
fun currentUiArrangement(): UiArrangement {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    return if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) {
        // Typically, a tablet or bigger in landscape
        UiArrangement.HORIZONTAL
    } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
        if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) {
            // Typically, a phone in landscape
            UiArrangement.HORIZONTAL
        } else {
            // Typically, a phone or tablet in portrait
            UiArrangement.VERTICAL
        }
    }
}

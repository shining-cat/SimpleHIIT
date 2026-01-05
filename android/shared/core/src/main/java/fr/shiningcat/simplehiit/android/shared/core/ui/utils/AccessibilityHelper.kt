package fr.shiningcat.simplehiit.android.shared.core.ui.utils

import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.DialogProperties

/**
 * Adapts a base Dp size based on the current font scale, with optional minimum and maximum coercion.
 *
 * This Composable function takes a `baseSize` in Dp and multiplies it by the current
 * `LocalDensity.current.fontScale`. This ensures that UI elements sized with this function
 * scale proportionally with the user's font size accessibility settings.
 *
 * Optional `coerceMinSize` and `coerceMaxSize` parameters can be provided to limit
 * the resulting Dp value within a specific range.
 *
 * @param baseSize The base Dp value to be scaled.
 * @param coerceMinSize An optional minimum Dp value. If the scaled size is smaller than this,
 *                      it will be coerced to this minimum.
 * @param coerceMaxSize An optional maximum Dp value. If the scaled size is larger than this,
 *                      it will be coerced to this maximum.
 * @return The adapted Dp value, scaled by the font scale and potentially coerced to the
 *         specified minimum and maximum sizes.
 */
@Composable
fun adaptDpToFontScale(
    baseSize: Dp,
    coerceMinSize: Dp? = null,
    coerceMaxSize: Dp? = null,
): Dp {
    val density = LocalDensity.current
    val fontScale = density.fontScale
    val scaledBase = baseSize * fontScale
    val coercedMin = coerceMinSize?.let { scaledBase.coerceAtLeast(it) } ?: scaledBase
    val coercedMax = coerceMaxSize?.let { coercedMin.coerceAtMost(it) } ?: coercedMin
    return coercedMax
}

/**
 * Returns adaptive DialogProperties based on the current font scale.
 *
 * At higher font scales (≥1.5f), sets usePlatformDefaultWidth to false
 * to allow dialogs to expand beyond the default Material3 width constraint (~560dp).
 *
 * @return DialogProperties with usePlatformDefaultWidth set based on font scale.
 */
@Composable
fun adaptiveDialogProperties(): DialogProperties {
    val fontScale = LocalDensity.current.fontScale
    return DialogProperties(
        usePlatformDefaultWidth = fontScale < 1.5f,
    )
}

/**
 * Applies adaptive width to dialog surfaces based on the current font scale.
 *
 * At higher font scales, dialogs expand to use more horizontal space, reducing the need
 * for vertical scrolling in landscape orientation where vertical space is limited.
 *
 * Must be used in conjunction with adaptiveDialogProperties() to work properly.
 *
 * Width percentages based on screen width:
 * - Font scale >= 2.0f (200%): 95% of screen width
 * - Font scale >= 1.5f (150%): 85% of screen width
 * - Font scale < 1.5f: Default dialog width (no modification)
 *
 * @return A Modifier with adaptive maximum width based on font scale and screen size.
 */
@Composable
fun Modifier.adaptiveDialogWidth(): Modifier {
    val fontScale = LocalDensity.current.fontScale
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp.dp

    return this.then(
        when {
            fontScale >= 2.0f -> Modifier.widthIn(max = screenWidthDp * 0.95f)
            fontScale >= 1.5f -> Modifier.widthIn(max = screenWidthDp * 0.85f)
            else -> Modifier
        },
    )
}

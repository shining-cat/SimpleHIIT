package fr.shiningcat.simplehiit.android.common.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp

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

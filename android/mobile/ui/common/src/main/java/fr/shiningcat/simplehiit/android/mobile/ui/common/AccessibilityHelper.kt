package fr.shiningcat.simplehiit.android.mobile.ui.common

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

val minimumTouchSize: Dp = 44.dp

@Composable
fun adaptDpToFontScale(
    baseSize: Dp,
    coerceMinSize: Dp? = null,
    coerceMaxSize: Dp? = null
): Dp {
    val density = LocalDensity.current
    val fontScale = density.fontScale
    val scaledBase = baseSize * fontScale
    val coercedMin = coerceMinSize?.let { scaledBase.coerceAtLeast(it) } ?: scaledBase
    val coercedMax = coerceMaxSize?.let { coercedMin.coerceAtMost(it) } ?: scaledBase
    return coercedMax
}

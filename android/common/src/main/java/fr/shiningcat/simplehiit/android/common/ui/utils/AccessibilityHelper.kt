package fr.shiningcat.simplehiit.android.common.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

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

data class TextLayoutInfo(
    val text: String,
    val style: TextStyle,
)

/**
 * Checks if the given text, when styled with the provided [TextLayoutInfo.style], fits within
 * the specified [numberOfLines] given the [availableWidthPx].
 *
 * **Note:** The [TextLayoutInfo.style] MUST use fontSize in sp units (e.g., 14.sp)
 *          for font scale accessibility to work correctly. Using dp or px units
 *          will not respect the user's font size settings.
 *
 *  @param textLayoutInfo A [TextLayoutInfo] object containing the text string and its [TextStyle]
 * @param numberOfLines The maximum number of lines the text is allowed to occupy.
 * @param availableWidthPx The available width in pixels for the text to be rendered in.
 * @return `true` if the text fits within the specified constraints, `false` otherwise.
 *         Returns `false` if [availableWidthPx] or [numberOfLines] is less than or equal to 0,
 *         or if the [textLayoutInfo.text] is empty.
 */
@Composable
fun fitsOnXLines(
    textLayoutInfo: TextLayoutInfo,
    numberOfLines: Int,
    availableWidthPx: Int = 1,
): Boolean {
    if (availableWidthPx <= 0 || numberOfLines <= 0 || textLayoutInfo.text.isEmpty()) return false
    val textMeasurer = rememberTextMeasurer()
    val result =
        textMeasurer.measure(
            text = textLayoutInfo.text,
            style = textLayoutInfo.style,
            constraints = Constraints(maxWidth = availableWidthPx),
        )
    return result.lineCount <= numberOfLines
}

/**
 * Checks if a list of text elements, each with its own style, fits horizontally within a given width.
 *
 * This Composable function calculates the combined width of all text elements specified in
 * the `textLayoutInfos` list. Each text element's width is measured based on its content and
 * [TextStyle]. It then compares this combined width against the `availableWidthPx`.
 *
 * @param textLayoutInfos A list of [TextLayoutInfo] objects, where each object contains a
 *                        text string and its corresponding [TextStyle].
 * @param availableWidthPx The available width in pixels for the combined text elements.
 *                         Defaults to 1 pixel.
 * @return `true` if the combined width of all text elements is less than or equal to
 *         `availableWidthPx`, `false` otherwise. Also returns `false` if `availableWidthPx`
 *         is less than or equal to 0.
 */
@Composable
fun fitsInWidth(
    textLayoutInfos: List<TextLayoutInfo>,
    spacingDp: Dp = 0.dp,
    availableWidthPx: Int = 1,
): Boolean {
    if (availableWidthPx <= 0) return false
    val density = LocalDensity.current
    val horizontalSpacingPx = with(density) { spacingDp.toPx() }
    val textMeasurer = rememberTextMeasurer()
    var totalWidth = 0f
    textLayoutInfos.forEachIndexed { index, textLayoutInfo ->
        val itemWidth =
            textMeasurer
                .measure(
                    text = textLayoutInfo.text,
                    style = textLayoutInfo.style,
                    constraints = Constraints(),
                ).size.width
        totalWidth += itemWidth
        if (index > 0) totalWidth += horizontalSpacingPx
    }
    return totalWidth <= availableWidthPx
}

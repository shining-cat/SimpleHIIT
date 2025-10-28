package fr.shiningcat.simplehiit.android.common.ui.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

data class TextLayoutInfo(
    val text: String,
    val style: TextStyle,
)

/**
 * Calculates the height in pixels required to render the given text within the specified constraints.
 *
 * This Composable function measures the height needed to display text when styled with the
 * provided [TextLayoutInfo.style], constrained by [availableWidthPx] and [maxLines].
 *
 * **Note:** The [TextLayoutInfo.style] MUST use fontSize in sp units (e.g., 14.sp)
 *          for font scale accessibility to work correctly. Using dp or px units
 *          will not respect the user's font size settings.
 *
 * @param textLayoutInfo A [TextLayoutInfo] object containing the text string and its [TextStyle].
 * @param availableWidthPx The available width in pixels for the text to be rendered in.
 * @param maxLines The maximum number of lines the text is allowed to occupy. Defaults to 1.
 * @return The height in pixels required to render the text, or `null` if [availableWidthPx]
 *         or [maxLines] is less than or equal to 0, or if the [textLayoutInfo.text] is empty.
 */
@Composable
fun getTextHeightPix(
    textLayoutInfo: TextLayoutInfo,
    availableWidthPx: Int,
    maxLines: Int = 1,
): Int? {
    if (availableWidthPx <= 0 || maxLines <= 0 || textLayoutInfo.text.isEmpty()) return null
    val textMeasurer = rememberTextMeasurer()
    val result =
        textMeasurer.measure(
            text = textLayoutInfo.text,
            style = textLayoutInfo.style,
            maxLines = maxLines,
            constraints = Constraints(maxWidth = availableWidthPx),
        )
    return result.size.height
}

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
 * **Note:** The [TextLayoutInfo.style] MUST use fontSize in sp units (e.g., 14.sp)
 *          for font scale accessibility to work correctly. Using dp or px units
 *          will not respect the user's font size settings.
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

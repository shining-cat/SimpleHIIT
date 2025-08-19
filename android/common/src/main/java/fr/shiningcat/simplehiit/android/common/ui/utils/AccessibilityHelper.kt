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
    val coercedMax = coerceMaxSize?.let { coercedMin.coerceAtMost(it) } ?: scaledBase
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
 * @param textLayoutInfo A [TextLayoutInfo] object containing the text string and its [TextStyle].
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

enum class ButtonType { TEXT, OUTLINED, FILLED }

data class ButtonLayoutInfo(
    val label: String,
    val style: TextStyle,
    val type: ButtonType,
)

/**
 * Determines if a list of buttons can fit on a single line within the available width.
 *
 * This function calculates the total width required by the buttons, including their labels,
 * padding, and the specified horizontal spacing between them. It then compares this total
 * width with the `availableWidthPx`.
 *
 * NOTE: This function assumes typical horizontal paddings for Material3 buttons.
 *
 * @param buttons A list of [ButtonLayoutInfo] objects, each describing a button's label, style, and type.
 * @param availableWidthPx The available width in pixels for displaying the buttons.
 * @param horizontalSpacingDp The desired horizontal spacing between buttons in Dp.
 * @return `true` if all buttons fit on one line within the `availableWidthPx`, `false` otherwise.
 *         Returns `false` if `availableWidthPx` is not positive, `buttons` is empty, or `horizontalSpacingDp` is not positive.
 */
@Composable
fun buttonsFitOnOneLine(
    buttons: List<ButtonLayoutInfo>,
    availableWidthPx: Int,
    horizontalSpacingDp: Dp,
): Boolean {
    if (availableWidthPx <= 0 || buttons.isEmpty() || horizontalSpacingDp <= 0.dp) return false
    val textMeasurer = rememberTextMeasurer()
    val density = LocalDensity.current
    val horizontalSpacingPx = with(density) { horizontalSpacingDp.toPx() }

    // Typical horizontal paddings for Material3 buttons
    val paddingsDp =
        mapOf(
            // ButtonDefaults.TextButtonHorizontalPadding:
            ButtonType.TEXT to 12.dp,
            // ButtonDefaults.ButtonHorizontalPadding:
            ButtonType.OUTLINED to 24.dp,
            // ButtonDefaults.ButtonHorizontalPadding:
            ButtonType.FILLED to 24.dp,
        )
    // calculate what width would be needed to have all buttons unconstrained in a line
    var totalWidth = 0f
    buttons.forEachIndexed { index, button ->
        val labelWidth =
            textMeasurer
                .measure(
                    text = button.label,
                    style = button.style,
                    constraints = Constraints(),
                ).size.width

        val paddingPx = with(density) { paddingsDp[button.type]?.toPx() ?: 0.dp.toPx() }
        totalWidth += labelWidth + 2 * paddingPx
        if (index > 0) totalWidth += horizontalSpacingPx
    }
    // if the total width of all unconstrained buttons in a line is larger than availableWidthPx, then they don't fit
    return totalWidth <= availableWidthPx
}

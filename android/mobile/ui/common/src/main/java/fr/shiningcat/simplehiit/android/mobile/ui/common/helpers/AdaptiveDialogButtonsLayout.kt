package fr.shiningcat.simplehiit.android.mobile.ui.common.helpers

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.OnSurfaceTextButton
import fr.shiningcat.simplehiit.commonresources.R

/**
 * Configuration for a single button within the AdaptiveDialogButtonsLayout.
 *
 * @param label The text to display on the button.
 * @param onClick The lambda to be executed when the button is clicked.
 * @param type The type of the button (TEXT, OUTLINED, FILLED), corresponding to Material Design button styles.
 * @param enabled Controls the enabled state of the button. Defaults to true.
 * @param style  [TextStyle] for the button's label.
 */
data class DialogButtonConfig(
    val label: String,
    val type: ButtonType,
    val style: TextStyle,
    val onClick: () -> Unit,
    val enabled: Boolean = true,
)

enum class ButtonType { TEXT, OUTLINED, FILLED }

/**
 * A Composable that lays out a list of dialog buttons adaptively in a Row or Column.
 *
 * It uses the private [buttonsFitOnOneLine] helper to determine if the provided [buttons]
 * can fit into the [dialogContentWidthDp] on a single line. If they fit, they are
 * laid out in a [Row] with [Arrangement.End]. Otherwise, they are laid out in a
 * [Column] where each button takes the full width.
 *
 * @param buttons A list of [DialogButtonConfig] objects describing the buttons to display.
 * @param modifier Modifier to be applied to the layout container (Row or Column).
 * @param dialogContentWidthDp The available width for the buttons layout.
 * @param horizontalSpacingDp The spacing between buttons when laid out in a Row. Defaults to 8.dp.
 * @param verticalSpacingDp The spacing between buttons when laid out in a Column. Defaults to 8.dp.
 *
 */
@Composable
fun AdaptiveDialogButtonsLayout(
    buttons: List<DialogButtonConfig>,
    modifier: Modifier = Modifier,
    dialogContentWidthDp: Dp,
    horizontalSpacingDp: Dp? = null,
    verticalSpacingDp: Dp? = null,
) {
    val actualHorizontalSpacingDp = horizontalSpacingDp ?: dimensionResource(R.dimen.spacing_1)
    val actualVerticalSpacingDp = verticalSpacingDp ?: dimensionResource(R.dimen.spacing_1)
    if (buttons.isEmpty()) {
        return // No buttons to lay out
    }

    val density = LocalDensity.current
    val availableWidthPx =
        remember(density, dialogContentWidthDp) {
            with(density) { dialogContentWidthDp.roundToPx() }
        }

    // Prepare ButtonLayoutInfo for buttonsFitOnOneLine
    val buttonLayoutInfos =
        remember(buttons) {
            buttons.map { config ->
                ButtonLayoutInfo(
                    label = config.label,
                    style = config.style,
                    type = config.type,
                )
            }
        }

    val fitOnOneLine =
        buttonsFitOnOneLine(
            buttons = buttonLayoutInfos,
            availableWidthPx = availableWidthPx,
            horizontalSpacingDp = actualHorizontalSpacingDp,
        )

    if (fitOnOneLine) {
        Row(
            modifier = modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            buttons.forEachIndexed { index, config ->
                RenderDialogButton(config = config)
                if (index < buttons.size - 1) {
                    Spacer(modifier = Modifier.width(actualHorizontalSpacingDp))
                }
            }
        }
    } else {
        Column(
            modifier = modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(actualVerticalSpacingDp),
        ) {
            buttons.forEach { config ->
                RenderDialogButton(
                    config = config,
                    modifier = Modifier.fillMaxWidth(),
                )
            }
        }
    }
}

@Composable
private fun RenderDialogButton(
    config: DialogButtonConfig,
    modifier: Modifier = Modifier,
) {
    when (config.type) {
        ButtonType.TEXT -> {
            OnSurfaceTextButton(
                onClick = config.onClick,
                enabled = config.enabled,
                modifier = modifier,
            ) {
                Text(text = config.label, style = config.style)
            }
        }

        ButtonType.OUTLINED -> {
            OutlinedButton(
                onClick = config.onClick,
                enabled = config.enabled,
                modifier = modifier,
            ) {
                Text(text = config.label, style = config.style)
            }
        }

        ButtonType.FILLED -> {
            Button(
                onClick = config.onClick,
                enabled = config.enabled,
                modifier = modifier,
            ) {
                Text(text = config.label, style = config.style)
            }
        }
    }
}

private data class ButtonLayoutInfo(
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
private fun buttonsFitOnOneLine(
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
            ButtonType.TEXT to dimensionResource(R.dimen.spacing_15),
            // ButtonDefaults.ButtonHorizontalPadding:
            ButtonType.OUTLINED to dimensionResource(R.dimen.spacing_3),
            // ButtonDefaults.ButtonHorizontalPadding:
            ButtonType.FILLED to dimensionResource(R.dimen.spacing_3),
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

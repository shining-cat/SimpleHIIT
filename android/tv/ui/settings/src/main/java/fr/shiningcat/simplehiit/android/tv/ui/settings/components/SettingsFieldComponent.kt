package fr.shiningcat.simplehiit.android.tv.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.MaterialTheme.typography
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.tv.ui.common.components.transparentButtonTextColors
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import kotlin.math.roundToInt

@Composable
fun SettingsFieldComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = "",
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val mainLabelStyled =
        TextLayoutInfo(
            text = label,
            style = typography.headlineSmall,
        )
    val secondaryLabelStyled =
        TextLayoutInfo(
            text = secondaryLabel,
            style = typography.labelMedium,
        )
    val valueStyled =
        TextLayoutInfo(
            text = value,
            style = typography.headlineSmall,
        )
    val secondaryValueStyled =
        TextLayoutInfo(
            text = secondaryValue,
            style = typography.labelMedium,
        )
    val elementsLeftList =
        listOf(
            mainLabelStyled,
            secondaryLabelStyled,
        )
    val elementsRightList =
        listOf(
            valueStyled,
            secondaryValueStyled,
        )
    val availableWidthPix = LocalWindowInfo.current.containerSize.width
    val leftAvailableWidthPix = 2 * availableWidthPix / 3f
    val rightAvailableWidthPix = availableWidthPix / 3f
    val useHorizontalLayout =
        elementsLeftList.all {
            fitsOnXLines(
                textLayoutInfo = it,
                numberOfLines = 1,
                availableWidthPx = leftAvailableWidthPix.roundToInt(),
            )
        } &&
            elementsRightList.all {
                fitsOnXLines(
                    textLayoutInfo = it,
                    numberOfLines = 1,
                    availableWidthPx = rightAvailableWidthPix.roundToInt(),
                )
            }
    val modifier =
        modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp)
    Row(modifier = modifier) {
        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(bottom = 8.dp),
            onClick = { onClick() },
            colors = transparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        ) {
            if (useHorizontalLayout) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(text = label, style = typography.headlineSmall)
                        if (secondaryLabel.isNotBlank()) {
                            Text(text = secondaryLabel, style = typography.labelMedium)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = value,
                            style = typography.headlineSmall,
                            textAlign = TextAlign.End,
                        )
                        if (secondaryValue.isNotBlank()) {
                            Text(
                                text = secondaryValue,
                                style = typography.labelMedium,
                                textAlign = TextAlign.End,
                            )
                        }
                    }
                }
            } else {
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text(
                        text = label,
                        style = typography.headlineSmall,
                        textAlign = TextAlign.Start,
                    )
                    if (secondaryLabel.isNotBlank()) {
                        Text(
                            text = secondaryLabel,
                            style = typography.labelMedium,
                            textAlign = TextAlign.Start,
                        )
                    }
                    Text(
                        text = value,
                        style = typography.headlineSmall,
                        textAlign = TextAlign.Start,
                    )
                    if (secondaryValue.isNotBlank()) {
                        Text(
                            text = secondaryValue,
                            style = typography.labelMedium,
                            textAlign = TextAlign.Start,
                        )
                    }
                }
            }
        }
    }
    /*Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(.15f))
        Button(
            modifier =
                modifier
                    .weight(weight = .6f, fill = true)
                    .defaultMinSize(minHeight = 48.dp)
                    .padding(bottom = 8.dp),
            onClick = { onClick() },
            colors = transparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(horizontalAlignment = Alignment.Start) {
                    Text(text = label, style = MaterialTheme.typography.headlineSmall)
                    if (secondaryLabel.isNotBlank()) {
                        Text(text = secondaryLabel, style = MaterialTheme.typography.labelMedium)
                    }
                }
                Column(horizontalAlignment = Alignment.End) {
                    Text(text = value, style = MaterialTheme.typography.headlineSmall)
                    if (secondaryValue.isNotBlank()) {
                        Text(text = secondaryValue, style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.weight(.15f))
    }*/
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SettingsFieldComponentPreview() {
    SimpleHiitTvTheme {
        Surface(
            modifier = Modifier.width(1080.dp),
            shape = MaterialTheme.shapes.extraSmall,
        ) {
            Column {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
                    secondaryLabel = "It has a secondary label",
                    secondaryValue = "Secondary value",
                )
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "Setting's value",
                )
            }
        }
    }
}

package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import kotlin.math.roundToInt

@Composable
fun SettingsFieldComponent(
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = "",
) {
    val mainLabelStyled =
        TextLayoutInfo(
            text = label,
            style = MaterialTheme.typography.headlineSmall,
        )
    val secondaryLabelStyled =
        TextLayoutInfo(
            text = secondaryLabel,
            style = MaterialTheme.typography.labelMedium,
        )
    val valueStyled =
        TextLayoutInfo(
            text = value,
            style = MaterialTheme.typography.headlineSmall,
        )
    val secondaryValueStyled =
        TextLayoutInfo(
            text = secondaryValue,
            style = MaterialTheme.typography.labelMedium,
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
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp)
            .clickable { onClick() }

    if (useHorizontalLayout) {
        Row(
            modifier,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Column(
                modifier = Modifier.weight(2f),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = mainLabelStyled.text,
                    modifier = Modifier.fillMaxWidth(),
                    style = mainLabelStyled.style,
                    textAlign = TextAlign.Start,
                )
                if (secondaryLabel.isNotBlank()) {
                    Text(
                        text = secondaryLabelStyled.text,
                        modifier = Modifier.fillMaxWidth(),
                        style = secondaryLabelStyled.style,
                        textAlign = TextAlign.Start,
                    )
                }
            }
            Column(
                modifier = Modifier.weight(1f),
                horizontalAlignment = Alignment.End,
            ) {
                Text(
                    text = valueStyled.text,
                    modifier = Modifier.fillMaxWidth(),
                    style = valueStyled.style,
                    textAlign = TextAlign.End,
                )
                if (secondaryValue.isNotBlank()) {
                    Text(
                        text = secondaryValueStyled.text,
                        modifier = Modifier.fillMaxWidth(),
                        style = secondaryValueStyled.style,
                        textAlign = TextAlign.End,
                    )
                }
            }
        }
    } else {
        Column(modifier) {
            Text(
                text = mainLabelStyled.text,
                modifier = Modifier.fillMaxWidth(),
                style = mainLabelStyled.style,
                textAlign = TextAlign.Start,
            )
            if (secondaryLabel.isNotBlank()) {
                Text(
                    text = secondaryLabelStyled.text,
                    modifier = Modifier.fillMaxWidth(),
                    style = secondaryLabelStyled.style,
                    textAlign = TextAlign.Start,
                )
            }
            Text(
                text = valueStyled.text,
                modifier = Modifier.fillMaxWidth(),
                style = valueStyled.style,
                textAlign = TextAlign.Start,
            )
            if (secondaryValue.isNotBlank()) {
                Text(
                    text = secondaryValueStyled.text,
                    modifier = Modifier.fillMaxWidth(),
                    style = secondaryValueStyled.style,
                    textAlign = TextAlign.Start,
                )
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SelectUsersComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp),
            ) {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
                    secondaryLabel = "It has a secondary label",
                    secondaryValue = "Secondary value",
                )
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
                )
            }
        }
    }
}

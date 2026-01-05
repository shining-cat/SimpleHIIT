package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.fitsInWidth
import fr.shiningcat.simplehiit.commonresources.R
import kotlin.math.floor

@Composable
fun SettingsFieldComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = "",
) {
    val settingsFieldLargePart = .8f
    val settingsFieldSmallPart = 1 - settingsFieldLargePart

    BoxWithConstraints(
        modifier =
            modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimensionResource(R.dimen.minimum_touch_size))
                .background(MaterialTheme.colorScheme.surface)
                .clickable { onClick() }
                .padding(dimensionResource(R.dimen.spacing_1)),
        contentAlignment = Alignment.Center,
    ) {
        val density = LocalDensity.current
        val availableWidthPix = with(density) { maxWidth.toPx() }

        val mainLabelStyled =
            TextLayoutInfo(
                text = label,
                style = MaterialTheme.typography.labelLarge,
            )
        val secondaryLabelStyled =
            TextLayoutInfo(
                text = secondaryLabel,
                style = MaterialTheme.typography.labelMedium,
            )
        val valueStyled =
            TextLayoutInfo(
                text = value,
                style = MaterialTheme.typography.labelLarge,
            )
        val secondaryValueStyled =
            TextLayoutInfo(
                text = secondaryValue,
                style = MaterialTheme.typography.labelMedium,
            )
        val oneThirdAvailableWidthPix = floor(availableWidthPix.times(settingsFieldSmallPart)).toInt()
        val twoThirdAvailableWidthPix = floor(availableWidthPix.times(settingsFieldLargePart)).toInt()
        val useHorizontalLayout =
            fitsInWidth(
                textLayoutInfos = listOf(mainLabelStyled),
                availableWidthPix = twoThirdAvailableWidthPix,
            ) &&
                fitsInWidth(
                    textLayoutInfos = listOf(valueStyled),
                    availableWidthPix = oneThirdAvailableWidthPix,
                ) &&
                fitsInWidth(
                    textLayoutInfos = listOf(secondaryLabelStyled),
                    availableWidthPix = twoThirdAvailableWidthPix,
                ) &&
                fitsInWidth(
                    textLayoutInfos = listOf(secondaryValueStyled),
                    availableWidthPix = oneThirdAvailableWidthPix,
                )
        val modifier =
            Modifier
                .fillMaxWidth()

        if (useHorizontalLayout) {
            Column(modifier) {
                Row {
                    Text(
                        modifier = Modifier.weight(settingsFieldLargePart),
                        text = mainLabelStyled.text,
                        style = mainLabelStyled.style,
                        textAlign = TextAlign.Start,
                    )
                    Text(
                        modifier = Modifier.weight(settingsFieldSmallPart),
                        text = valueStyled.text,
                        style = valueStyled.style,
                        textAlign = TextAlign.End,
                    )
                }
                Row {
                    if (secondaryLabel.isNotBlank()) {
                        Text(
                            modifier = Modifier.weight(settingsFieldLargePart),
                            text = secondaryLabelStyled.text,
                            style = secondaryLabelStyled.style,
                            textAlign = TextAlign.Start,
                        )
                    }
                    if (secondaryValue.isNotBlank()) {
                        Text(
                            modifier = Modifier.weight(settingsFieldSmallPart),
                            text = secondaryValueStyled.text,
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
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = valueStyled.text,
                    modifier = Modifier.fillMaxWidth(),
                    style = valueStyled.style,
                    textAlign = TextAlign.Center,
                )
                if (secondaryLabel.isNotBlank()) {
                    Text(
                        text = secondaryLabelStyled.text,
                        modifier = Modifier.fillMaxWidth(),
                        style = secondaryLabelStyled.style,
                        textAlign = TextAlign.Center,
                    )
                }
                if (secondaryValue.isNotBlank()) {
                    Text(
                        text = secondaryValueStyled.text,
                        modifier = Modifier.fillMaxWidth(),
                        style = secondaryValueStyled.style,
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SelectUsersComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_2)),
            ) {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
                    secondaryLabel = "It has a secondary label",
                    secondaryValue = "Secondary value",
                )
                SettingsFieldComponent(
                    label = "Compte a rebours de debut de session",
                    value = "5s",
                )
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
                )
            }
        }
    }
}

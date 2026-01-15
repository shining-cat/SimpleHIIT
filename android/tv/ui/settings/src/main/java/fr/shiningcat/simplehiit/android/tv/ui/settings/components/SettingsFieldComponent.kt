/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
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
import androidx.compose.ui.res.dimensionResource
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
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.fitsInWidth
import fr.shiningcat.simplehiit.android.tv.ui.common.components.transparentButtonTextColors
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

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
    val elementsFirstLine =
        listOf(
            mainLabelStyled,
            valueStyled,
        )
    val elementsSecondLine =
        listOf(
            secondaryLabelStyled,
            secondaryValueStyled,
        )
    // todo: this gets the device width and not the available container width
    val availableWidthPix = LocalWindowInfo.current.containerSize.width
    val useHorizontalLayout =
        fitsInWidth(
            textLayoutInfos = elementsFirstLine,
            availableWidthPix = availableWidthPix,
            spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_05),
        ) &&
            fitsInWidth(
                textLayoutInfos = elementsSecondLine,
                availableWidthPix = availableWidthPix,
                spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_05),
            )
    val modifier =
        modifier
            .fillMaxWidth()
            .padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1))
    Row(modifier = modifier) {
        Button(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = dimensionResource(CommonResourcesR.dimen.button_height))
                    .padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1)),
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
                        Text(text = label, style = typography.labelLarge)
                        if (secondaryLabel.isNotBlank()) {
                            Text(text = secondaryLabel, style = typography.labelMedium)
                        }
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = value,
                            style = typography.labelLarge,
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
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_025)),
                ) {
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
                    label = "Setting",
                    value = "2s",
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

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun NumberCyclesComponent(
    modifier: Modifier = Modifier,
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
) {
    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = R.string.number_of_cycle_setting_title),
        )
        Row(
            Modifier
                .padding(
                    horizontal = 0.dp,
                    vertical = dimensionResource(R.dimen.spacing_1),
                ).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val decreaseAllowed = numberOfCycles > 1
            IconButton(
                enabled = decreaseAllowed,
                onClick = decreaseNumberOfCycles,
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowDown,
                    contentDescription = stringResource(id = R.string.decrease),
                    tint = if (decreaseAllowed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                    modifier = Modifier.size(adaptDpToFontScale(dimensionResource(R.dimen.button_height))),
                )
            }
            Text(
                text =
                    stringResource(
                        id = R.string.number_of_cycle_setting,
                        numberOfCycles,
                        lengthOfCycle,
                    ),
                style = MaterialTheme.typography.headlineMedium,
                modifier = Modifier.padding(horizontal = dimensionResource(R.dimen.spacing_1)),
            )
            IconButton(
                onClick = increaseNumberOfCycles,
            ) {
                Icon(
                    imageVector = Icons.Default.KeyboardArrowUp,
                    contentDescription = stringResource(id = R.string.increase),
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(adaptDpToFontScale(dimensionResource(R.dimen.button_height))),
                )
            }
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = R.string.total_length, totalLengthFormatted),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun NumberCyclesComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            NumberCyclesComponent(
                numberOfCycles = 2,
                lengthOfCycle = "42s",
                totalLengthFormatted = "1mn 24s",
            )
        }
    }
}

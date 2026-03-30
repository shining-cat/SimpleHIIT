/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.BeepSoundType

@Composable
fun SettingsBeepSoundTypeComponent(
    beepSoundType: BeepSoundType,
    onSetBeepSoundType: (BeepSoundType) -> Unit = {},
    enabled: Boolean = true,
) {
    val toggleAction = {
        val newType = if (beepSoundType == BeepSoundType.HIGH) BeepSoundType.LOW else BeepSoundType.HIGH
        onSetBeepSoundType(newType)
    }
    Row(
        modifier =
            Modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = dimensionResource(R.dimen.minimum_touch_size))
                .background(MaterialTheme.colorScheme.surface)
                .then(
                    if (enabled) Modifier.clickable { toggleAction() } else Modifier,
                ).padding(horizontal = dimensionResource(R.dimen.spacing_1))
                .then(
                    if (!enabled) Modifier.alpha(0.38f) else Modifier,
                ),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = stringResource(R.string.beep_sound_type_low),
            style =
                MaterialTheme.typography.labelLarge.copy(
                    lineHeight = MaterialTheme.typography.labelLarge.fontSize,
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
        Switch(
            modifier =
                Modifier
                    .padding(horizontal = dimensionResource(R.dimen.spacing_1))
                    .graphicsLayer(
                        scaleX = 0.8f,
                        scaleY = 0.8f,
                        transformOrigin = TransformOrigin(0.5f, 0.5f),
                    ),
            checked = beepSoundType == BeepSoundType.HIGH,
            onCheckedChange = null,
            colors =
                SwitchDefaults.colors(
                    checkedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    checkedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    checkedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedTrackColor = MaterialTheme.colorScheme.surfaceVariant,
                    uncheckedThumbColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    uncheckedBorderColor = MaterialTheme.colorScheme.onSurfaceVariant,
                ),
        )
        Text(
            text = stringResource(R.string.beep_sound_type_high),
            style =
                MaterialTheme.typography.labelLarge.copy(
                    lineHeight = MaterialTheme.typography.labelLarge.fontSize,
                ),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SettingsBeepSoundTypeComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column {
                SettingsBeepSoundTypeComponent(
                    beepSoundType = BeepSoundType.LOW,
                    enabled = true,
                )
                SettingsBeepSoundTypeComponent(
                    beepSoundType = BeepSoundType.HIGH,
                    enabled = true,
                )
                SettingsBeepSoundTypeComponent(
                    beepSoundType = BeepSoundType.LOW,
                    enabled = false,
                )
            }
        }
    }
}

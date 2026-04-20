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
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Switch
import androidx.tv.material3.SwitchDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.transparentButtonTextColors
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.domain.common.models.BeepSoundType
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsBeepSoundTypeComponent(
    modifier: Modifier = Modifier,
    beepSoundType: BeepSoundType,
    onSetBeepSoundType: (BeepSoundType) -> Unit = {},
    enabled: Boolean = true,
) {
    val toggleAction = {
        val newType = if (beepSoundType == BeepSoundType.HIGH) BeepSoundType.LOW else BeepSoundType.HIGH
        onSetBeepSoundType(newType)
    }
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier =
                modifier
                    .defaultMinSize(minHeight = dimensionResource(CommonResourcesR.dimen.button_height))
                    .padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1))
                    .then(
                        if (!enabled) Modifier.alpha(0.38f) else Modifier,
                    ),
            onClick = { toggleAction() },
            enabled = enabled,
            colors = transparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    text = stringResource(CommonResourcesR.string.beep_sound_type_low),
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    modifier =
                        Modifier
                            .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_1))
                            .scale(0.7f),
                    checked = beepSoundType == BeepSoundType.HIGH,
                    onCheckedChange = { /*doing nothing here as we handle the action in the parent. This toggle is only for display*/ },
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
                    text = stringResource(CommonResourcesR.string.beep_sound_type_high),
                    style = MaterialTheme.typography.labelLarge,
                )
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SettingsBeepSoundTypeComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
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

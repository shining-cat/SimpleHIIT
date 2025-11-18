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
import androidx.compose.ui.draw.scale
import androidx.compose.ui.res.dimensionResource
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
import fr.shiningcat.simplehiit.android.tv.ui.settings.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SettingsToggleComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: Boolean,
    onToggle: () -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Button(
            modifier =
                modifier
                    .defaultMinSize(minHeight = dimensionResource(R.dimen.button_height))
                    .padding(bottom = dimensionResource(CommonResourcesR.dimen.spacing_1)),
            onClick = { onToggle() },
            colors = transparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Text(
                    modifier = Modifier.padding(end = dimensionResource(CommonResourcesR.dimen.spacing_1)),
                    text = label,
                    style = MaterialTheme.typography.labelLarge,
                )
                Switch(
                    modifier = Modifier.scale(0.7f),
                    checked = value,
                    onCheckedChange = { /*doing nothing here as we handle the action in the parent. This toggle is only for display*/ },
                    colors =
                        SwitchDefaults.colors(
                            checkedTrackColor = MaterialTheme.colorScheme.primary,
                            checkedThumbColor = MaterialTheme.colorScheme.secondary,
                            checkedBorderColor = MaterialTheme.colorScheme.primary,
                            uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                            uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                            uncheckedBorderColor = MaterialTheme.colorScheme.primary,
                        ),
                )
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SettingsToggleComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column {
                SettingsToggleComponent(
                    label = "This setting is on",
                    value = true,
                )
                SettingsToggleComponent(
                    label = "This setting is off",
                    value = false,
                )
            }
        }
    }
}

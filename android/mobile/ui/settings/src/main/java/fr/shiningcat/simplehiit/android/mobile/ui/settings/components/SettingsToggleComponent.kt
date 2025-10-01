package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SettingsToggleComponent(
    label: String,
    value: Boolean,
    onToggle: () -> Unit = {},
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = dimensionResource(R.dimen.minimum_touch_size))
            .padding(bottom = dimensionResource(R.dimen.spacing_1)),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(text = label, style = MaterialTheme.typography.headlineSmall)
        Switch(
            checked = value,
            onCheckedChange = { onToggle() },
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

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SettingsToggleComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
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

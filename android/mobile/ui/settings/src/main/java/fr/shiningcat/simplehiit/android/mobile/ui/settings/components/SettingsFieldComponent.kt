package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import android.content.res.Configuration
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme

@Composable
fun SettingsFieldComponent(
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = "",
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp)
            .clickable { onClick() },
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

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SelectUsersComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "Setting's value",
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

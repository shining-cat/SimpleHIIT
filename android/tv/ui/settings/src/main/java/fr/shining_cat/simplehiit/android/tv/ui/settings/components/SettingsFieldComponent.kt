package fr.shining_cat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@Composable
fun SettingsFieldComponent(
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = ""
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
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
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SelectUsersComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "Setting's value",
                    secondaryLabel = "It has a secondary label",
                    secondaryValue = "Secondary value",
                )
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "Setting's value"
                )
            }
        }
    }
}
package fr.shining_cat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.TransparentButtonTextColors
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsFieldComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: String,
    onClick: () -> Unit = {},
    secondaryLabel: String = "",
    secondaryValue: String = "",
    hiitLogger: HiitLogger? = null
) {
    Row (modifier = Modifier.fillMaxWidth()){
        Spacer(modifier = Modifier.weight(.15f))
        Button(
            modifier = modifier
                .weight(weight = .6f, fill = true)
                .defaultMinSize(minHeight = 48.dp)
                .padding(bottom = 8.dp),
            onClick = { onClick() },
            colors = TransparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
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
        Spacer(modifier = Modifier.weight(.15f))
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
private fun SettingsFieldComponentPreview() {
    SimpleHiitTvTheme {
        Surface(
            modifier = Modifier.width(1080.dp),
            shape = MaterialTheme.shapes.extraSmall
        ) {
            Column {
                SettingsFieldComponent(
                    label = "This is a Setting Field",
                    value = "value",
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
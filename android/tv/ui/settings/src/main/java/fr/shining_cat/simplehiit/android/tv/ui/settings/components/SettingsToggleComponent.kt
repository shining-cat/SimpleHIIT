package fr.shining_cat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
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
import androidx.tv.material3.Switch
import androidx.tv.material3.SwitchDefaults
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.TransparentButtonTextColors
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsToggleComponent(
    modifier: Modifier = Modifier,
    label: String,
    value: Boolean,
    onToggle: () -> Unit = {},
) {
    Row(modifier = Modifier.fillMaxWidth()) {
        Spacer(modifier = Modifier.weight(.15f))
        Button(
            modifier = modifier
                .weight(weight = .6f, fill = true)
                .defaultMinSize(minHeight = 48.dp)
                .padding(bottom = 8.dp),
            onClick = { onToggle() },
            colors = TransparentButtonTextColors(),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, style = MaterialTheme.typography.headlineSmall)
                Switch(
                    checked = value,
                    onCheckedChange = { /*doing nothing here as we handle the action in the parent. This toggle is only for display*/ },
                    colors = SwitchDefaults.colors(
                        checkedTrackColor = MaterialTheme.colorScheme.primary,
                        checkedThumbColor = MaterialTheme.colorScheme.secondary,
                        checkedBorderColor = MaterialTheme.colorScheme.primary,
                        uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                        uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                        uncheckedBorderColor = MaterialTheme.colorScheme.primary
                    )
                )
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
private fun SettingsToggleComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column {
                SettingsToggleComponent(
                    label = "This setting is on",
                    value = true
                )
                SettingsToggleComponent(
                    label = "This setting is off",
                    value = false
                )

            }
        }
    }
}
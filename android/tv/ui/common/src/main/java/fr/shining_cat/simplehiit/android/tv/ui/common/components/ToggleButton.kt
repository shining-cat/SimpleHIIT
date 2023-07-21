@file:OptIn(ExperimentalTvMaterial3Api::class)

package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    Button(
        onClick = { onToggle() },
        colors = ButtonDefaults.colors(
            containerColor = if(selected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface
        )
    ) {
        if (selected) {
            Icon(
                imageVector = Icons.Filled.Done,
                contentDescription = "Localized Description",
                modifier = Modifier.size(48.dp)
            )
        }
        Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
    }
}


// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ToggleButtonPreview() {
    SimpleHiitTvTheme {
        Surface {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                ToggleButton(
                    label = "I'm selected",
                    selected = true,
                    onToggle = {}
                )
                ToggleButton(
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {}
                )
                ToggleButton(
                    modifier = Modifier.width(86.dp), //causing a truncation
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {}
                )
            }
        }
    }
}

package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    if(selected) {
        Button(
            onClick = { onToggle() },
            colors = ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.primary,
                pressedContainerColor = MaterialTheme.colorScheme.primary,
                pressedContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.medium)
        ) {
            Row(
                modifier = Modifier.height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(48.dp)
                )
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    } else{
        OutlinedButton(
            onClick = { onToggle() },
            colors = ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.primary,
                pressedContainerColor = MaterialTheme.colorScheme.primary,
                pressedContentColor = MaterialTheme.colorScheme.onPrimary
            ),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.medium)
        ) {
            Row(
                modifier = Modifier.height(48.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}


// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ToggleButtonPreview() {
    SimpleHiitTvTheme {
        Surface {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
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
                    modifier = Modifier.width(124.dp), //causing a truncation
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {}
                )
            }
        }
    }
}

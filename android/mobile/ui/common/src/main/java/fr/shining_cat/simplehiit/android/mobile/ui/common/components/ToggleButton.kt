package fr.shining_cat.simplehiit.android.mobile.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onToggle: () -> Unit
) {
    FilterChip(
        modifier = modifier.height(48.dp),
        selected = selected,
        onClick = { onToggle() },
        label = { Text(text = label, maxLines = 1, overflow = TextOverflow.Ellipsis) },
        colors = FilterChipDefaults.filterChipColors(
            containerColor = MaterialTheme.colorScheme.surface,
            selectedContainerColor = MaterialTheme.colorScheme.primary
        ),
        leadingIcon = if (selected) {
            {
                Icon(
                    imageVector = Icons.Filled.Done,
                    contentDescription = "Localized Description",
                    modifier = Modifier.size(FilterChipDefaults.IconSize)
                )
            }
        } else {
            null
        }
    )
}

// Previews
@Preview(

    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChoiceDialogPreview() {
    SimpleHiitTheme {
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

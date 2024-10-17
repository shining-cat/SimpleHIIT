package fr.shiningcat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun ButtonToggle(
    modifier: Modifier = Modifier,
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
) {
    if (selected) {
        ButtonFilled(
            modifier = modifier,
            onClick = { onToggle() },
            label = label,
            icon = Icons.Filled.Done,
            iconContentDescription = R.string.active,
        )
    } else {
        ButtonBordered(
            modifier = modifier,
            onClick = { onToggle() },
            label = label,
        )
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
private fun ButtonTogglePreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonToggle(
                    modifier = Modifier.height(48.dp),
                    label = "I'm selected",
                    selected = true,
                    onToggle = {},
                )
                ButtonToggle(
                    modifier = Modifier.height(48.dp),
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
                ButtonToggle(
                    // causing a truncation
                    modifier =
                        Modifier
                            .height(48.dp)
                            .width(124.dp),
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
            }
        }
    }
}

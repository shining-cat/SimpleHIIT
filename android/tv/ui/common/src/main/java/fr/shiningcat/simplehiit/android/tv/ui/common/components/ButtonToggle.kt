package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun ButtonToggle(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    label: String,
    selected: Boolean,
    onToggle: () -> Unit,
) {
    if (selected) {
        ButtonFilled(
            modifier = modifier,
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            onClick = { onToggle() },
            label = label,
            icon = Icons.Filled.Done,
            iconContentDescription = R.string.active,
        )
    } else {
        ButtonBordered(
            modifier = modifier,
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            onClick = { onToggle() },
            label = label,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonTogglePreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.width(adaptDpToFontScale(300.dp)),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(48.dp)),
                    fillHeight = true,
                    label = "I'm selected",
                    selected = true,
                    onToggle = {},
                )
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(48.dp)),
                    fillHeight = true,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
                ButtonToggle(
                    // causing a truncation
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(48.dp))
                            .width(adaptDpToFontScale(128.dp)),
                    fillHeight = true,
                    fillWidth = true,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
            }
        }
    }
}

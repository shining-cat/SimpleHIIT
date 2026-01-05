package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun getToggleButtonLostWidthPix(): Float {
    val density = LocalDensity.current
    return with(density) {
        // FilterChip internal width components (when selected, i.e. with leading icon):
        // - Leading icon size: 18dp (FilterChipDefaults.IconSize)
        // - Icon spacing: 8dp (between icon and text)
        // - Start padding: 16dp (FilterChip default horizontal padding)
        // - End padding: 16dp (FilterChip default horizontal padding)
        // Total lost width = 18 + 8 + 16 + 16 = 58dp
        val iconSize = FilterChipDefaults.IconSize // 18dp
        val iconSpacing = dimensionResource(R.dimen.spacing_1) // 8dp
        val horizontalPadding = dimensionResource(R.dimen.spacing_2) * 2 // 16dp * 2 = 32dp
        (iconSize + iconSpacing + horizontalPadding).toPx()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ToggleButton(
    modifier: Modifier = Modifier,
    label: String,
    labelStyle: TextStyle,
    selected: Boolean,
    onToggle: () -> Unit,
) {
    FilterChip(
        modifier =
            modifier.defaultMinSize(
                minWidth = dimensionResource(R.dimen.minimum_touch_size),
                minHeight = dimensionResource(R.dimen.minimum_touch_size),
            ),
        selected = selected,
        onClick = { onToggle() },
        label = {
            Text(
                text = label,
                maxLines = 1,
                style = labelStyle,
                overflow = TextOverflow.Ellipsis,
            )
        },
        colors =
            FilterChipDefaults.filterChipColors(
                selectedLabelColor = MaterialTheme.colorScheme.onPrimary,
                labelColor = MaterialTheme.colorScheme.onSurface,
                containerColor = MaterialTheme.colorScheme.surface,
                selectedContainerColor = MaterialTheme.colorScheme.primary,
            ),
        leadingIcon =
            if (selected) {
                {
                    Icon(
                        imageVector = Icons.Filled.Done,
                        tint = MaterialTheme.colorScheme.onPrimary,
                        contentDescription = stringResource(R.string.active),
                        modifier = Modifier.size(adaptDpToFontScale(FilterChipDefaults.IconSize)),
                    )
                }
            } else {
                null
            },
    )
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun ToggleButtonPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly,
            ) {
                ToggleButton(
                    label = "I'm selected",
                    labelStyle = MaterialTheme.typography.labelMedium,
                    selected = true,
                    onToggle = {},
                )
                ToggleButton(
                    label = "I'm NOT selected",
                    labelStyle = MaterialTheme.typography.labelMedium,
                    selected = false,
                    onToggle = {},
                )
                ToggleButton(
                    // causing a truncation
                    modifier = Modifier.width(86.dp),
                    labelStyle = MaterialTheme.typography.labelMedium,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
            }
        }
    }
}

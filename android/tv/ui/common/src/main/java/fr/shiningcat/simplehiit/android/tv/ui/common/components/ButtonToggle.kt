package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.IconButtonDefaults.MediumIconSize
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun getToggleButtonLostWidthPix(): Float {
    val density = LocalDensity.current
    return with(density) {
        // ButtonFilled internal width components (when selected, i.e. with leading icon):
        // - Leading icon size: 24dp (MediumIconSize)
        // - Icon spacing: 8dp (ButtonDefaults.IconSpacing)
        // - Start padding: 16dp (spacing_2)
        // - End padding: 16dp (spacing_2)
        // Total lost width = 24 + 8 + 16 + 16 = 64dp
        val iconSize = MediumIconSize // 24dp
        val iconSpacing = ButtonDefaults.IconSpacing // 8dp
        val horizontalPadding = dimensionResource(CommonResourcesR.dimen.spacing_2) * 2 // 16dp * 2 = 32dp
        (iconSize + iconSpacing + horizontalPadding).toPx()
    }
}

@Composable
fun ButtonToggle(
    modifier: Modifier = Modifier,
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
    label: String,
    selected: Boolean,
    reserveIconSpace: Boolean = false,
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
            iconContentDescription = CommonResourcesR.string.active,
            reserveIconSpace = reserveIconSpace,
        )
    } else {
        ButtonBordered(
            modifier = modifier,
            fillWidth = fillWidth,
            fillHeight = fillHeight,
            onClick = { onToggle() },
            label = label,
            reserveIconSpace = reserveIconSpace,
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
                verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
            ) {
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    fillHeight = true,
                    label = "I'm selected",
                    selected = true,
                    onToggle = {},
                )
                ButtonToggle(
                    modifier = Modifier.height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height))),
                    fillHeight = true,
                    label = "I'm NOT selected",
                    selected = false,
                    onToggle = {},
                )
                ButtonToggle(
                    // causing a truncation
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
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

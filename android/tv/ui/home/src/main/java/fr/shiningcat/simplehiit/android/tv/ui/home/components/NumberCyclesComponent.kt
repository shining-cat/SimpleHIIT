package fr.shiningcat.simplehiit.android.tv.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.home.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun NumberCyclesComponent(
    modifier: Modifier = Modifier,
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
) {
    Column(
        modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = stringResource(id = CommonResourcesR.string.number_of_cycle_setting_title),
        )
        Row(
            Modifier
                .padding(
                    horizontal = 0.dp,
                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_2),
                ).fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            val minusButtonActive = numberOfCycles > 1
            ButtonFilled(
                modifier =
                    Modifier
                        .height(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                        .width(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                        .focusProperties { canFocus = minusButtonActive },
                fillHeight = true,
                fillWidth = true,
                onClick = decreaseNumberOfCycles,
                accentColor = false,
                icon = Icons.Filled.KeyboardArrowDown,
                iconContentDescription = CommonResourcesR.string.minus_cycle_description,
                enabled = minusButtonActive,
            )
            Spacer(modifier = Modifier.width(dimensionResource(CommonResourcesR.dimen.spacing_3)))
            Text(
                text =
                    stringResource(
                        id = CommonResourcesR.string.number_of_cycle_setting,
                        numberOfCycles,
                        lengthOfCycle,
                    ),
                style = MaterialTheme.typography.headlineMedium,
            )
            Spacer(modifier = Modifier.width(dimensionResource(CommonResourcesR.dimen.spacing_3)))
            ButtonFilled(
                modifier =
                    Modifier
                        .height(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                        .width(adaptDpToFontScale(dimensionResource(R.dimen.button_height))),
                fillHeight = true,
                fillWidth = true,
                onClick = increaseNumberOfCycles,
                accentColor = false,
                icon = Icons.Filled.KeyboardArrowUp,
                iconContentDescription = CommonResourcesR.string.plus_cycle_description,
            )
        }
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            text = stringResource(id = CommonResourcesR.string.total_length, totalLengthFormatted),
            style = MaterialTheme.typography.headlineSmall,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun NumberCyclesComponentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            NumberCyclesComponent(
                numberOfCycles = 3,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
            )
        }
    }
}

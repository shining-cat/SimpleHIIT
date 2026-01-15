/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun RemainingPercentageComponent(
    modifier: Modifier,
    label: String,
    percentage: Float,
    thickness: Dp,
    bicolor: Boolean,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_1)),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier,
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.basicMarquee(),
            maxLines = 1,
        )
        LinearProgressIndicator(
            progress = { percentage },
            modifier =
                Modifier
                    .height(thickness)
                    .fillMaxWidth(),
            color = MaterialTheme.colorScheme.secondary,
            trackColor = if (bicolor) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            strokeCap = StrokeCap.Butt,
            gapSize = 0.dp,
            drawStopIndicator = {},
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun RemainingPercentageComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    label = "Next exercise in 3s",
                    percentage = .3f,
                    thickness = dimensionResource(R.dimen.step_remaining_progress_thickness),
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    label = "Next rest in 7mn\u00A023s",
                    percentage = .8f,
                    thickness = dimensionResource(R.dimen.step_remaining_progress_thickness),
                    bicolor = false,
                )
                RemainingPercentageComponent(
                    modifier =
                        Modifier
                            .padding(horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2)),
                    label = "Total remaining: 1h\u00A020mn\u00A037s",
                    percentage = .79f,
                    thickness = dimensionResource(R.dimen.session_remaining_progress_thickness),
                    bicolor = true,
                )
            }
        }
    }
}

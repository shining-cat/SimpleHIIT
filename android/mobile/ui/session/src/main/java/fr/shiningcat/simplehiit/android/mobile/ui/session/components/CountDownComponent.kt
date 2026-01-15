/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.R
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.sharedui.session.CountDown
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * Displays a circular progress indicator with a countdown timer.
 *
 * @param baseSize The size of the component, will adapt to fontScale, but not to countDown value (more digits need more room)
 * @param countDown The [CountDown] data to display
 * @param hiitLogger An optional [HiitLogger] for logging. (Currently unused)
 */
@Composable
fun CountDownComponent(
    baseSize: Dp,
    countDown: CountDown,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val adaptedSize = adaptDpToFontScale(baseSize)
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(adaptedSize),
    ) {
        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
            progress = { countDown.progress },
            strokeWidth = dimensionResource(R.dimen.countdown_stroke_width),
            strokeCap = StrokeCap.Butt,
            trackColor = MaterialTheme.colorScheme.primary,
            color = MaterialTheme.colorScheme.secondary,
            gapSize = 0.dp,
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = countDown.secondsDisplay,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitMobileTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
            ) {
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "35.5", progress = 1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "21", progress = .9f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "17", progress = .7f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "9", progress = .5f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "4", progress = .3f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "3", progress = .2f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true),
                )
            }
        }
    }
}

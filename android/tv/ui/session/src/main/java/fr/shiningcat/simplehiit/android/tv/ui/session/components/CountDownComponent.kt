/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.CustomCircularProgressIndicator
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.sharedui.session.CountDown

/**
 * Composable function that displays a countdown timer that adapts to available screen space.
 *
 * The countdown timer uses a fraction of the available height to determine its size,
 * making it responsive to different TV screen sizes. It also adapts to font scale changes.
 *
 * @param modifier Modifier to be applied to the component
 * @param heightFraction Fraction of available height to use for countdown size (e.g., 0.33f for 1/3)
 * @param countDown The current countdown progress
 * @param hiitLogger An optional logger for logging purposes
 */
@Composable
fun CountDownComponent(
    modifier: Modifier = Modifier,
    heightFraction: Float,
    countDown: CountDown,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    BoxWithConstraints(
        modifier = modifier,
        contentAlignment = Alignment.Center,
    ) {
        val density = LocalDensity.current
        val availableHeightPx = with(density) { maxHeight.toPx() }
        val baseSizeDp = with(density) { (availableHeightPx * heightFraction).toDp() }
        val adaptedSize = adaptDpToFontScale(baseSizeDp)

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.size(adaptedSize),
        ) {
            val radius = adaptedSize.value
            val thickness = adaptedSize.value / 10
            CustomCircularProgressIndicator(
                modifier =
                    Modifier
                        .size(adaptedSize)
                        .background(Color.Transparent),
                currentValue = (countDown.progress * 100).toInt(),
                trackColor = MaterialTheme.colorScheme.primary,
                progressColor = MaterialTheme.colorScheme.secondary,
                circleRadius = radius,
                thickness = thickness,
                fillColor = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
                subDivisionsColor = MaterialTheme.colorScheme.secondary,
            )
            Text(
                modifier = Modifier.align(Alignment.Center),
                text = countDown.secondsDisplay,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.secondary,
            )
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = spacedBy(adaptDpToFontScale(dimensionResource(R.dimen.spacing_025))),
            ) {
                CountDownComponent(
                    modifier = Modifier.weight(1f),
                    heightFraction = 0.8f,
                    countDown = CountDown(secondsDisplay = "35", progress = 1f, playBeep = true),
                )
                CountDownComponent(
                    modifier = Modifier.weight(1f),
                    heightFraction = 0.8f,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true),
                )
                CountDownComponent(
                    modifier = Modifier.weight(1f),
                    heightFraction = 0.8f,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true),
                )
            }
        }
    }
}

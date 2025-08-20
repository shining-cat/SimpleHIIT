package fr.shiningcat.simplehiit.android.tv.ui.session.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.CustomCircularProgressIndicator
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.commonutils.HiitLogger

/**
 * Composable function that displays a countdown timer, dynamically adapts to fontscale
 *
 * This function takes a `baseSize` parameter to determine the size of the countdown timer,
 * a `countDown` parameter to display the current countdown progress, and an optional `hiitLogger`
 * parameter for logging purposes.
 *
 * The countdown timer is displayed as a circular progress indicator with the remaining seconds
 * displayed in the center. The progress indicator's color and thickness can be customized.
 *
 * @param baseSize The base size of the countdown timer. defaults to 100.dp, a reasonable size for a 3-digits display
 * @param countDown The current countdown progress.
 * @param hiitLogger An optional logger for logging purposes.
 */
@Composable
fun CountDownComponent(
    baseSize: Dp = 100.dp,
    countDown: CountDown,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val adaptedSize = adaptDpToFontScale(baseSize)
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

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = spacedBy(adaptDpToFontScale(2.dp)),
            ) {
                CountDownComponent(
                    baseSize = 100.dp,
                    countDown = CountDown(secondsDisplay = "35", progress = 1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 100.dp,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 100.dp,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true),
                )
            }
        }
    }
}

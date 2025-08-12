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
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.commonutils.HiitLogger

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
        // this first never-moving one is to simulate the trackColor from a LinearProgressIndicator
        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
            progress = { 1f },
            strokeWidth = 5.dp,
            color = MaterialTheme.colorScheme.primary,
        )
        CircularProgressIndicator(
            modifier =
                Modifier
                    .align(Alignment.Center)
                    .fillMaxSize(),
            progress = { countDown.progress },
            strokeWidth = 5.dp,
            color = MaterialTheme.colorScheme.secondary,
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
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "35.5", progress = 1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "21", progress = .9f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "17", progress = .7f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "9", progress = .5f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "4", progress = .3f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "3", progress = .2f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 48.dp,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true),
                )
            }
        }
    }
}

package fr.shiningcat.simplehiit.android.tv.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.CustomCircularProgressIndicator
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.commonutils.HiitLogger

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
@ExperimentalTvMaterial3Api
@Preview(
    name = "light mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 1.5",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 1.5f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Preview(
    name = "light mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    name = "dark mode, fontscale 2",
    showSystemUi = true,
    device = Devices.TV_1080p,
    fontScale = 2f,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Row(
                modifier = Modifier.fillMaxSize(),
            ) {
                CountDownComponent(
                    baseSize = 230.dp,
                    countDown = CountDown(secondsDisplay = "35", progress = 1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 230.dp,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true),
                )
                CountDownComponent(
                    baseSize = 230.dp,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true),
                )
            }
        }
    }
}

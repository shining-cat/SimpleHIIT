package fr.shining_cat.simplehiit.android.tv.ui.session.components

import android.content.res.Configuration
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.CustomCircularProgressIndicator
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.session.CountDown
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun CountDownComponent(
    size: Dp,
    countDown: CountDown,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        val radius = size.value// - 20f
        val thickness = size.value / 10
        CustomCircularProgressIndicator(
            modifier = Modifier
                .size(size)
                .background(Color.Transparent),
            currentValue = (countDown.progress * 100).toInt(),
            trackColor = MaterialTheme.colorScheme.primary,
            progressColor = MaterialTheme.colorScheme.secondary,
            circleRadius = radius,
            thickness = thickness ,
            fillColor = MaterialTheme.colorScheme.primary.copy(alpha = .3f),
            subDivisionsColor = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier.align(Alignment.Center),
            text = countDown.secondsDisplay,
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun CountDownCircularProgressPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                CountDownComponent(
                    size = 230.dp,
                    countDown = CountDown(secondsDisplay = "35", progress = 1f, playBeep = true)
                )
                CountDownComponent(
                    size = 230.dp,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true)
                )
                CountDownComponent(
                    size = 230.dp,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true)
                )
            }
        }
    }
}
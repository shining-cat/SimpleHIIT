package fr.shiningcat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.VectorConverter
import androidx.compose.animation.core.animateValue
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun BasicLoading(
    modifier: Modifier = Modifier,
    halfCycleDurationMs: Int = 1000,
    color: Color = MaterialTheme.colorScheme.primary,
    hiitLogger: HiitLogger? = null
) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        val infiniteTransition = rememberInfiniteTransition(label = "")

        val animatedPosition by infiniteTransition.animateValue(
            initialValue = 1,
            targetValue = 100,
            typeConverter = Int.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = halfCycleDurationMs * 2, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animatedPosition"
        )
        val animatedRotation by infiniteTransition.animateValue(
            initialValue = 0f,
            targetValue = 360f,
            typeConverter = Float.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(durationMillis = halfCycleDurationMs * 20, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "animatedRotation"
        )
        val animatedAlpha by infiniteTransition.animateValue(
            initialValue = 0f,
            targetValue = 1f,
            typeConverter = Float.VectorConverter,
            animationSpec = infiniteRepeatable(
                animation = tween(
                    durationMillis = halfCycleDurationMs,
                    easing = LinearOutSlowInEasing
                ),
                repeatMode = RepeatMode.Reverse
            ),
            label = "animatedAlpha"
        )

        CustomCircularProgressIndicator(
            modifier = modifier
                .rotate(animatedRotation)
                .alpha(animatedAlpha),
            currentValue = animatedPosition,
            maxValue = 100,
            circleRadius = 230f,
            thickness = 25f,
            progressColor = color,
            hiitLogger = hiitLogger
        )
    }
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
fun PreviewBasicLoading() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            BasicLoading()
        }
    }
}

package fr.shining_cat.simplehiit.android.mobile.ui.session.components

import android.content.res.Configuration
import android.media.MediaPlayer
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
import androidx.compose.runtime.DisposableEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.session.CountDown
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun CountDownComponent(
    size: Dp,
    countDown: CountDown,
    hiitLogger: HiitLogger? = null
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(size)
    ) {
        //this first never-moving one is to simulate the trackColor from a LinearProgressIndicator
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            progress = 1f,
            strokeWidth = 5.dp,
            color = MaterialTheme.colorScheme.primary
        )
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
                .fillMaxSize(),
            progress = countDown.progress,
            strokeWidth = 5.dp,
            color = MaterialTheme.colorScheme.secondary
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
    SimpleHiitTheme {
        Surface {
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "35", progress = 1f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "21", progress = .9f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "17", progress = .7f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "9", progress = .5f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "4", progress = .3f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "3", progress = .2f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "2", progress = .1f, playBeep = true)
                )
                CountDownComponent(
                    size = 48.dp,
                    countDown = CountDown(secondsDisplay = "0", progress = 0f, playBeep = true)
                )
            }
        }
    }
}
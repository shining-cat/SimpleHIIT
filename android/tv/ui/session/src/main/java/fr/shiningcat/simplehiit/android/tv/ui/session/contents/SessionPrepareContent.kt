package fr.shiningcat.simplehiit.android.tv.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.session.CountDown
import fr.shiningcat.simplehiit.android.tv.ui.session.SessionViewState
import fr.shiningcat.simplehiit.android.tv.ui.session.components.CountDownComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@Composable
fun SessionPrepareContent(
    viewState: SessionViewState.InitialCountDownSession,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Box(
        Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        CountDownComponent(
            size = 250.dp,
            countDown = viewState.countDown,
            hiitLogger = hiitLogger,
        )
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SessionPrepareContentPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SessionPrepareContent(
                viewState =
                    SessionViewState.InitialCountDownSession(
                        countDown =
                            CountDown(
                                secondsDisplay = "6",
                                progress = .9f,
                                playBeep = false,
                            ),
                    ),
            )
        }
    }
}

package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.CountDownComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger

@Composable
fun SessionPrepareContent(
    modifier: Modifier = Modifier,
    viewState: SessionViewState.InitialCountDownSession,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    Box(
        modifier = modifier.windowInsetsPadding(WindowInsets.safeDrawing),
        contentAlignment = Alignment.Center,
    ) {
        CountDownComponent(
            baseSize = 64.dp,
            countDown = viewState.countDown,
            hiitLogger = hiitLogger,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SessionPrepareContentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            SessionPrepareContent(
                modifier = Modifier,
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

package fr.shiningcat.simplehiit.android.mobile.ui.session.contents

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.session.CountDown
import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shiningcat.simplehiit.android.mobile.ui.session.components.CountDownComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger

/**
 * Displays the initial countdown before session starts.
 * The countdown size adapts to available screen space, using a fraction of the smaller dimension
 * to ensure it works well across different screen sizes and orientations.
 *
 * @param modifier Modifier to be applied to the component
 * @param viewState The current view state containing countdown information
 * @param hiitLogger An optional logger for debugging purposes
 */
@Composable
fun SessionPrepareContent(
    modifier: Modifier = Modifier,
    viewState: SessionViewState.InitialCountDownSession,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center,
    ) {
        val density = LocalDensity.current
        val availableHeightPx = with(density) { maxHeight.toPx() }
        val availableWidthPx = with(density) { maxWidth.toPx() }

        // Use 40% of the smaller dimension to ensure countdown fits well with margin,
        // even at 200% font scale (which effectively doubles the size via adaptDpToFontScale)
        val sizeFraction = 0.4f
        val baseSizePx = minOf(availableHeightPx, availableWidthPx) * sizeFraction
        val baseSizeDp = with(density) { baseSizePx.toDp() }

        CountDownComponent(
            baseSize = baseSizeDp,
            countDown = viewState.countDown,
            hiitLogger = hiitLogger,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SessionPrepareContentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            SessionPrepareContent(
                modifier = Modifier.fillMaxSize(),
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

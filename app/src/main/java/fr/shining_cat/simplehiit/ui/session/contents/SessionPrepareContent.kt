package fr.shining_cat.simplehiit.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.ui.session.CountDown
import fr.shining_cat.simplehiit.ui.session.SessionViewState
import fr.shining_cat.simplehiit.ui.session.components.CountDownComponent
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.utils.HiitLogger

@Composable
fun SessionPrepareContent(
    viewState: SessionViewState.InitialCountDownSession,
    hiitLogger: HiitLogger? = null
) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CountDownComponent(size = 64.dp, countDown = viewState.countDown, hiitLogger = hiitLogger)
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
private fun SessionPrepareContentPreview() {
    SimpleHiitTheme {
        Surface {
            SessionPrepareContent(
                viewState = SessionViewState.InitialCountDownSession(
                    countDown = CountDown(
                        secondsDisplay = "6",
                        progress = .9f,
                        playBeep = false
                    )
                )
            )
        }
    }
}
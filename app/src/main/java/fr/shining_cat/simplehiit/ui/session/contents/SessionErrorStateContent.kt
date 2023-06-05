package fr.shining_cat.simplehiit.ui.session.contents

import android.content.res.Configuration
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import fr.shining_cat.simplehiit.android.mobile.commonui.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.ui.session.SessionViewState
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun SessionErrorStateContent(
    screenViewState: SessionViewState.Error,
    hiitLogger: HiitLogger? = null
) {
    //TODO: build error state content
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
private fun SessionErrorStateContentPreview() {
    SimpleHiitTheme {
        Surface {
            SessionErrorStateContent(
                screenViewState = SessionViewState.Error(errorCode = "ABCD-123")
            )
        }
    }
}

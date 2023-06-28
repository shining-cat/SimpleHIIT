package fr.shining_cat.simplehiit.android.mobile.ui.session.contents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.session.SessionViewState
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.Constants

@Composable
fun SessionErrorStateContent(
    screenViewState: SessionViewState.Error,
    navigateUp: () -> Boolean,
    onAbort: () -> Unit,
    paddingValues: PaddingValues,
    hiitLogger: HiitLogger? = null
) {
    Column(
        modifier = Modifier
            .padding(paddingValues = paddingValues)
            .padding(8.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .padding(horizontal = 0.dp, vertical = 16.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
            text = if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                stringResource(id = R.string.error_session_abort)
            } else {
                stringResource(id = R.string.error_session_retry)
            },
            style = MaterialTheme.typography.headlineMedium,
        )
        if (screenViewState.errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp),
                text = stringResource(id = R.string.error_code, screenViewState.errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        val clickAction: () -> Unit = {
            if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                onAbort()
            } else {
                navigateUp()
            }
        }
        Button(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp),
            onClick = clickAction,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError
            )
        ) {
            Text(
                text = if (screenViewState.errorCode == Constants.Errors.SESSION_NOT_FOUND.code) {
                    stringResource(id = R.string.abort_session_button_label)
                } else {
                    stringResource(id = R.string.exit_button_label)
                }
            )
        }
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
private fun SessionErrorStateContentPreview(
    @PreviewParameter(SessionErrorStateContentPreviewParameterProvider::class) sessionViewState: SessionViewState.Error
) {
    SimpleHiitTheme {
        Surface {
            SessionErrorStateContent(
                screenViewState = sessionViewState,
                navigateUp = { true },
                onAbort = {},
                paddingValues = PaddingValues(0.dp)
            )
        }
    }
}

internal class SessionErrorStateContentPreviewParameterProvider :
    PreviewParameterProvider<SessionViewState.Error> {
    override val values: Sequence<SessionViewState.Error>
        get() = sequenceOf(
            SessionViewState.Error(errorCode = "ABCD-123"),
            SessionViewState.Error(errorCode = ""),
            SessionViewState.Error(errorCode = Constants.Errors.SESSION_NOT_FOUND.code)
        )
}

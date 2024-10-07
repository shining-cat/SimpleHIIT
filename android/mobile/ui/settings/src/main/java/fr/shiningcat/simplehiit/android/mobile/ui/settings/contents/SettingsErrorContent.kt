package fr.shiningcat.simplehiit.android.mobile.ui.settings.contents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SettingsErrorContent(
    errorCode: String,
    resetSettings: () -> Unit = {},
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
    ) {
        Image(
            modifier =
                Modifier
                    .size(120.dp)
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal = 0.dp, vertical = 16.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description),
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
            text = stringResource(id = R.string.error_irrecoverable_state_settings),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier =
                    Modifier
                        .padding(horizontal = 0.dp, vertical = 16.dp)
                        .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.error_code, errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Button(
            modifier =
                Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
            onClick = resetSettings,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.error,
                    contentColor = MaterialTheme.colorScheme.onError,
                ),
        ) {
            Text(text = stringResource(id = R.string.reset_settings_button_label))
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SettingsErrorContentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            SettingsErrorContent(errorCode = "ABCD-123")
        }
    }
}

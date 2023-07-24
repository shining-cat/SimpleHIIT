package fr.shining_cat.simplehiit.android.tv.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun HomeErrorContent(
    errorCode: String,
    resetWholeApp: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.SpaceEvenly,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            modifier = Modifier
                .size(120.dp)
                .align(Alignment.CenterHorizontally)
                .padding(horizontal = 0.dp, vertical = 16.dp),
            painter = painterResource(id = R.drawable.warning),
            contentDescription = stringResource(id = R.string.warning_icon_content_description)
        )
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(horizontal = 0.dp, vertical = 16.dp),
            text = stringResource(id = R.string.error_irrecoverable_state),
            style = MaterialTheme.typography.headlineMedium,
        )
        if (errorCode.isNotBlank()) {
            Text(
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(horizontal = 0.dp, vertical = 16.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(id = R.string.error_code, errorCode),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
        Button(
            onClick = resetWholeApp,
            colors = ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.error,
                pressedContainerColor = MaterialTheme.colorScheme.surface,
                pressedContentColor = MaterialTheme.colorScheme.onSurface
            ),
            shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small)
        ) {
            Text(text = stringResource(id = R.string.reset_app_button_label))
        }
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeErrorContentPreview() {
    SimpleHiitTvTheme {
        Surface {
            HomeErrorContent(
                errorCode = "An error happened",
                resetWholeApp = {}
            )
        }
    }
}


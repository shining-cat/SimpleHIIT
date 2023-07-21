package fr.shining_cat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Button
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NonInteractiveSurfaceDefaults
import androidx.tv.material3.OutlinedButton
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun WarningDialog(
    message: String = "",
    proceedButtonLabel: String,
    proceedAction: () -> Unit,
    dismissButtonLabel: String = stringResource(id = R.string.cancel_button_label),
    dismissAction: () -> Unit
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            colors = NonInteractiveSurfaceDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Image(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.CenterHorizontally)
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    painter = painterResource(id = R.drawable.warning),
                    contentDescription = stringResource(id = R.string.warning_icon_content_description)
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (dismissButtonLabel.isNotBlank()) {
                        OutlinedButton(onClick = dismissAction) {
                            Text(text = dismissButtonLabel)
                        }
                    }
                    Button(onClick = proceedAction) {
                        Text(text = proceedButtonLabel)
                    }
                }
            }
        }
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun WarningDialogPreview() {
    SimpleHiitTvTheme {
        Surface {
            WarningDialog(
                message = "This will erase all users, all stored sessions, and all settings",
                proceedButtonLabel = "Yeah",
                proceedAction = {},
                dismissButtonLabel = "Nope",
                dismissAction = {}
            )
        }
    }
}

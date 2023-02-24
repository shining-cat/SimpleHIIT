package fr.shining_cat.simplehiit.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
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
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun ConfirmDialog(
    message: String,
    buttonConfirmLabel: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    Dialog(onDismissRequest = onCancel) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
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
                    Button(
                        onClick = { onConfirm }

                    ) {
                        Text(text = buttonConfirmLabel)
                    }
                    OutlinedButton(onClick = { onCancel }) {
                        Text(text = stringResource(id = R.string.cancel_button_label))
                    }
                }
            }
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun ChoiceDialogPreview() {
    SimpleHiitTheme {
        ConfirmDialog(
            message = "This will erase all users, all stored sessions, and all settings",
            buttonConfirmLabel = "Delete",
            {}, {}
        )
    }
}

package fr.shining_cat.simplehiit.android.mobile.common.components

import android.content.res.Configuration
import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme

@Composable
fun ChoiceDialog(
    title: String = "",
    @DrawableRes image: Int = -1,
    imageContentDescription: String = "",
    message: String = "",
    primaryButtonLabel: String,
    primaryAction: () -> Unit,
    secondaryButtonLabel: String = "",
    secondaryAction: () -> Unit = {},
    dismissButtonLabel: String = stringResource(id = R.string.cancel_button_label),
    dismissAction: () -> Unit
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                if (title.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Left,
                        text = title,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                if(image != -1) {
                    Image(
                        modifier = Modifier
                            .size(120.dp)
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 0.dp, vertical = 24.dp),
                        painter = painterResource(id = image),
                        contentDescription = stringResource(id = R.string.warning_icon_content_description)
                    )
                }
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
                    if (secondaryButtonLabel.isNotBlank()) {
                        TextButton(onClick = secondaryAction) {
                            Text(text = secondaryButtonLabel)
                        }
                    }
                    if (dismissButtonLabel.isNotBlank()) {
                        OutlinedButton(onClick = dismissAction) {
                            Text(text = dismissButtonLabel)
                        }
                    }
                    Button(onClick = primaryAction) {
                        Text(text = primaryButtonLabel)
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
        ChoiceDialog(
            message = "This will erase all users, all stored sessions, and all settings",
            primaryButtonLabel = "Yeah",
            primaryAction = {},
            secondaryButtonLabel = "Maybe",
            dismissButtonLabel = "Nope",
            dismissAction = {}
        )
    }
}

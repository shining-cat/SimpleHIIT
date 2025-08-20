package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun WarningDialog(
    message: String = "",
    proceedButtonLabel: String,
    proceedAction: () -> Unit,
    dismissButtonLabel: String = stringResource(id = R.string.cancel_button_label),
    dismissAction: () -> Unit,
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            colors =
                SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Image(
                    modifier =
                        Modifier
                            .size(adaptDpToFontScale(120.dp))
                            .align(Alignment.CenterHorizontally)
                            .padding(horizontal = 0.dp, vertical = 24.dp),
                    painter = painterResource(id = R.drawable.warning),
                    contentDescription = stringResource(id = R.string.warning_icon_content_description),
                )
                Text(
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
                    text = message,
                    style = MaterialTheme.typography.bodyMedium,
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    if (dismissButtonLabel.isNotBlank()) {
                        ButtonBordered(
                            modifier =
                                Modifier
                                    .height(adaptDpToFontScale(48.dp))
                                    .weight(1f),
                            fillWidth = true,
                            fillHeight = true,
                            onClick = dismissAction,
                            label = dismissButtonLabel,
                        )
                    }
                    ButtonFilled(
                        modifier =
                            Modifier
                                .height(adaptDpToFontScale(48.dp))
                                .weight(1f),
                        fillWidth = true,
                        fillHeight = true,
                        onClick = proceedAction,
                        label = proceedButtonLabel,
                    )
                }
            }
        }
    }
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun WarningDialogPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            WarningDialog(
                message = "This will erase all users, all stored sessions, and all settings",
                proceedButtonLabel = "Yeah",
                proceedAction = {},
                dismissButtonLabel = "Nope",
                dismissAction = {},
            )
        }
    }
}

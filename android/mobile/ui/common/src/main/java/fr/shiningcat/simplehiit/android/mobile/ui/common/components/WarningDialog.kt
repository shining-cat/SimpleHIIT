package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.utils.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.common.ui.utils.ButtonType
import fr.shiningcat.simplehiit.android.common.ui.utils.DialogButtonConfig
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun WarningDialog(
    message: String = "",
    proceedButtonLabel: String,
    proceedAction: () -> Unit,
    dismissButtonLabel: String = stringResource(id = R.string.cancel_button_label),
    dismissAction: () -> Unit,
) {
    val dialogPadding = 8.dp

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
        ) {
            BoxWithConstraints {
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding
                Column(
                    modifier =
                        Modifier
                            .padding(8.dp)
                            .fillMaxWidth(),
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
                        modifier =
                            Modifier
                                .padding(horizontal = 0.dp, vertical = 24.dp)
                                .fillMaxWidth(),
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    val primaryButtonInfo =
                        DialogButtonConfig(
                            label = proceedButtonLabel,
                            style = MaterialTheme.typography.labelMedium,
                            type = ButtonType.FILLED,
                            onClick = proceedAction,
                        )
                    val dismissButtonInfo =
                        if (dismissButtonLabel.isNotBlank()) {
                            DialogButtonConfig(
                                label = dismissButtonLabel,
                                style = MaterialTheme.typography.labelMedium,
                                type = ButtonType.OUTLINED,
                                onClick = dismissAction,
                            )
                        } else {
                            null
                        }
                    val buttons = listOfNotNull(primaryButtonInfo, dismissButtonInfo)
                    val buttonsSpacingDp = 12.dp
                    AdaptiveDialogButtonsLayout(
                        buttons = buttons,
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
                        dialogContentWidthDp = effectiveDialogContentWidthDp,
                        horizontalSpacingDp = buttonsSpacingDp,
                        verticalSpacingDp = buttonsSpacingDp,
                    )
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun WarningDialogPreview() {
    SimpleHiitMobileTheme {
        Surface {
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

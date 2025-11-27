package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.ButtonType
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.DialogButtonConfig
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun PauseDialog(
    onResume: () -> Unit,
    onAbort: () -> Unit,
) {
    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)
    val title = stringResource(id = CommonResourcesR.string.pause)
    val message = stringResource(id = CommonResourcesR.string.pause_explanation)
    val abortButtonLabel = stringResource(id = CommonResourcesR.string.abort_session_button_label)
    val resumeButtonLabel = stringResource(CommonResourcesR.string.resume_button_label)

    Dialog(onDismissRequest = onResume) {
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
                            .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                            .fillMaxWidth(),
                ) {
                    if (title.isNotBlank()) {
                        Text(
                            textAlign = TextAlign.Left,
                            text = title,
                            style = MaterialTheme.typography.headlineSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                        )
                    }
                    Text(
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier.padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            ),
                        text = message,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                    PauseDialogButtonsLayout(
                        actionButtonLabel = abortButtonLabel,
                        onActionClick = onAbort,
                        dismissButtonLabel = resumeButtonLabel,
                        onDismissClick = onResume,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
                    )
                }
            }
        }
    }
}

@Composable
private fun PauseDialogButtonsLayout(
    actionButtonLabel: String,
    onActionClick: () -> Unit,
    dismissButtonLabel: String,
    onDismissClick: () -> Unit,
    effectiveDialogContentWidthDp: Dp,
) {
    val primaryButtonInfo =
        if (dismissButtonLabel.isNotBlank()) {
            DialogButtonConfig(
                label = actionButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.FILLED,
                onClick = onActionClick,
            )
        } else {
            null
        }
    val dismissButtonInfo =
        if (dismissButtonLabel.isNotBlank()) {
            DialogButtonConfig(
                label = dismissButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.TEXT,
                onClick = onDismissClick,
            )
        } else {
            null
        }

    val buttons = listOfNotNull(dismissButtonInfo, primaryButtonInfo)
    val buttonsSpacingDp = dimensionResource(CommonResourcesR.dimen.spacing_15)
    AdaptiveDialogButtonsLayout(
        buttons = buttons,
        modifier =
            Modifier.padding(
                horizontal = 0.dp,
                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
            ),
        dialogContentWidthDp = effectiveDialogContentWidthDp,
        horizontalSpacingDp = buttonsSpacingDp,
        verticalSpacingDp = buttonsSpacingDp,
    )
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun ChoiceDialogPreview() {
    SimpleHiitMobileTheme {
        Surface {
            // The Surface here is just for the preview's background, not the dialog's
            PauseDialog(
                onAbort = {},
                onResume = {},
            )
        }
    }
}

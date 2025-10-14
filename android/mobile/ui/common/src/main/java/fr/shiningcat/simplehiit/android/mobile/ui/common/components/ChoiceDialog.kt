package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.utils.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.common.ui.utils.ButtonType
import fr.shiningcat.simplehiit.android.common.ui.utils.DialogButtonConfig
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.R
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun ChoiceDialog(
    title: String = "",
    @DrawableRes image: Int = -1,
    @StringRes imageContentDescription: Int = -1,
    message: String = "",
    primaryButtonLabel: String,
    primaryAction: () -> Unit,
    secondaryButtonLabel: String? = null,
    secondaryAction: () -> Unit = {},
    dismissButtonLabel: String? = null,
    dismissAction: () -> Unit,
) {
    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

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
                    if (image != -1) {
                        Image(
                            modifier =
                                Modifier
                                    .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                                    .align(Alignment.CenterHorizontally)
                                    .padding(
                                        horizontal = 0.dp,
                                        vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                    ),
                            painter = painterResource(id = image),
                            contentDescription = stringResource(id = imageContentDescription),
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
                    ChoiceDialogButtonsLayout(
                        primaryButtonLabel = primaryButtonLabel,
                        onPrimaryClick = primaryAction,
                        secondaryButtonLabel = secondaryButtonLabel,
                        onSecondaryClick = secondaryAction,
                        dismissButtonLabel = dismissButtonLabel,
                        onDismissClick = dismissAction,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
                    )
                }
            }
        }
    }
}

@Composable
private fun ChoiceDialogButtonsLayout(
    primaryButtonLabel: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonLabel: String?,
    onSecondaryClick: () -> Unit,
    dismissButtonLabel: String?,
    onDismissClick: () -> Unit,
    effectiveDialogContentWidthDp: Dp,
) {
    val primaryButtonInfo =
        DialogButtonConfig(
            label = primaryButtonLabel,
            style = MaterialTheme.typography.labelMedium,
            type = ButtonType.FILLED,
            onClick = onPrimaryClick,
        )
    val secondaryButtonInfo =
        if (secondaryButtonLabel.isNullOrBlank().not()) {
            DialogButtonConfig(
                label = secondaryButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.OUTLINED,
                onClick = onSecondaryClick,
            )
        } else {
            null
        }
    val dismissButtonInfo =
        if (dismissButtonLabel.isNullOrBlank().not()) {
            DialogButtonConfig(
                label = dismissButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.TEXT,
                onClick = onDismissClick,
            )
        } else {
            null
        }

    val buttons = listOfNotNull(dismissButtonInfo, secondaryButtonInfo, primaryButtonInfo)
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
            ChoiceDialog(
                message = "This will erase all users, all stored sessions, and all settings",
                primaryButtonLabel = "Yeah",
                primaryAction = {},
                secondaryButtonLabel = "Maybe",
                dismissButtonLabel = "Nope",
                dismissAction = {},
            )
        }
    }
}

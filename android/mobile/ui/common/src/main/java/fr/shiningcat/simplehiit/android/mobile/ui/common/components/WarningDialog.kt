/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.common.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.R
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.ButtonType
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.DialogButtonConfig
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptiveDialogWidth
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun WarningDialog(
    message: String = "",
    proceedButtonLabel: String,
    proceedAction: () -> Unit,
    dismissButtonLabel: String = stringResource(id = CommonResourcesR.string.cancel_button_label),
    dismissAction: () -> Unit,
) {
    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

    Dialog(
        onDismissRequest = dismissAction,
        properties = adaptiveDialogProperties(),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.adaptiveDialogWidth(),
        ) {
            BoxWithConstraints {
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding
                Column(
                    modifier =
                        Modifier
                            .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                            .fillMaxWidth()
                            .verticalScroll(rememberScrollState()),
                ) {
                    Image(
                        modifier =
                            Modifier
                                .size(adaptDpToFontScale(dimensionResource(R.dimen.dialog_main_icon_size)))
                                .align(Alignment.CenterHorizontally)
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                ),
                        painter = painterResource(id = CommonResourcesR.drawable.warning),
                        contentDescription = stringResource(id = CommonResourcesR.string.warning_icon_content_description),
                    )
                    Text(
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                ).fillMaxWidth(),
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

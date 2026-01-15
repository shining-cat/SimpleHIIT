/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun DialogWarning(
    message: String = "",
    proceedButtonLabel: String,
    proceedAction: () -> Unit,
    dismissButtonLabel: String = stringResource(id = CommonResourcesR.string.cancel_button_label),
    dismissAction: () -> Unit,
) {
    DialogContentLayout(
        onDismissRequest = dismissAction,
        image = CommonResourcesR.drawable.warning,
        imageContentDescription = CommonResourcesR.string.warning_icon_content_description,
        content = {
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
        },
        buttons = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_3)),
            ) {
                if (dismissButtonLabel.isNotBlank()) {
                    ButtonBordered(
                        modifier =
                            Modifier
                                .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
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
                            .height(adaptDpToFontScale(dimensionResource(R.dimen.dialog_standard_button_height)))
                            .weight(1f),
                    fillWidth = true,
                    fillHeight = true,
                    onClick = proceedAction,
                    label = proceedButtonLabel,
                )
            }
        },
    )
}

// Previews
@PreviewTvScreensNoUi
@Composable
private fun DialogWarningPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            DialogWarning(
                message = "This will erase all users, all stored sessions, and all settings",
                proceedButtonLabel = "Yeah",
                proceedAction = {},
                dismissButtonLabel = "Nope",
                dismissAction = {},
            )
        }
    }
}

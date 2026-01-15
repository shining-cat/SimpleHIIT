/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.R
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * Internal composable that provides the shared structure for all dialog components.
 *
 * This composable centralizes the common Dialog/Surface/Column pattern used across
 * DialogChoice, DialogWarning, and DialogError. It handles:
 * - Dialog wrapper with dismiss logic
 * - Surface styling with theme colors
 * - Column layout with consistent padding
 * - Optional title display
 * - Optional image/icon display
 * - Content slot for custom dialog content
 * - Buttons slot with proper spacing to prevent focus zoom overflow
 *
 * The buttons slot includes extra padding to accommodate TV Material3's default
 * focus scale effect (typically 1.1x zoom), preventing buttons from overlapping
 * with dialog edges when focused.
 *
 * @param onDismissRequest Called when the user tries to dismiss the dialog.
 * @param title Optional title text to display at the top of the dialog.
 * @param titleStyle Typography style for the title. Defaults to headlineSmall.
 * @param image Optional drawable resource for an icon/image to display.
 * @param imageContentDescription String resource for the image's content description.
 * @param horizontalAlignment Horizontal alignment for the column content. Defaults to CenterHorizontally.
 * @param content Composable lambda for the main dialog content (messages, etc.).
 * @param buttons Composable lambda for the button row or button(s) at the bottom.
 */
@Composable
fun DialogContentLayout(
    onDismissRequest: () -> Unit,
    title: String? = null,
    titleStyle: androidx.compose.ui.text.TextStyle = MaterialTheme.typography.headlineSmall,
    @DrawableRes image: Int? = null,
    @StringRes imageContentDescription: Int? = null,
    horizontalAlignment: Alignment.Horizontal = Alignment.CenterHorizontally,
    content: @Composable ColumnScope.() -> Unit,
    buttons: @Composable ColumnScope.() -> Unit,
) {
    Dialog(onDismissRequest = onDismissRequest) {
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
                        .padding(
                            horizontal = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                        ).fillMaxWidth(),
                horizontalAlignment = horizontalAlignment,
            ) {
                // Optional title
                if (title != null && title.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(
                                    horizontal = 0.dp,
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_05),
                                ),
                        text = title,
                        style = titleStyle,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }

                // Optional image/icon
                if (image != null) {
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
                        contentDescription =
                            if (imageContentDescription != null) {
                                stringResource(id = imageContentDescription)
                            } else {
                                ""
                            },
                    )
                }

                // Main content
                content()

                // Buttons with extra padding to handle focus zoom
                // TV Material3 buttons typically scale to ~1.1x on focus
                // This padding prevents the zoomed button from touching dialog edges
                Column(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2),
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            ),
                    verticalArrangement = Arrangement.spacedBy(dimensionResource(CommonResourcesR.dimen.spacing_2)),
                ) {
                    buttons()
                }
            }
        }
    }
}

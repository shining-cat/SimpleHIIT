/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Icon
import androidx.tv.material3.IconButtonDefaults.LargeIconSize
import androidx.tv.material3.IconButtonDefaults.MediumIconSize
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * Internal composable that provides the shared content layout for all button components.
 *
 * This composable centralizes the common Row layout pattern used across ButtonFilled,
 * ButtonError, ButtonBordered, and ButtonText. It handles:
 * - Icon positioning and sizing
 * - Text layout with overflow handling
 * - Icon space reservation for consistent widths
 * - Responsive width/height filling
 *
 * @param fillWidth Whether the content should fill the available width.
 * @param fillHeight Whether the content should fill the available height.
 * @param label The text to display. If null, no text is displayed.
 * @param icon The icon to display. If null, no icon is displayed.
 * @param iconContentDescription The content description resource ID for the icon. Null if no description.
 * @param reserveIconSpace When true, reserves space for an icon even when icon is null.
 */
@Composable
internal fun ButtonContentLayout(
    fillWidth: Boolean,
    fillHeight: Boolean,
    label: String?,
    icon: ImageVector?,
    @StringRes
    iconContentDescription: Int? = null,
    reserveIconSpace: Boolean = false,
) {
    val rowModifier =
        Modifier
            .run { if (fillWidth) fillMaxWidth() else this }
            .run { if (fillHeight) fillMaxHeight() else this }
            .padding(
                horizontal = dimensionResource(CommonResourcesR.dimen.spacing_2),
                vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
            )

    Row(
        modifier = rowModifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
    ) {
        if (icon != null) {
            Icon(
                imageVector = icon,
                modifier =
                    Modifier
                        .size(adaptDpToFontScale(LargeIconSize))
                        .run { if (label != null) padding(end = ButtonDefaults.IconSpacing) else this },
                contentDescription = iconContentDescription?.let { stringResource(id = it) } ?: "",
            )
        } else if (reserveIconSpace) {
            // Reserve space for icon to match width with buttons that have icons
            Spacer(
                modifier =
                    Modifier
                        .size(adaptDpToFontScale(MediumIconSize))
                        .run { if (label != null) padding(end = ButtonDefaults.IconSpacing) else this },
            )
        }
        if (label != null) {
            Text(
                text = label,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

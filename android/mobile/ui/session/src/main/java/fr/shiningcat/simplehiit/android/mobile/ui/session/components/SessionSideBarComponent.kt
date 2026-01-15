/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.session.components

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.SideBarItem
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R

@Composable
fun SessionSideBarComponent(
    @DrawableRes icon: Int,
    @StringRes label: Int,
    onClick: () -> Unit = {},
) {
    NavigationRail(
        modifier = Modifier.fillMaxHeight(),
        containerColor = MaterialTheme.colorScheme.primary,
        contentColor = MaterialTheme.colorScheme.onPrimary,
    ) {
        SideBarItem(
            onClick = onClick,
            icon = icon,
            label = label,
            selected = false,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SessionSideBarComponentPreview() {
    SimpleHiitMobileTheme {
        Surface {
            SessionSideBarComponent(
                label = R.string.pause,
                icon = R.drawable.pause,
            )
        }
    }
}

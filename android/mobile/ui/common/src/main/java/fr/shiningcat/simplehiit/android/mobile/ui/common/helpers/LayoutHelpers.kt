/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.common.helpers

import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.WindowInsetsSides
import androidx.compose.foundation.layout.displayCutout
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.only
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement

/**
 * Applies appropriate window insets for main content in horizontal/vertical layouts.
 *
 * This modifier handles edge-to-edge insets differently based on UI arrangement:
 *
 * **Vertical Mode:**
 * - Bottom: Safe drawing insets (avoids system navigation bar)
 *
 * **Horizontal Mode:**
 * - Bottom: Safe drawing insets (avoids system navigation bar)
 * - End: Display cutout padding (protects content from camera cutout)
 * - End: Navigation bar padding (protects content from button-based navigation bar)
 * - Top: Status bar padding
 *
 * **Important:** This should be applied to the main content Column, NOT to containers
 * that include NavigationSideBar, as NavigationSideBar handles its own start-side
 * insets via [androidx.compose.material3.NavigationRailDefaults.windowInsets].
 *
 * @param uiArrangement The current UI arrangement (HORIZONTAL or VERTICAL)
 * @return Modifier with appropriate window insets applied
 *
 * @see fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
 */
@Composable
fun Modifier.mainContentInsets(uiArrangement: UiArrangement): Modifier =
    this
        .windowInsetsPadding(WindowInsets.safeDrawing.only(WindowInsetsSides.Bottom))
        .run {
            if (uiArrangement == UiArrangement.HORIZONTAL) {
                this
                    .windowInsetsPadding(WindowInsets.displayCutout.only(WindowInsetsSides.End))
                    .windowInsetsPadding(WindowInsets.navigationBars.only(WindowInsetsSides.End))
                    .windowInsetsPadding(WindowInsets.statusBars)
            } else {
                this
            }
        }

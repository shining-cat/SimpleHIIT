/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SelectUsersComponentVertical(
    modifier: Modifier = Modifier,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val availableWidthDp = maxWidth

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = stringResource(id = CommonResourcesR.string.selected_users_setting_title),
                style = MaterialTheme.typography.headlineLarge,
            )
            // when we have less than maxLines users, show them full width,
            // if there's 2 x maxLines or less show 2 columns
            // if there's more than 2 x maxLines show unlimited number of columns but tweak the buttons' width to make clear there are more to scroll on the right
            val maxLines = 3
            val spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_1)
            val minButtonWidthDp =
                max(
                    dimensionResource(R.dimen.user_select_toggle_button_min_width),
                    dimensionResource(CommonResourcesR.dimen.minimum_touch_size),
                )
            val buttonWidth =
                when {
                    (users.size < maxLines + 1) -> {
                        val singleColumnWidthDP = availableWidthDp - spacingDp * 2
                        // should use all available width, so no need to handle different fontscales
                        max(singleColumnWidthDP, minButtonWidthDp)
                    }
                    (users.size < 2 * maxLines + 1) -> {
                        val twoColumnsWidthDP = (availableWidthDp - spacingDp * 3).div(2)
                        // should use half of the available width, scale up with fontscale but not down
                        val baseWidth = max(twoColumnsWidthDP, minButtonWidthDp)
                        adaptDpToFontScale(baseSize = baseWidth, coerceMinSize = baseWidth)
                    }
                    else -> {
                        val sneakPeekDp = 20.dp
                        val moreColumnsWidthDP = (availableWidthDp - spacingDp * 3).div(2) - sneakPeekDp
                        // should use half of the available width, scale up with fontscale but not down
                        val baseWidth = max(moreColumnsWidthDP, minButtonWidthDp)
                        adaptDpToFontScale(baseSize = baseWidth, coerceMinSize = baseWidth)
                    }
                }
            val buttonHeight = dimensionResource(R.dimen.user_select_toggle_button_height)
            val gridHeight = buttonHeight * maxLines + spacingDp * (maxLines - 1)
            LazyHorizontalGrid(
                rows = GridCells.Fixed(maxLines),
                modifier =
                    Modifier
                        .padding(top = spacingDp)
                        .height(gridHeight),
                contentPadding = PaddingValues(horizontal = spacingDp),
                verticalArrangement = Arrangement.spacedBy(spacingDp),
                horizontalArrangement = Arrangement.spacedBy(spacingDp),
            ) {
                items(users) { user ->
                    ToggleButton(
                        modifier =
                            Modifier
                                .height(buttonHeight)
                                .width(buttonWidth),
                        label = user.name,
                        labelStyle = MaterialTheme.typography.labelMedium,
                        selected = user.selected,
                        onToggle = { toggleSelectedUser(user) },
                    )
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Preview(name = "Phone", device = PHONE, showSystemUi = true)
@Preview(name = "Unfolded Foldable", device = FOLDABLE, showSystemUi = true)
@Preview(
    name = "Tablet",
    device = "spec:width=1280dp,height=800dp,dpi=240,orientation=portrait",
    showSystemUi = true,
)
@Composable
private fun SelectUsersComponentPreview(
    @PreviewParameter(SelectUsersComponentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
            SelectUsersComponentVertical(users = users)
        }
    }
}

internal class SelectUsersComponentPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                listOf(User(123L, "User 1", selected = true)),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
                ),
            )
}

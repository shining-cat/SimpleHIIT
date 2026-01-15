/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.R
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsUsersComponent(
    users: List<User>,
    onClickUser: (User) -> Unit = {},
    onAddUser: () -> Unit = {},
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        val spacing = dimensionResource(CommonResourcesR.dimen.spacing_3)
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = spacing),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = CommonResourcesR.string.users_list_setting_label),
        )

        if (users.isNotEmpty()) {
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(spacing),
                maxItemsInEachRow = 3,
            ) {
                users.forEach { user ->
                    ButtonBordered(
                        onClick = { onClickUser(user) },
                        label = user.name,
                    )
                }
            }
        }

        ButtonFilled(
            modifier =
                Modifier
                    .padding(top = spacing)
                    .height(adaptDpToFontScale(dimensionResource(CommonResourcesR.dimen.button_height)))
                    .width(dimensionResource(R.dimen.settings_add_user_button_width)),
            fillWidth = true,
            fillHeight = true,
            onClick = onAddUser,
            accentColor = true,
            icon = Icons.Filled.Add,
            iconContentDescription = CommonResourcesR.string.add_user_button_label,
        )
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SettingsUsersComponentPreview(
    // Renamed for clarity
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsUsersComponent(users = users)
        }
    }
}

internal class SettingsUsersComponentPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser =
        listOf(
            User(123L, "User 1", selected = true),
            User(234L, "User pouet 2", selected = false),
            User(345L, "User ping 3", selected = true),
            User(345L, "User 4 has a very very long name", selected = true),
            User(123L, "User tralala 5", selected = true),
            User(
                234L,
                "User tudut 6 with an even longer name to test wrapping",
                selected = false,
            ),
            User(345L, "User toto 7", selected = true),
            User(345L, "UserWithAVeryLongNameIndeed 8", selected = true),
            User(345L, "User 9", selected = true),
        )

    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                emptyList(),
                listOfOneUser,
                listOfTwoUser,
                listOfMoreUser,
            )
}

package fr.shiningcat.simplehiit.android.mobile.ui.settings.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun SettingsUsersComponent(
    users: List<User>,
    onClickUser: (User) -> Unit = {},
    onAddUser: () -> Unit = {},
) {
    Column(
        modifier = Modifier.padding(horizontal = 8.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.users_list_setting_label),
        )
        Spacer(modifier = Modifier.height(8.dp))
        val itemHeight = adaptDpToFontScale(48.dp)
        val spacing = 8.dp

        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            verticalArrangement = Arrangement.spacedBy(spacing),
            // Allow items to fill rows naturally
            maxItemsInEachRow = Int.MAX_VALUE,
        ) {
            users.forEach { user ->
                OutlinedButton(
                    modifier =
                        Modifier
                            .height(itemHeight)
                            .defaultMinSize(minWidth = 80.dp),
                    onClick = { onClickUser(user) },
                ) {
                    Text(text = user.name)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp)) // Add some space before the add button
        Button(
            modifier =
                Modifier
                    .height(itemHeight)
                    .width(adaptDpToFontScale(200.dp))
                    .padding(horizontal = 32.dp),
            onClick = onAddUser,
            colors =
                ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.secondary,
                    contentColor = MaterialTheme.colorScheme.onSecondary,
                ),
        ) {
            Icon(
                imageVector = Icons.Filled.Add,
                contentDescription = stringResource(id = R.string.add_user_button_label),
                modifier = Modifier.size(adaptDpToFontScale(32.dp)),
                tint = MaterialTheme.colorScheme.onSecondary,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Composable
private fun SettingsUsersComponentPreviewPhonePortrait(
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
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
            User(345L, "User 4 has a very long name", selected = true),
            User(123L, "User tralala 5", selected = true),
            User(234L, "User tudut 6", selected = false),
            User(345L, "User toto 7", selected = true),
            User(345L, "UserWithAVeryLongNameIndeed 8", selected = true),
            User(345L, "UserWithQuiteALongName 9", selected = true),
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

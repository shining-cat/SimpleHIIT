package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun SelectUsersComponent(
    modifier: Modifier = Modifier,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {}
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.selected_users_setting_title),
            style = MaterialTheme.typography.headlineLarge
        )

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier
                .padding(top = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users.size) {
                val user = users[it]
                ToggleButton(
                    modifier = Modifier
                        .height(56.dp)
                        .defaultMinSize(minWidth = 112.dp),
                    label = user.name,
                    selected = user.selected,
                    onToggle = { toggleSelectedUser(user) }
                )
            }
        }
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SelectUsersComponentPreview(
    @PreviewParameter(SelectUsersComponentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitMobileTheme {
        Surface {
            SelectUsersComponent(
                users = users,
                modifier = Modifier.height(250.dp)
            )
        }
    }
}

internal class SelectUsersComponentPreviewParameterProvider :
    PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() = sequenceOf(
            listOf(User(123L, "User 1", selected = true)),
            listOf(
                User(123L, "User 1", selected = true),
                User(234L, "User 2", selected = false)
            ),
            listOf(
                User(123L, "User 1", selected = true),
                User(234L, "User pouet 2", selected = false),
                User(345L, "User ping 3", selected = true),
                User(345L, "User 4 hase a very long name", selected = true),
                User(123L, "User tralala 5", selected = true),
                User(234L, "User tudut 6", selected = false),
                User(345L, "User toto 7", selected = true),
                User(345L, "UserWithLongName 8", selected = true),
            )
        )
}



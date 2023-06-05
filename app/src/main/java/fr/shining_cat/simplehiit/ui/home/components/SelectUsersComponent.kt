package fr.shining_cat.simplehiit.ui.home.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.commonui.components.ToggleButton
import fr.shining_cat.simplehiit.android.mobile.commonui.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commondomain.models.User

@Composable
fun SelectUsersComponent(
    users: List<User>,
    toggleSelectedUser: (User) -> Unit
) {
    Column(
        Modifier.fillMaxWidth().padding(vertical = 16.dp)
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = R.string.selected_users_setting_title),
            style = MaterialTheme.typography.headlineLarge
        )
        LazyRow(
            modifier = Modifier
                .padding(vertical=16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users.size) {
                val user = users[it]
                ToggleButton(
                    modifier = Modifier.height(56.dp).defaultMinSize(minWidth = 112.dp),
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
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SelectUsersComponentPreview() {
    SimpleHiitTheme {
        Surface {
            SelectUsersComponent(
                users = listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                    User(345L, "User 3", selected = true)
                ),
                toggleSelectedUser = {}
            )
        }
    }
}



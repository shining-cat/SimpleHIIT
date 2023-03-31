package fr.shining_cat.simplehiit.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.ui.components.ToggleButton

@Composable
fun SelectUsersComponent(users: List<User>, toggleSelectedUser: (User) -> Unit) {
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

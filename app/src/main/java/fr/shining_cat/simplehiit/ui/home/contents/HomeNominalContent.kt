package fr.shining_cat.simplehiit.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.ui.home.components.NumberCyclesComponent
import fr.shining_cat.simplehiit.ui.home.components.SelectUsersComponent
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun HomeNominalContent(
    openInputNumberCycles: (Int) -> Unit,
    numberOfCycles: Int,
    lengthOfCycle: String,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit,
    navigateToSession: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
    ) {
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        NumberCyclesComponent(
            openInputNumberCycles = openInputNumberCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        SelectUsersComponent(
            users = users,
            toggleSelectedUser = toggleSelectedUser
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            modifier = Modifier
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = navigateToSession,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            Text(text = stringResource(id = R.string.launch_session_label))
        }
        Spacer(modifier = Modifier.height(24.dp))
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
private fun HomeNominalContentPreview() {
    SimpleHiitTheme {
        Surface{
            HomeNominalContent(
                openInputNumberCycles = {},
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                users = listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                    User(345L, "User 3", selected = true)
                ),
                toggleSelectedUser = {},
                navigateToSession = {}
            )
        }
    }
}

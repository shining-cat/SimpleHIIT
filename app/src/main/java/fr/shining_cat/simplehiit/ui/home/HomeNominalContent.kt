package fr.shining_cat.simplehiit.ui.home

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.User

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

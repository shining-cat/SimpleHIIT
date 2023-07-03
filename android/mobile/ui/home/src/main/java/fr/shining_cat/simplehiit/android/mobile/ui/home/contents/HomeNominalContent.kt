package fr.shining_cat.simplehiit.android.mobile.ui.home.contents

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
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.NumberCyclesComponent
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.SelectUsersComponent
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun HomeNominalContent(
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
    navigateToSession: () -> Unit = {}
) {
    val canLaunchSession = users.any { it.selected }
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
        SelectUsersComponent(
            users = users,
            toggleSelectedUser = toggleSelectedUser
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        NumberCyclesComponent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            enabled = canLaunchSession,
            modifier = Modifier
                .height(56.dp)
                .align(Alignment.CenterHorizontally),
            onClick = navigateToSession,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.secondary,
                contentColor = MaterialTheme.colorScheme.onSecondary
            )
        ) {
            if(canLaunchSession){
                Text(text = stringResource(id = R.string.launch_session_label))
            } else{
                Text(text = stringResource(id = R.string.cannot_launch_session_label))
            }
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
        Surface {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                    User(345L, "User 3", selected = true)
                )
            )
        }
    }
}

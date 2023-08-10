package fr.shining_cat.simplehiit.android.tv.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.home.components.NumberCyclesComponent
import fr.shining_cat.simplehiit.android.tv.ui.home.components.SelectUsersComponent
import fr.shining_cat.simplehiit.android.tv.ui.home.components.SingleUserHeaderComponent
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.User
import kotlinx.coroutines.delay

@Composable
fun HomeNominalContent(
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
    navigateToSession: () -> Unit = {},
    hiitLogger: HiitLogger? = null
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(1000L) //wait a sec to increase awareness of the user of the focusing on the main button
        focusRequester.requestFocus()
    }
    val canLaunchSession = users.any { it.selected }

    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                modifier = Modifier.weight(1f),
                user = users[0]
            )
        } else {
            SelectUsersComponent(
                modifier = Modifier.weight(1f),
                users = users,
                toggleSelectedUser = toggleSelectedUser
            )
        }
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            NumberCyclesComponent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = numberOfCycles,
                lengthOfCycle = lengthOfCycle,
                totalLengthFormatted = totalLengthFormatted,
                modifier = Modifier.weight(1f, true)
            )
            Column(
                modifier = Modifier.weight(1f, true),
                verticalArrangement = Arrangement.Center
            ) {
                ButtonFilled(
                    modifier = Modifier
                        .height(48.dp)
                        .focusRequester(focusRequester),//calling focus on the launch button on opening
                    label = if (canLaunchSession) {
                        stringResource(id = R.string.launch_session_label)
                    } else {
                        stringResource(id = R.string.cannot_launch_session_label)
                    },
                    accentColor = true,
                    enabled = canLaunchSession,
                    onClick = navigateToSession
                )
            }
        }
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun HomeNominalContentPreviewPhonePortrait(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users
            )
        }
    }
}

internal class HomeNominalContentPreviewParameterProvider : PreviewParameterProvider<List<User>> {
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
            ),
            listOf(User(123L, "User 1", selected = false))
        )
}
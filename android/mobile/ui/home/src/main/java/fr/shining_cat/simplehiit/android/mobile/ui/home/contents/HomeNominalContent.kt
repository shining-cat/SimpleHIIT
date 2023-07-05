package fr.shining_cat.simplehiit.android.mobile.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.LaunchSessionButton
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.NumberCyclesComponent
import fr.shining_cat.simplehiit.android.mobile.ui.home.components.SelectUsersComponent
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun HomeNominalContent(
    decreaseNumberOfCycles: () -> Unit = {},
    increaseNumberOfCycles: () -> Unit = {},
    numberOfCycles: Int,
    lengthOfCycle: String,
    totalLengthFormatted: String,
    uiArrangement: UiArrangement,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
    navigateToSession: () -> Unit = {}
) {
    when (uiArrangement) {
        UiArrangement.VERTICAL -> VerticalHomeNominalContent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            navigateToSession = navigateToSession
        )

        UiArrangement.HORIZONTAL -> HorizontalHomeNominalContent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            navigateToSession = navigateToSession
        )
    }
}

@Composable
private fun VerticalHomeNominalContent(
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
            .fillMaxSize(),
    ) {
        SelectUsersComponent(
            modifier = Modifier.weight(1f, true),
            users = users,
            toggleSelectedUser = toggleSelectedUser
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = Dp.Hairline
        )
        NumberCyclesComponent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            thickness = Dp.Hairline
        )
        LaunchSessionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            canLaunchSession = canLaunchSession,
            navigateToSession = navigateToSession
        )
    }
}

@Composable
private fun HorizontalHomeNominalContent(
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
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SelectUsersComponent(
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            modifier = Modifier.weight(1f)
        )
        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
        ) {
            NumberCyclesComponent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = numberOfCycles,
                lengthOfCycle = lengthOfCycle,
                totalLengthFormatted = totalLengthFormatted,
                modifier = Modifier.weight(1f, true)
            )
            Divider(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp), thickness = Dp.Hairline
            )
            LaunchSessionButton(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                canLaunchSession = canLaunchSession,
                navigateToSession = navigateToSession
            )
        }
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun HomeNominalContentPreviewPhonePortrait() {
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
                ),
                uiArrangement = UiArrangement.VERTICAL
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeNominalContentPreviewTabletLandscape() {
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
                ),
                uiArrangement = UiArrangement.HORIZONTAL
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400
)
@Composable
private fun HomeNominalContentPreviewPhoneLandscape() {
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
                ),
                uiArrangement = UiArrangement.HORIZONTAL
            )
        }
    }
}

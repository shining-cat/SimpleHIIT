package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Divider
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.utils.StickyFooterArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.LaunchSessionButton
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.NumberCyclesComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SelectUsersComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SingleUserHeaderComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User

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
    navigateToSession: () -> Unit = {},
    hiitLogger: HiitLogger? = null
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
            navigateToSession = navigateToSession,
            hiitLogger = hiitLogger
        )

        UiArrangement.HORIZONTAL -> HorizontalHomeNominalContent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            navigateToSession = navigateToSession,
            hiitLogger = hiitLogger
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
    navigateToSession: () -> Unit = {},
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null
) {
    val canLaunchSession = users.any { it.selected }
    Column(
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
                modifier = Modifier.weight(.5f, true),
                users = users,
                toggleSelectedUser = toggleSelectedUser
            )
        }
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
            totalLengthFormatted = totalLengthFormatted
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
    navigateToSession: () -> Unit = {},
    hiitLogger: HiitLogger? = null
) {
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
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight(),
            verticalArrangement = StickyFooterArrangement(
                0.dp,
                hiitLogger
            ),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                NumberCyclesComponent(
                    decreaseNumberOfCycles = decreaseNumberOfCycles,
                    increaseNumberOfCycles = increaseNumberOfCycles,
                    numberOfCycles = numberOfCycles,
                    lengthOfCycle = lengthOfCycle,
                    totalLengthFormatted = totalLengthFormatted,
                    modifier = Modifier.weight(1f, true)
                )
            }
            item {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp),
                    thickness = Dp.Hairline
                )
                LaunchSessionButton(
                    canLaunchSession = canLaunchSession,
                    navigateToSession = navigateToSession
                )
            }
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
private fun HomeNominalContentPreviewPhonePortrait(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users,
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
private fun HomeNominalContentPreviewTabletLandscape(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users,
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
private fun HomeNominalContentPreviewPhoneLandscape(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitMobileTheme {
        Surface {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users,
                uiArrangement = UiArrangement.HORIZONTAL
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
                User(345L, "UserWithLongName 8", selected = true)
            )
        )
}

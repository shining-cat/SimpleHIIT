package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.layout.Arrangement.spacedBy
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.ui.utils.StickyFooterArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.LaunchSessionButton
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.NumberCyclesComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SelectUsersComponent
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SingleUserHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R
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
    hiitLogger: HiitLogger? = null,
) {
    when (uiArrangement) {
        UiArrangement.VERTICAL ->
            VerticalHomeNominalContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = numberOfCycles,
                lengthOfCycle = lengthOfCycle,
                totalLengthFormatted = totalLengthFormatted,
                users = users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = navigateToSession,
                hiitLogger = hiitLogger,
            )

        UiArrangement.HORIZONTAL ->
            HorizontalHomeNominalContent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = numberOfCycles,
                lengthOfCycle = lengthOfCycle,
                totalLengthFormatted = totalLengthFormatted,
                users = users,
                toggleSelectedUser = toggleSelectedUser,
                navigateToSession = navigateToSession,
                hiitLogger = hiitLogger,
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
    hiitLogger: HiitLogger? = null,
) {
    val canLaunchSession = users.any { it.selected }
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxSize(),
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                modifier = Modifier.weight(1f),
                user = users[0],
            )
        } else {
            SelectUsersComponent(
                modifier = Modifier.weight(.5f, true),
                users = users,
                toggleSelectedUser = toggleSelectedUser,
            )
        }
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
        )
        NumberCyclesComponent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
        )
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
        )
        LaunchSessionButton(
            modifier = Modifier.align(Alignment.CenterHorizontally),
            canLaunchSession = canLaunchSession,
            navigateToSession = navigateToSession,
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
    hiitLogger: HiitLogger? = null,
) {
    val canLaunchSession = users.any { it.selected }
    Row(
        modifier =
            Modifier
                .padding(
                    top = dimensionResource(R.dimen.spacing_1),
                    start = dimensionResource(R.dimen.spacing_1),
                    end = dimensionResource(R.dimen.spacing_1),
                ).fillMaxSize(),
        horizontalArrangement = spacedBy(dimensionResource(R.dimen.spacing_1)),
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                modifier = Modifier.weight(1f),
                user = users[0],
            )
        } else {
            SelectUsersComponent(
                modifier = Modifier.weight(1f),
                users = users,
                toggleSelectedUser = toggleSelectedUser,
            )
        }
        LazyColumn(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight(),
            verticalArrangement =
                StickyFooterArrangement(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            item {
                NumberCyclesComponent(
                    decreaseNumberOfCycles = decreaseNumberOfCycles,
                    increaseNumberOfCycles = increaseNumberOfCycles,
                    numberOfCycles = numberOfCycles,
                    lengthOfCycle = lengthOfCycle,
                    totalLengthFormatted = totalLengthFormatted,
                    modifier = Modifier.weight(1f, true),
                )
            }
            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.spacing_1)),
                    thickness = Dp.Hairline,
                )
                LaunchSessionButton(
                    canLaunchSession = canLaunchSession,
                    navigateToSession = navigateToSession,
                )
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeNominalContentPreviewPhoneLandscape(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users,
                uiArrangement = previewUiArrangement,
            )
        }
    }
}

internal class HomeNominalContentPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                listOf(User(123L, "User 1", selected = true)),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User 2", selected = false),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
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
            )
}

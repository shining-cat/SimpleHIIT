package fr.shiningcat.simplehiit.android.mobile.ui.home.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.VerticalDivider
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
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
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SelectUsersComponentHorizontal
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SelectUsersComponentVertical
import fr.shiningcat.simplehiit.android.mobile.ui.home.components.SingleUserHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.User

/**
 * Home nominal content with font scale accessibility handling.
 *
 * At high font scales (>1.5f) with horizontal UI arrangement, the 2-column layout can cause
 * the NumberCyclesComponent to overflow on small landscape screens. This composable explicitly
 * chooses the vertical layout when both conditions are met (horizontal arrangement AND high font scale),
 * ensuring all content remains scrollable and accessible while keeping the side navigation bar visible.
 */
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
    warning: LaunchSessionWarning? = null,
    hiitLogger: HiitLogger? = null,
) {
    val fontScale = LocalDensity.current.fontScale

    // Explicitly choose layout based on both uiArrangement and fontScale
    // Use vertical layout if:
    // 1. uiArrangement is VERTICAL, OR
    // 2. uiArrangement is HORIZONTAL but fontScale exceeds threshold (accessibility override)
    val useSingleColumnLayout =
        uiArrangement == UiArrangement.VERTICAL ||
            (uiArrangement == UiArrangement.HORIZONTAL && fontScale > 1.5f)

    if (useSingleColumnLayout) {
        VerticalHomeNominalContent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            navigateToSession = navigateToSession,
            warning = warning,
            hiitLogger = hiitLogger,
        )
    } else {
        HorizontalHomeNominalContent(
            decreaseNumberOfCycles = decreaseNumberOfCycles,
            increaseNumberOfCycles = increaseNumberOfCycles,
            numberOfCycles = numberOfCycles,
            lengthOfCycle = lengthOfCycle,
            totalLengthFormatted = totalLengthFormatted,
            users = users,
            toggleSelectedUser = toggleSelectedUser,
            navigateToSession = navigateToSession,
            warning = warning,
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
    warning: LaunchSessionWarning? = null,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val canLaunchSession = users.any { it.selected }
    Column(
        modifier =
            Modifier
                .padding(vertical = dimensionResource(R.dimen.spacing_1))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                user = users[0],
            )
        } else {
            SelectUsersComponentVertical(
                users = users,
                toggleSelectedUser = toggleSelectedUser,
            )
        }
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(bottom = dimensionResource(R.dimen.spacing_1)),
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
            launchSessionWarning = warning,
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
    warning: LaunchSessionWarning? = null,
    hiitLogger: HiitLogger? = null,
) {
    val canLaunchSession = users.any { it.selected }
    Row(
        modifier =
            Modifier
                .padding(start = dimensionResource(R.dimen.spacing_1))
                .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                modifier = Modifier.weight(1f),
                user = users.first(),
            )
        } else {
            SelectUsersComponentHorizontal(
                modifier = Modifier.weight(1f),
                users = users,
                toggleSelectedUser = toggleSelectedUser,
            )
        }
        VerticalDivider(
            modifier =
                Modifier
                    .fillMaxHeight()
                    .padding(horizontal = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
        )
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
            verticalArrangement =
                StickyFooterArrangement(0.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            NumberCyclesComponent(
                decreaseNumberOfCycles = decreaseNumberOfCycles,
                increaseNumberOfCycles = increaseNumberOfCycles,
                numberOfCycles = numberOfCycles,
                lengthOfCycle = lengthOfCycle,
                totalLengthFormatted = totalLengthFormatted,
                modifier = Modifier.weight(1f, true),
            )

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
                launchSessionWarning = warning,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun HomeNominalContentPreviewPhone(
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
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                ),
                listOf(
                    User(123L, "User 1", selected = true),
                    User(234L, "User pouet 2", selected = false),
                    User(345L, "User ping 3", selected = true),
                    User(345L, "User 4 a long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
                ),
            )
}

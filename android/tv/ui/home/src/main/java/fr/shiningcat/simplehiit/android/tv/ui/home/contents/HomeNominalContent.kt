package fr.shiningcat.simplehiit.android.tv.ui.home.contents

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreens
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.home.components.NumberCyclesComponent
import fr.shiningcat.simplehiit.android.tv.ui.home.components.SelectUsersComponent
import fr.shiningcat.simplehiit.android.tv.ui.home.components.SingleUserHeaderComponent
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.delay
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

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
    warning: LaunchSessionWarning? = null,
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger? = null,
) {
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        delay(1000L) // wait 1s to increase awareness of the user of the focusing on the main button
        focusRequester.requestFocus()
    }
    val canLaunchSession = users.any { it.selected }

    Row(
        modifier =
            Modifier
                .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                .fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        if (users.size == 1) {
            SingleUserHeaderComponent(
                modifier = Modifier.weight(1f),
                user = users.first(),
            )
        } else {
            SelectUsersComponent(
                modifier = Modifier.weight(1f),
                users = users,
                toggleSelectedUser = toggleSelectedUser,
            )
        }
        Column(
            modifier =
                Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .verticalScroll(rememberScrollState()),
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
            Column(
                modifier = Modifier.weight(1f, true),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                ButtonFilled(
                    modifier =
                        Modifier
                            .height(adaptDpToFontScale(dimensionResource(CommonResourcesR.dimen.button_height)))
                            .focusRequester(focusRequester),
                    fillHeight = true,
                    // calling focus on the launch button on opening
                    label =
                        if (canLaunchSession) {
                            stringResource(id = CommonResourcesR.string.launch_session_label)
                        } else {
                            stringResource(id = CommonResourcesR.string.cannot_launch_session_label)
                        },
                    accentColor = true,
                    enabled = canLaunchSession,
                    onClick = navigateToSession,
                )

                warning?.let {
                    val warningText =
                        when (it) {
                            LaunchSessionWarning.DUPLICATED_EXERCISES ->
                                stringResource(
                                    id = CommonResourcesR.string.launch_session_warning_duplicated_exercises,
                                )

                            LaunchSessionWarning.SKIPPED_EXERCISE_TYPES ->
                                stringResource(
                                    id = CommonResourcesR.string.launch_session_warning_skipped_exercise_types,
                                )

                            LaunchSessionWarning.NO_USER_SELECTED ->
                                stringResource(
                                    id = CommonResourcesR.string.launch_session_warning_no_user_selected,
                                )
                        }
                    Text(
                        text = warningText,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier =
                            Modifier
                                .padding(
                                    vertical = dimensionResource(CommonResourcesR.dimen.spacing_1),
                                    horizontal = dimensionResource(CommonResourcesR.dimen.spacing_3),
                                ).fillMaxWidth(.8f),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

// Previews
@ExperimentalTvMaterial3Api
@PreviewTvScreens
@Composable
private fun HomeNominalContentPreviewPhonePortrait(
    @PreviewParameter(HomeNominalContentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            HomeNominalContent(
                numberOfCycles = 5,
                lengthOfCycle = "4mn",
                totalLengthFormatted = "20mn",
                users = users,
                warning = LaunchSessionWarning.DUPLICATED_EXERCISES,
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
                    User(345L, "User 4 hase a very long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
                ),
                listOf(User(123L, "User 1", selected = false)),
            )
}

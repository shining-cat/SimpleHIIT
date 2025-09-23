package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.getToggleButtonLostWidthDp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlin.math.roundToInt
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SelectUsersComponent(
    modifier: Modifier = Modifier,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
) {
    Column(
        modifier = modifier.fillMaxWidth(),
    ) {
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = stringResource(id = CommonResourcesR.string.selected_users_setting_title),
            style = MaterialTheme.typography.headlineLarge,
        )
        val availableWidthPix = LocalWindowInfo.current.containerSize.width
        val spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_1)
        val density = LocalDensity.current
        val spacingPix = with(density) { spacingDp.toPx() }
        val toggleButtonLostWidthPix = getToggleButtonLostWidthDp()
        val oneHalfColumnAvailableWidth =
            (availableWidthPix - 2 * spacingPix) / 2f - toggleButtonLostWidthPix
        val useTwoColumn =
            users.all {
                fitsOnXLines(
                    textLayoutInfo =
                        TextLayoutInfo(
                            text = it.name,
                            style = MaterialTheme.typography.labelMedium,
                        ),
                    numberOfLines = 1,
                    availableWidthPx = oneHalfColumnAvailableWidth.roundToInt(),
                )
            }
        val numberOfColumns = if (useTwoColumn) 2 else 1
        LazyVerticalGrid(
            columns = GridCells.Fixed(numberOfColumns),
            modifier =
                Modifier
                    .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_2))
                    .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(spacingDp),
            verticalArrangement = Arrangement.spacedBy(spacingDp),
        ) {
            items(users.size) {
                val user = users[it]
                ToggleButton(
                    modifier =
                        Modifier
                            .height(dimensionResource(R.dimen.user_select_toggle_button_height))
                            .defaultMinSize(minWidth = dimensionResource(R.dimen.user_select_toggle_button_min_width)),
                    label = user.name,
                    labelStyle = MaterialTheme.typography.labelMedium,
                    selected = user.selected,
                    onToggle = { toggleSelectedUser(user) },
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
private fun SelectUsersComponentPreview(
    @PreviewParameter(SelectUsersComponentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
            SelectUsersComponent(
                users = users,
                modifier = Modifier.height(250.dp),
            )
        }
    }
}

internal class SelectUsersComponentPreviewParameterProvider : PreviewParameterProvider<List<User>> {
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
                    User(345L, "User 4 a long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
                ),
            )
}

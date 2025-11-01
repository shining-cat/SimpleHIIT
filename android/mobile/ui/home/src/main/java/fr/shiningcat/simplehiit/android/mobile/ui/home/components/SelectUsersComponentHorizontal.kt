package fr.shiningcat.simplehiit.android.mobile.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.common.ui.utils.getTextHeightPix
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ToggleButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.getToggleButtonLostWidthPix
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.home.R
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlin.math.floor
import kotlin.math.roundToInt
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SelectUsersComponentHorizontal(
    modifier: Modifier = Modifier,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
) {
    BoxWithConstraints(
        modifier = modifier.fillMaxWidth(),
    ) {
        val density = LocalDensity.current
        val availableHeightPix = with(density) { maxHeight.toPx() }
        val availableWidthPix = with(density) { maxWidth.toPx() }

        Column(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val title = stringResource(id = CommonResourcesR.string.selected_users_setting_title)
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title,
                style = MaterialTheme.typography.headlineLarge,
            )
            // if we can't fit 2 columns without truncating titles, always use single column:
            val spacingDp = dimensionResource(CommonResourcesR.dimen.spacing_1)
            val spacingPix = with(density) { spacingDp.toPx() }
            val toggleButtonLostWidthPix = getToggleButtonLostWidthPix()
            val oneHalfColumnAvailableWidth =
                (availableWidthPix - 3 * spacingPix) / 2f - toggleButtonLostWidthPix
            val useTwoColumnAllowed =
                users.all {
                    fitsOnXLines(
                        textLayoutInfo =
                            TextLayoutInfo(
                                text = it.name,
                                style = MaterialTheme.typography.labelMedium,
                            ),
                        numberOfLines = 1,
                        availableWidthPix = oneHalfColumnAvailableWidth.roundToInt(),
                    )
                }
            // we don't adapt the button's height to fontscale as even 200% fits fine in the height used
            val baseButtonHeightDp = dimensionResource(R.dimen.user_select_toggle_button_height)
            val numberOfColumns =
                if (useTwoColumnAllowed) {
                    // we need to calculate the available height to determine if all users can fit one per line,
                    // otherwise switch to 2 columns, and make scrollable
                    val buttonHeightPix = with(density) { baseButtonHeightDp.toPx() }
                    val titleHeightPix =
                        getTextHeightPix(
                            textLayoutInfo =
                                TextLayoutInfo(
                                    text = title,
                                    style = MaterialTheme.typography.headlineLarge,
                                ),
                            maxLines = 1,
                            availableWidthPix = floor(availableWidthPix).toInt(),
                        ) ?: 0
                    // we'll add an additional spacing at the bottom in the calculation for comfort
                    val totalUsedHeightPix = titleHeightPix + spacingPix + users.size * (buttonHeightPix + spacingPix)
                    if (availableHeightPix > totalUsedHeightPix) 1 else 2
                } else {
                    1
                }

            LazyVerticalGrid(
                modifier =
                    Modifier
                        .padding(top = spacingDp)
                        .fillMaxWidth(),
                columns = GridCells.Fixed(numberOfColumns),
                verticalArrangement = Arrangement.spacedBy(spacingDp),
                horizontalArrangement = Arrangement.spacedBy(spacingDp),
            ) {
                items(users.size) {
                    val user = users[it]
                    ToggleButton(
                        modifier =
                            Modifier
                                .height(baseButtonHeightDp)
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
}

// Previews
@PreviewLightDark
@PreviewFontScale
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420",
    showSystemUi = true,
)
@Preview(name = "Tablet - Landscape", device = TABLET, showSystemUi = true)
@Preview(name = "Desktop", device = DESKTOP, showSystemUi = true)
@Composable
private fun SelectUsersComponentPreview(
    @PreviewParameter(SelectUsersComponentHorizontalPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
            SelectUsersComponentHorizontal(
                users = users,
                modifier = Modifier.height(250.dp),
            )
        }
    }
}

internal class SelectUsersComponentHorizontalPreviewParameterProvider : PreviewParameterProvider<List<User>> {
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
                    User(345L, "User 4 pouet", selected = true),
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

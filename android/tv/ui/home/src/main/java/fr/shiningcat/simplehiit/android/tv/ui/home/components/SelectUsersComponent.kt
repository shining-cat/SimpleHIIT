package fr.shiningcat.simplehiit.android.tv.ui.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.common.ui.utils.getTextHeightPix
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonToggle
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.home.R
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun SelectUsersComponent(
    modifier: Modifier = Modifier,
    users: List<User>,
    toggleSelectedUser: (User) -> Unit = {},
    userButtonsFocusRequesters: Map<String, FocusRequester> = emptyMap(),
) {
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val density = LocalDensity.current
        val availableHeightPix = with(density) { maxHeight.toPx() }
        val availableWidthPix = with(density) { maxWidth.toPx() }.toInt()

        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            val title = stringResource(id = CommonResourcesR.string.selected_users_setting_title)
            val spacing = dimensionResource(CommonResourcesR.dimen.spacing_3)
            val spacingPix = with(density) { spacing.toPx() }

            Text(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(bottom = spacing),
                textAlign = TextAlign.Center,
                text = title,
                style = MaterialTheme.typography.headlineLarge,
            )

            val baseButtonHeightDp = adaptDpToFontScale(dimensionResource(R.dimen.user_select_button_height))

            // Check if all users fit in available height when laid out in a single column
            val buttonHeightPix = with(density) { baseButtonHeightDp.toPx() }
            val titleHeightPix =
                getTextHeightPix(
                    textLayoutInfo =
                        TextLayoutInfo(
                            text = title,
                            style = MaterialTheme.typography.headlineLarge,
                        ),
                    maxLines = 1,
                    availableWidthPix = availableWidthPix,
                ) ?: 0
            // Add an additional spacing at the bottom for comfort
            val totalUsedHeightPix = titleHeightPix + spacingPix + users.size * (buttonHeightPix + spacingPix)
            val numberOfColumns = if (availableHeightPix > totalUsedHeightPix) 1 else 2

            LazyVerticalGrid(
                modifier =
                    Modifier
                        .padding(top = spacing)
                        .fillMaxWidth(),
                columns = GridCells.Fixed(numberOfColumns),
                contentPadding = PaddingValues(all = spacing),
                verticalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterVertically),
                horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
            ) {
                items(
                    count = users.size,
                    // a stable identifier on each item helps avoiding a complete recomposition of
                    // the component when only a selected attribute is toggled
                    key = { index -> users[index].id },
                ) { index ->
                    val user = users[index]
                    val userFocusRequester = userButtonsFocusRequesters[user.id.toString()]
                    ButtonToggle(
                        modifier =
                            Modifier
                                .run {
                                    if (userFocusRequester != null) {
                                        this.focusRequester(userFocusRequester)
                                    } else {
                                        this
                                    }
                                }.height(baseButtonHeightDp)
                                .defaultMinSize(minWidth = dimensionResource(R.dimen.user_select_button_min_width)),
                        fillWidth = false,
                        fillHeight = true,
                        label = user.name,
                        selected = user.selected,
                        onToggle = { toggleSelectedUser(user) },
                    )
                }
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun SelectUsersComponentPreview(
    @PreviewParameter(SelectUsersComponentPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SelectUsersComponent(
                users = users,
                modifier = Modifier.height(700.dp),
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
                    User(345L, "User ping 3", selected = false),
                    User(345L, "User 4 hase a very long name", selected = true),
                    User(123L, "User tralala 5", selected = true),
                    User(234L, "User tudut 6", selected = false),
                    User(345L, "User toto 7", selected = true),
                    User(345L, "UserWithLongName 8", selected = true),
                ),
            )
}

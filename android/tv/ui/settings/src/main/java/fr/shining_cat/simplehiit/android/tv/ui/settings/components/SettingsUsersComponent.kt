package fr.shining_cat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.User
import kotlin.math.ceil

@OptIn(ExperimentalFoundationApi::class, ExperimentalTvMaterial3Api::class)
@Composable
fun SettingsUsersComponent(
    users: List<User>,
    onClickUser: (User) -> Unit = {},
    onAddUser: () -> Unit = {}
) {
    Column(
        modifier = Modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.users_list_setting_label)
        )
        Spacer(modifier = Modifier.height(24.dp))
        val itemHeight = 48.dp
        val numberOfColumns = 3
        val spacing = 24.dp
        val totalNumberOfItems = users.size
        val rowsCount = ceil(totalNumberOfItems.toFloat() / numberOfColumns.toFloat()).toInt()
        val gridHeight = (itemHeight + spacing) * rowsCount
        LazyVerticalStaggeredGrid(
            modifier = Modifier.height(gridHeight),
            columns = StaggeredGridCells.Fixed(numberOfColumns),
            verticalItemSpacing = spacing,
            horizontalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false
        ) {
            items(users.size) {
                val user = users[it]
                ButtonBordered(
                    modifier = Modifier
                        .height(itemHeight)
                        .defaultMinSize(minWidth = 112.dp),
                    onClick = { onClickUser(user) },
                    label = user.name
                )
            }
        }
        ButtonFilled(
            modifier = Modifier
                .height(itemHeight)
                .padding(horizontal = 32.dp),
            onClick = onAddUser,
            accentColor = true,
            icon = Icons.Filled.Add,
            iconContentDescription = R.string.add_user_button_label
        )
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun SettingsUsersComponentPreviewPhonePortrait(
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsUsersComponent(users = users)
        }
    }
}

internal class SettingsUsersComponentPreviewParameterProvider :
    PreviewParameterProvider<List<User>> {

    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser = listOf(
        User(123L, "User 1", selected = true),
        User(234L, "User pouet 2", selected = false),
        User(345L, "User ping 3", selected = true),
        User(345L, "User 4 hase a very long name", selected = true),
        User(123L, "User tralala 5", selected = true),
        User(234L, "User tudut 6", selected = false),
        User(345L, "User toto 7", selected = true),
        User(345L, "UserWithLongName 8", selected = true),
        User(345L, "UserWithLongName 9", selected = true)
    )

    override val values: Sequence<List<User>>
        get() = sequenceOf(
            emptyList(),
            listOfOneUser,
            listOfTwoUser,
            listOfMoreUser
        )
}

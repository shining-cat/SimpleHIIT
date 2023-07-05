package fr.shining_cat.simplehiit.android.mobile.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun SettingsUsersComponent(
    users: List<User>,
    onClickUser: (User) -> Unit = {},
    onAddUser: () -> Unit = {}
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.users_list_setting_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users.size) {
                val user = users[it]
                OutlinedButton(
                    modifier = Modifier
                        .height(56.dp)
                        .defaultMinSize(minWidth = 112.dp),
                    onClick = { onClickUser(user) }
                ) {
                    Text(text = user.name)
                }
            }
            item {
                Button(
                    modifier = Modifier.height(56.dp),
                    onClick = onAddUser,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_user_button_label),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
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
private fun SettingsUsersComponentPreviewPhonePortrait(
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTheme {
        Surface {
            SettingsUsersComponent(users = users)
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
private fun SettingsUsersComponentPreviewTabletLandscape(
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTheme {
        Surface {
            SettingsUsersComponent(users = users)
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
private fun SettingsUsersComponentPreviewPhoneLandscape(
    @PreviewParameter(SettingsUsersComponentPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTheme {
        Surface {
            SettingsUsersComponent(users = users)
        }
    }
}

internal class SettingsUsersComponentPreviewParameterProvider :
    PreviewParameterProvider<List<User>> {

    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser = listOf(
        User(name = "user 1"),
        User(name = "user 2"),
        User(name = "user 3"),
        User(name = "user 4"),
        User(name = "user 5")
    )

    override val values: Sequence<List<User>>
        get() = sequenceOf(
            emptyList(),
            listOfOneUser,
            listOfTwoUser,
            listOfMoreUser
        )
}

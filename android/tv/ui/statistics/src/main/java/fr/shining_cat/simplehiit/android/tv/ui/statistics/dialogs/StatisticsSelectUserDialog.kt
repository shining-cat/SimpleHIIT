package fr.shining_cat.simplehiit.android.tv.ui.statistics.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.NonInteractiveSurfaceDefaults
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.User

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun StatisticsSelectUserDialog(
    users: List<User>,
    selectUser: (User) -> Unit,
    dismissAction: () -> Unit
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            colors = NonInteractiveSurfaceDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                Text(
                    textAlign = TextAlign.Left,
                    text = stringResource(id = R.string.user_pick_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                LazyColumn(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .fillMaxWidth()
                        .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(users.size) {
                        ButtonText(
                            onClick = { selectUser(users[it]) },
                            label = users[it].name
                        )
                    }
                }
                ButtonText(
                    onClick = dismissAction,
                    label = stringResource(id = R.string.cancel_button_label)
                )
            }
        }
    }
}

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsPickUserDialogPreview(
    @PreviewParameter(StatisticsPickUserDialogPreviewParameterProvider::class) users: List<User>
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsSelectUserDialog(
                users = users,
                selectUser = {},
                dismissAction = {}
            )
        }
    }
}

internal class StatisticsPickUserDialogPreviewParameterProvider :
    PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() = sequenceOf(
            listOf(),
            listOf(User(name = "user 1")),
            listOf(
                User(name = "user 1"),
                User(name = "user 2"),
                User(name = "user 3"),
                User(name = "user 4"),
                User(name = "user 5")
            ),
            listOf(
                User(name = "user 1"),
                User(name = "user 2"),
                User(name = "user 3"),
                User(name = "user 4"),
                User(name = "user 5"),
                User(name = "user 1"),
                User(name = "user 2"),
                User(name = "user 3"),
                User(name = "user 4"),
                User(name = "user 5"),
                User(name = "user 1"),
                User(name = "user 2"),
                User(name = "user 3"),
                User(name = "user 4"),
                User(name = "user 5")
            )
        )
}
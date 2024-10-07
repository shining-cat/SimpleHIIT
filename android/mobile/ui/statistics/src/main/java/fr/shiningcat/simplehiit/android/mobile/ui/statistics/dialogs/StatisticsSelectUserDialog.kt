package fr.shiningcat.simplehiit.android.mobile.ui.statistics.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun StatisticsSelectUserDialog(
    users: List<User>,
    selectUser: (User) -> Unit,
    dismissAction: () -> Unit,
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            // TODO: we want the dialog height to be between two fractions of the screen's height. This is forcing it to be 80%, even if it is nearly empty
            modifier = Modifier.fillMaxHeight(.8f),
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(8.dp)
                        .fillMaxWidth(),
            ) {
                Text(
                    textAlign = TextAlign.Left,
                    text = stringResource(id = R.string.user_pick_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                LazyColumn(
                    modifier =
                        Modifier
                            .padding(vertical = 4.dp)
                            .fillMaxWidth()
                            .weight(1f),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(0.dp),
                ) {
                    items(users.size) {
                        TextButton(
                            onClick = { selectUser(users[it]) },
                        ) {
                            Text(text = users[it].name)
                        }
                    }
                }
                TextButton(onClick = dismissAction) {
                    Text(text = stringResource(id = R.string.cancel_button_label))
                }
            }
        }
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun StatisticsPickUserDialogPreview(
    @PreviewParameter(StatisticsPickUserDialogPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        StatisticsSelectUserDialog(
            users = users,
            selectUser = {},
            dismissAction = {},
        )
    }
}

internal class StatisticsPickUserDialogPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                listOf(),
                listOf(User(name = "user 1")),
                listOf(
                    User(name = "user 1"),
                    User(name = "user 2"),
                    User(name = "user 3"),
                    User(name = "user 4"),
                    User(name = "user 5"),
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
                    User(name = "user 5"),
                ),
            )
}

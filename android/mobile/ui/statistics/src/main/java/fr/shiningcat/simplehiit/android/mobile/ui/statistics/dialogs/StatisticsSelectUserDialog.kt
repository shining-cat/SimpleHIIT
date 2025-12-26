package fr.shiningcat.simplehiit.android.mobile.ui.statistics.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogProperties
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptiveDialogWidth
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.OnSurfaceTextButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun StatisticsSelectUserDialog(
    users: List<User>,
    selectUser: (User) -> Unit,
    dismissAction: () -> Unit,
) {
    Dialog(
        onDismissRequest = dismissAction,
        properties = adaptiveDialogProperties(),
    ) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
            modifier = Modifier.adaptiveDialogWidth(),
        ) {
            BoxWithConstraints {
                val maxScrollableHeight = maxHeight * 0.7f

                Column(
                    modifier =
                        Modifier
                            .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                            .fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val spacing = dimensionResource(CommonResourcesR.dimen.spacing_1)
                    Text(
                        textAlign = TextAlign.Center,
                        text = stringResource(id = CommonResourcesR.string.user_pick_dialog_title),
                        style = MaterialTheme.typography.headlineSmall,
                    )
                    Spacer(modifier = Modifier.height(spacing))

                    val itemHeight =
                        adaptDpToFontScale(dimensionResource(CommonResourcesR.dimen.minimum_touch_size))

                    Column(
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .heightIn(max = maxScrollableHeight)
                                .verticalScroll(rememberScrollState())
                                .padding(bottom = spacing),
                    ) {
                        FlowRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(spacing, Alignment.CenterHorizontally),
                            verticalArrangement = Arrangement.spacedBy(spacing),
                            maxItemsInEachRow = Int.MAX_VALUE,
                        ) {
                            users.forEach { user ->
                                OutlinedButton(
                                    modifier =
                                        Modifier
                                            .height(itemHeight)
                                            .defaultMinSize(minWidth = dimensionResource(CommonResourcesR.dimen.minimum_touch_size)),
                                    onClick = { selectUser(user) },
                                ) {
                                    Text(text = user.name)
                                }
                            }
                        }
                    }

                    OnSurfaceTextButton(
                        onClick = dismissAction,
                        modifier = Modifier.padding(top = spacing),
                    ) {
                        Text(
                            text = stringResource(id = CommonResourcesR.string.cancel_button_label),
                            style = MaterialTheme.typography.labelMedium,
                        )
                    }
                }
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsSelectUserDialogPreview(
    @PreviewParameter(StatisticsSelectUserDialogPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitMobileTheme {
        Surface {
            StatisticsSelectUserDialog(
                users = users,
                selectUser = {},
                dismissAction = {},
            )
        }
    }
}

internal class StatisticsSelectUserDialogPreviewParameterProvider : PreviewParameterProvider<List<User>> {
    override val values: Sequence<List<User>>
        get() =
            sequenceOf(
                listOf(
                    User(name = "Alice"),
                    User(name = "Bob"),
                ),
                listOf(
                    User(name = "Alice"),
                    User(name = "Bob"),
                    User(name = "Charlie"),
                    User(name = "David"),
                    User(name = "Emma"),
                ),
                listOf(
                    User(name = "User 1"),
                    User(name = "User with long name 2"),
                    User(name = "User 3"),
                    User(name = "User 4 has a very long name"),
                    User(name = "User 5"),
                    User(name = "User 6"),
                    User(name = "User 7"),
                    User(name = "UserWithAVeryLongNameIndeed 8"),
                    User(name = "UserWithQuiteALongName 9"),
                ),
                // Many users to test scrolling
                List(20) { index ->
                    User(name = "User ${index + 1}")
                },
            )
}

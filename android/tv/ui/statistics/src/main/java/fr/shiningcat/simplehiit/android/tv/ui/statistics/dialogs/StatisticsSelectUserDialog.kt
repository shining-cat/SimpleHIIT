package fr.shiningcat.simplehiit.android.tv.ui.statistics.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.statistics.R
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlin.math.ceil
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

@Composable
fun StatisticsSelectUserDialog(
    users: List<User>,
    selectUser: (User) -> Unit,
    dismissAction: () -> Unit,
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            colors =
                SurfaceDefaults.colors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                ),
            shape = MaterialTheme.shapes.medium,
        ) {
            Column(
                modifier =
                    Modifier
                        .padding(dimensionResource(CommonResourcesR.dimen.spacing_1))
                        .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                val spacing = dimensionResource(CommonResourcesR.dimen.spacing_3)
                Text(
                    textAlign = TextAlign.Left,
                    text = stringResource(id = CommonResourcesR.string.user_pick_dialog_title),
                    style = MaterialTheme.typography.headlineSmall,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Spacer(modifier = Modifier.height(spacing))

                val itemHeight = dimensionResource(R.dimen.button_height)
                val numberOfColumns = 3
                // this is to avoid the zoomed-in focused buttons of the first row to be clipped
                val forcedTopMargin = dimensionResource(CommonResourcesR.dimen.spacing_1)
                val rowsCount = ceil(users.size.toFloat() / numberOfColumns.toFloat()).toInt()
                // adding forcedMargin on top and bottom for symmetry, rather than a last spacing
                val gridHeight =
                    2 * forcedTopMargin + (itemHeight) * rowsCount + spacing * (rowsCount - 1)

                LazyVerticalGrid(
                    columns = GridCells.Fixed(numberOfColumns),
                    modifier = Modifier.height(gridHeight),
                    verticalArrangement = Arrangement.spacedBy(spacing),
                    horizontalArrangement = Arrangement.spacedBy(spacing),
                    userScrollEnabled = false,
                ) {
                    items(users.size) {
                        val user = users[it]
                        ButtonBordered(
                            // offset has to be applied to all items to avoid irregular spacing. It does not override the spacedBy of the LazyGrid
                            modifier =
                                Modifier
                                    .height(itemHeight)
                                    .offset(y = forcedTopMargin)
                                    .defaultMinSize(minWidth = dimensionResource(R.dimen.stats_select_user_button_min_width)),
                            onClick = { selectUser(user) },
                            label = user.name,
                        )
                    }
                }

                Spacer(modifier = Modifier.height(spacing))

                ButtonText(
                    modifier =
                        Modifier
                            .height(dimensionResource(R.dimen.button_height))
                            .width(dimensionResource(R.dimen.stats_select_user_cancel_button_width)),
                    onClick = dismissAction,
                    label = stringResource(id = CommonResourcesR.string.cancel_button_label),
                )
            }
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@PreviewTvScreensNoUi
@Composable
private fun StatisticsPickUserDialogPreview(
    @PreviewParameter(StatisticsPickUserDialogPreviewParameterProvider::class) users: List<User>,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsSelectUserDialog(
                users = users,
                selectUser = {},
                dismissAction = {},
            )
        }
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

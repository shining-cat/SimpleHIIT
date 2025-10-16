package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun StatisticsNoSessionsContent(
    modifier: Modifier = Modifier,
    noSessionsViewState: StatisticsViewState.NoSessions,
    uiArrangement: UiArrangement,
    onUserSelected: (User) -> Unit = {},
) {
    Column(
        modifier =
            modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        StatisticsHeaderComponent(
            currentUserName = noSessionsViewState.selectedUser.name,
            allUsers = noSessionsViewState.allUsers,
            uiArrangement = uiArrangement,
            onUserSelected = onUserSelected,
        )

        Icon(
            imageVector = ImageVector.vectorResource(R.drawable.doge),
            contentDescription = stringResource(id = R.string.doge_icon_content_description),
            tint = MaterialTheme.colorScheme.onPrimary,
        )
        Text(
            textAlign = TextAlign.Center,
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(R.dimen.spacing_2),
                    ).fillMaxWidth(),
            text = stringResource(id = R.string.no_users_found_error_message),
            style = MaterialTheme.typography.headlineMedium,
        )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun StatisticsNoSessionsContentPreview(
    @PreviewParameter(StatisticsNoSessionsContentPreviewParameterProvider::class) viewState: StatisticsViewState.NoSessions,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
    SimpleHiitMobileTheme {
        Surface {
            StatisticsNoSessionsContent(
                uiArrangement = previewUiArrangement,
                noSessionsViewState = viewState,
            )
        }
    }
}

internal class StatisticsNoSessionsContentPreviewParameterProvider : PreviewParameterProvider<StatisticsViewState.NoSessions> {
    override val values: Sequence<StatisticsViewState.NoSessions>
        get() =
            sequenceOf(
                StatisticsViewState.NoSessions(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                ),
                StatisticsViewState.NoSessions(
                    allUsers =
                        listOf(
                            User(name = "Sven"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                ),
            )
}

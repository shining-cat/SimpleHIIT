package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.common.ui.utils.StickyFooterArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.StatisticsViewState
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticCardComponent
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun StatisticsNominalContent(
    modifier: Modifier = Modifier,
    deleteAllSessionsForUser: (User) -> Unit = {},
    nominalViewState: StatisticsViewState.Nominal,
    uiArrangement: UiArrangement,
    onUserSelected: (User) -> Unit = {},
    hiitLogger: HiitLogger? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    vertical = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(R.dimen.spacing_1) else 0.dp,
                    horizontal = dimensionResource(R.dimen.spacing_1),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiArrangement == UiArrangement.VERTICAL) {
            // we don't want the name to scroll along with the grid (sticky header)
            StatisticsHeaderComponent(
                currentUserName = nominalViewState.selectedUser.name,
                allUsers = nominalViewState.allUsers,
                uiArrangement = uiArrangement,
                onUserSelected = onUserSelected,
            )
        }
        //
        val fontscale = LocalDensity.current.fontScale
        val columnsCount = if (fontscale > 1.3f) 1 else 2
        val spacingDp = dimensionResource(R.dimen.spacing_2)
        val wholeWidth: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(columnsCount) }
        LazyVerticalGrid(
            modifier =
                Modifier
                    .padding(top = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(R.dimen.spacing_1) else 0.dp)
                    .fillMaxSize(),
            columns = GridCells.Fixed(columnsCount),
            verticalArrangement =
                StickyFooterArrangement(spacingDp),
            horizontalArrangement = Arrangement.spacedBy(spacingDp),
        ) {
            if (uiArrangement == UiArrangement.HORIZONTAL) {
                // we want the header to scroll up with the page to optimize available screen space
                item(span = wholeWidth) {
                    StatisticsHeaderComponent(
                        currentUserName = nominalViewState.selectedUser.name,
                        allUsers = nominalViewState.allUsers,
                        uiArrangement = uiArrangement,
                        onUserSelected = onUserSelected,
                    )
                }
            }
            items(nominalViewState.selectedUserStatistics.size) {
                val displayStatistic = nominalViewState.selectedUserStatistics[it]
                StatisticCardComponent(displayStatistic)
            }
            item(span = wholeWidth) {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.spacing_2)),
                    thickness = Dp.Hairline,
                )
                TextButton(
                    modifier =
                        Modifier
                            .padding(
                                bottom = dimensionResource(R.dimen.spacing_3),
                            ),
                    onClick = { deleteAllSessionsForUser(nominalViewState.selectedUser) },
                ) {
                    Text(
                        text =
                            stringResource(
                                id = R.string.reset_statistics_button_label,
                                nominalViewState.selectedUser.name,
                            ),
                        textAlign = TextAlign.Center,
                    )
                }
            }
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticsNominalContentPreview(
    @PreviewParameter(StatisticsNominalContentPreviewParameterProvider::class) viewState: StatisticsViewState.Nominal,
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
            StatisticsNominalContent(
                uiArrangement = previewUiArrangement,
                nominalViewState = viewState,
                modifier = Modifier.fillMaxSize(),
            )
        }
    }
}

internal class StatisticsNominalContentPreviewParameterProvider : PreviewParameterProvider<StatisticsViewState.Nominal> {
    override val values: Sequence<StatisticsViewState.Nominal>
        get() =
            sequenceOf(
                StatisticsViewState.Nominal(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    selectedUserStatistics =
                        listOf(
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                        ),
                ),
                StatisticsViewState.Nominal(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    selectedUserStatistics =
                        listOf(
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                        ),
                ),
                StatisticsViewState.Nominal(
                    allUsers =
                        listOf(
                            User(name = "Alice"),
                            User(name = "Bob"),
                            User(name = "Charlie"),
                        ),
                    selectedUser = User(name = "Sven Svensson"),
                    selectedUserStatistics =
                        listOf(
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                            DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                "5h 23mn 64s",
                                DisplayStatisticType.TOTAL_EXERCISE_TIME,
                            ),
                            DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                "15mn 13s",
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                            ),
                            DisplayedStatistic(
                                "3,5",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                            ),
                        ),
                ),
            )
}

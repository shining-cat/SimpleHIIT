package fr.shining_cat.simplehiit.android.tv.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.grid.TvGridCells
import androidx.tv.foundation.lazy.grid.TvGridItemSpan
import androidx.tv.foundation.lazy.grid.TvLazyGridItemSpanScope
import androidx.tv.foundation.lazy.grid.TvLazyVerticalGrid
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.common.utils.StickyFooterArrangement
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.statistics.StatisticsViewState
import fr.shining_cat.simplehiit.android.tv.ui.statistics.components.StatisticCardComponent
import fr.shining_cat.simplehiit.android.tv.ui.statistics.components.StatisticsHeaderComponent
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shining_cat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun StatisticsNominalContent(
    openUserPicker: () -> Unit = {},
    deleteAllSessionsForUser: (User) -> Unit = {},
    viewState: StatisticsViewState.Nominal,
    hiitLogger: HiitLogger? = null
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        //we don't want the name to scroll along with the grid (sticky header)
        StatisticsHeaderComponent(
            openUserPicker = openUserPicker,
            currentUserName = viewState.user.name,
            showUsersSwitch = viewState.showUsersSwitch
        )
        //
        val columnsCount = 2
        val gridPadding = 16.dp
        val doubleSpan: (TvLazyGridItemSpanScope) -> TvGridItemSpan =
            { TvGridItemSpan(columnsCount) }
        TvLazyVerticalGrid(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            columns = TvGridCells.Fixed(2),
            verticalArrangement = StickyFooterArrangement(
                gridPadding,
                hiitLogger
            ),
            horizontalArrangement = Arrangement.spacedBy(gridPadding)
        ) {
            items(viewState.statistics.size) {
                val displayStatistic = viewState.statistics[it]
                StatisticCardComponent(displayStatistic)
            }
            item(span = doubleSpan) {
                Spacer(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp)
                )
                ButtonText(
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 16.dp),
                    onClick = { deleteAllSessionsForUser(viewState.user) },
                    label = stringResource(
                        id = R.string.reset_statistics_button_label,
                        viewState.user.name
                    )
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
private fun StatisticsNominalContentPreviewTabletLandscape(
    @PreviewParameter(StatisticsNominalContentPreviewParameterProvider::class) viewState: StatisticsViewState.Nominal
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticsNominalContent(viewState = viewState)
        }
    }
}

internal class StatisticsNominalContentPreviewParameterProvider :
    PreviewParameterProvider<StatisticsViewState.Nominal> {
    override val values: Sequence<StatisticsViewState.Nominal>
        get() = sequenceOf(
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                statistics = listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                ),
                showUsersSwitch = true
            ),
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                statistics = listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                ),
                showUsersSwitch = false
            ),
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                statistics = listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                ),
                showUsersSwitch = true
            )
        )
}
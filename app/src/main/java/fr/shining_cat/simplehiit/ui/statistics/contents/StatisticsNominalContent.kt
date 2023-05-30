package fr.shining_cat.simplehiit.ui.statistics.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyGridItemSpanScope
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.commondomain.models.DisplayStatisticType
import fr.shining_cat.simplehiit.commondomain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.ui.statistics.StatisticsViewState
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun StatisticsNominalContent(
    deleteAllSessionsForUser: (User) -> Unit,
    viewState: StatisticsViewState.Nominal,
    hiitLogger: HiitLogger?
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxSize()
    ) {
        //we don't want the name to scroll along with the grid (sticky header)
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = viewState.user.name
        )
        //
        val columnsCount = 2
        val gridPadding = 16.dp
        val doubleSpan: (LazyGridItemSpanScope) -> GridItemSpan = { GridItemSpan(columnsCount) }
        LazyVerticalGrid(
            modifier = Modifier
                .padding(8.dp)
                .fillMaxSize(),
            columns = GridCells.Fixed(2),
            verticalArrangement = StickyFooterArrangement(gridPadding, hiitLogger),
            horizontalArrangement = Arrangement.spacedBy(gridPadding)
        ) {
            items(viewState.statistics.size) {
                val displayStatistic = viewState.statistics[it]
                StatisticCardComponent(displayStatistic)
            }
            item(span = doubleSpan) {
                Divider(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 8.dp), thickness = 1.dp
                )
                TextButton(
                    modifier = Modifier
                        .padding(horizontal = 0.dp, vertical = 16.dp),
                    onClick = { deleteAllSessionsForUser(viewState.user) }
                ) {
                    Text(
                        text = stringResource(
                            id = R.string.reset_statistics_button_label,
                            viewState.user.name
                        )
                    )
                }
            }
        }
    }
}

internal class StickyFooterArrangement(
    private val verticalPadding: Dp,
    private val hiitLogger: HiitLogger?
    ) : Arrangement.Vertical {

    //for some reason, if the resulting height of the grid is bigger than the visible space available (ie, if it needs to be scrollable)
    // then spacing will be used to render the padding between items and the rows' y value we set in Density.arrange will be ignored
    //however in the opposite case, spacing's value is ignored, and instead the rows' y value is used
    //thus we need to both override spacing to return our own value, and to include it in the rows' y calculation

    override val spacing: Dp
        get() = verticalPadding

    override fun Density.arrange(totalSize: Int, sizes: IntArray, outPositions: IntArray) {
        var y = 0
        sizes.forEachIndexed { index, size ->
            outPositions[index] = y
            y += (size + verticalPadding.toPx().toInt())
        }
        if (y < totalSize) {
            val lastIndex = outPositions.lastIndex
            outPositions[lastIndex] = totalSize - sizes.last()
        }
    }
}

@Composable
private fun StatisticCardComponent(statistic: DisplayedStatistic) {
    val label = when (statistic.type) {
        DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(R.string.sessions_total)
        DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(R.string.time_total)
        DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(R.string.average)
        DisplayStatisticType.LONGEST_STREAK -> stringResource(R.string.longest_streak)
        DisplayStatisticType.CURRENT_STREAK -> stringResource(R.string.current_streak)
        DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> stringResource(R.string.average_sessions_week)
    }
    Card() {
        Text(
            text = statistic.displayValue,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium
        )
        Text(
            text = label,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsContentNominalPreview(
    @PreviewParameter(StatisticsContentNominalPreviewParameterProvider::class) viewState: StatisticsViewState.Nominal
) {
    SimpleHiitTheme {
        Surface{
            StatisticsNominalContent(
                deleteAllSessionsForUser = {},
                viewState = viewState,
                hiitLogger = null
            )
        }
    }
}

internal class StatisticsContentNominalPreviewParameterProvider :
    PreviewParameterProvider<StatisticsViewState.Nominal> {
    override val values: Sequence<StatisticsViewState.Nominal>
        get() = sequenceOf(
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                )
            ),
            StatisticsViewState.Nominal(
                user = User(name = "Sven Svensson"),
                listOf(
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

                    )
            )
        )
}
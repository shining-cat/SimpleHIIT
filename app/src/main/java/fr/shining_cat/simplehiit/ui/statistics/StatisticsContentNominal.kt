package fr.shining_cat.simplehiit.ui.statistics

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.DisplayStatisticType
import fr.shining_cat.simplehiit.domain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@Composable
fun StatisticsContentNominal(
    deleteAllSessionsForUser: (User) -> Unit,
    viewState: StatisticsViewState.StatisticsNominal
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineLarge,
            text = viewState.user.name
        )
        Box(modifier = Modifier.fillMaxSize()) {
            LazyVerticalGrid(
//                modifier = Modifier.fillMaxSize(),
                columns = GridCells.Fixed(2),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                userScrollEnabled = false
            ) {
                items(viewState.statistics.size) {
                    val displayStatistic = viewState.statistics[it]
                    StatisticCard(displayStatistic)
                }
            }
        }
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        TextButton(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
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

@Composable
private fun StatisticCard(statistic: DisplayedStatistic) {
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
        Text(text = label,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineSmall
        )
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun StatisticsContentNominalPreview(
    @PreviewParameter(StatisticsContentNominalPreviewParameterProvider::class) viewState: StatisticsViewState.StatisticsNominal
) {
    SimpleHiitTheme {
        StatisticsContentNominal({}, viewState)
    }
}

internal class StatisticsContentNominalPreviewParameterProvider :
    PreviewParameterProvider<StatisticsViewState.StatisticsNominal> {
    override val values: Sequence<StatisticsViewState.StatisticsNominal>
        get() = sequenceOf(
            StatisticsViewState.StatisticsNominal(
                user = User(name = "Sven Svensson"),
                listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK)
                )
            )
        )
}
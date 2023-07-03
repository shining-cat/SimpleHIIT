package fr.shining_cat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shining_cat.simplehiit.domain.common.models.DisplayedStatistic

@Composable
fun StatisticCardComponent(statistic: DisplayedStatistic) {
    val label = when (statistic.type) {
        DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(R.string.sessions_total)
        DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(R.string.time_total)
        DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(R.string.average)
        DisplayStatisticType.LONGEST_STREAK -> stringResource(R.string.longest_streak)
        DisplayStatisticType.CURRENT_STREAK -> stringResource(R.string.current_streak)
        DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> stringResource(R.string.average_sessions_week)
    }
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface,
            contentColor = MaterialTheme.colorScheme.onSurface
        ),
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = if (statistic.type == DisplayStatisticType.LONGEST_STREAK || statistic.type == DisplayStatisticType.CURRENT_STREAK) {
                    val numberOfDays = statistic.displayValue.toInt()
                    pluralStringResource(
                        R.plurals.statistics_number_of_days,
                        numberOfDays,
                        numberOfDays
                    )
                    // "When using the pluralStringResource method, you need to pass the count twice if your string includes string formatting with a number"
                    // see: https://developer.android.com/jetpack/compose/resources#string-plurals
                } else {
                    statistic.displayValue
                },
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
}
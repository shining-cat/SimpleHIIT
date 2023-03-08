package fr.shining_cat.simplehiit.ui.statistics

import androidx.compose.foundation.layout.*
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.R

@Composable
fun StatisticsContentNominal(
    deleteAllSessionsForUser: (User) -> Unit,
    viewState: StatisticsViewState.StatisticsNominal,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Text(text = stringResource(id = R.string.sessions_total, viewState.totalNumberOfSessions))
        Text(text = stringResource(id = R.string.time_total, viewState.cumulatedTimeOfExerciseFormatted))
        Text(text = stringResource(id = R.string.average, viewState.averageSessionLengthFormatted))
        Text(text = stringResource(id = R.string.longest_streak, viewState.longestStreakDays))
        Text(text = stringResource(id = R.string.current_streak, viewState.currentStreakDays))
        Text(text = stringResource(id = R.string.average_sessions_week, viewState.averageNumberOfSessionsPerWeek))
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
            Text(text = stringResource(id = R.string.reset_statistics_button_label, viewState.user.name))
        }
    }

}

@Composable
private fun StatisticField(
    label: String,
    value: String
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.headlineSmall)
        Text(text = value, style = MaterialTheme.typography.headlineSmall)
    }
}
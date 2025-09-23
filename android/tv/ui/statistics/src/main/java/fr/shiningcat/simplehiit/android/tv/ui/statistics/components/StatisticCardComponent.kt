package fr.shiningcat.simplehiit.android.tv.ui.statistics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Border
import androidx.tv.material3.Glow
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic

@Composable
fun StatisticCardComponent(
    modifier: Modifier = Modifier,
    statistic: DisplayedStatistic,
) {
    val label =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(R.string.sessions_total)
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(R.string.time_total)
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(R.string.average)
            DisplayStatisticType.LONGEST_STREAK -> stringResource(R.string.longest_streak)
            DisplayStatisticType.CURRENT_STREAK -> stringResource(R.string.current_streak)
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> stringResource(R.string.average_sessions_week)
        }

    Surface(
        modifier = modifier,
        glow =
            Glow(
                elevationColor = MaterialTheme.colorScheme.onBackground,
                elevation = 5.dp,
            ),
        border =
            Border(
                border =
                    BorderStroke(
                        width = dimensionResource(R.dimen.stroke_025),
                        color = MaterialTheme.colorScheme.primary,
                    ),
                shape = MaterialTheme.shapes.small,
            ),
        colors =
            SurfaceDefaults.colors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        shape = MaterialTheme.shapes.small,
    ) {
        Column(modifier = Modifier.padding(dimensionResource(R.dimen.spacing_2))) {
            Text(
                text =
                    if (statistic.type == DisplayStatisticType.LONGEST_STREAK || statistic.type == DisplayStatisticType.CURRENT_STREAK) {
                        val numberOfDays = statistic.displayValue.toInt()
                        pluralStringResource(
                            R.plurals.statistics_number_of_days,
                            numberOfDays,
                            numberOfDays,
                        )
                        // "When using the pluralStringResource method, you need to pass the count twice if your string includes string formatting with a number"
                        // see: https://developer.android.com/jetpack/compose/resources#string-plurals
                    } else {
                        statistic.displayValue
                    },
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineMedium,
            )
            Text(
                text = label,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.headlineSmall,
            )
        }
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@PreviewTvScreensNoUi
@Composable
private fun StatisticCardComponentPreview(
    @PreviewParameter(StatisticCardComponentPreviewParameterProvider::class) statistic: DisplayedStatistic,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            StatisticCardComponent(statistic = statistic)
        }
    }
}

internal class StatisticCardComponentPreviewParameterProvider : PreviewParameterProvider<DisplayedStatistic> {
    override val values: Sequence<DisplayedStatistic>
        get() =
            sequenceOf(
                DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
            )
}

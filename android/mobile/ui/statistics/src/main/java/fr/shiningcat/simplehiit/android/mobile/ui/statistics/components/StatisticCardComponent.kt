package fr.shiningcat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.R
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.commonresources.R as CommonResources

@Composable
fun StatisticCardComponent(statistic: DisplayedStatistic) {
    val label =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(CommonResources.string.sessions_total)
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(CommonResources.string.time_total)
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(CommonResources.string.average)
            DisplayStatisticType.LONGEST_STREAK -> stringResource(CommonResources.string.longest_streak)
            DisplayStatisticType.CURRENT_STREAK -> stringResource(CommonResources.string.current_streak)
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> stringResource(CommonResources.string.average_sessions_week)
        }
    val illustrationDrawableId =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> CommonResources.drawable.laurels
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> CommonResources.drawable.clock
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> CommonResources.drawable.figure_and_clock
            DisplayStatisticType.LONGEST_STREAK -> CommonResources.drawable.trophy
            DisplayStatisticType.CURRENT_STREAK -> CommonResources.drawable.calendar_checked
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> CommonResources.drawable.figure_and_calendar
        }
    val illustrationContentDescription =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(CommonResources.string.laurels_icon_content_description)
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(CommonResources.string.clock_icon_content_description)
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(CommonResources.string.figure_and_clock_icon_content_description)
            DisplayStatisticType.LONGEST_STREAK -> stringResource(CommonResources.string.trophy_icon_content_description)
            DisplayStatisticType.CURRENT_STREAK -> stringResource(CommonResources.string.calendar_icon_content_description)
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK ->
                stringResource(
                    CommonResources.string.figure_and_calendar_icon_content_description,
                )
        }
    Card(
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Column(modifier = Modifier.padding(dimensionResource(CommonResources.dimen.spacing_2))) {
            Image(
                modifier =
                    Modifier
                        .size(adaptDpToFontScale(dimensionResource(R.dimen.stats_card_illustration_size)))
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            horizontal = 0.dp,
                            vertical = dimensionResource(CommonResources.dimen.spacing_3),
                        ),
                painter = painterResource(id = illustrationDrawableId),
                contentDescription = illustrationContentDescription,
            )
            Text(
                text =
                    if (statistic.type == DisplayStatisticType.LONGEST_STREAK || statistic.type == DisplayStatisticType.CURRENT_STREAK) {
                        val numberOfDays = statistic.displayValue.toInt()
                        pluralStringResource(
                            CommonResources.plurals.statistics_number_of_days,
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
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun StatisticCardComponentPreview(
    @PreviewParameter(StatisticCardComponentPreviewParameterProvider::class) statistic: DisplayedStatistic,
) {
    SimpleHiitMobileTheme {
        Surface {
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

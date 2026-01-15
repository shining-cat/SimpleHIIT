/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.statistics.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.R
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

/**
 * Displays a statistic card with an icon, value, and label.
 *
 * @param modifier Modifier to be applied to the card
 * @param statistic The statistic data to display
 * @param shouldFillMaxHeight If true, the card will fill the parent's height; if false, it will wrap content
 */
@Composable
fun StatisticCardComponent(
    modifier: Modifier = Modifier,
    statistic: DisplayedStatistic,
    shouldFillMaxHeight: Boolean = false,
) {
    val label =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> stringResource(CommonResourcesR.string.sessions_total)
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> stringResource(CommonResourcesR.string.time_total)
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> stringResource(CommonResourcesR.string.average)
            DisplayStatisticType.LONGEST_STREAK -> stringResource(CommonResourcesR.string.longest_streak)
            DisplayStatisticType.CURRENT_STREAK -> stringResource(CommonResourcesR.string.current_streak)
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> stringResource(CommonResourcesR.string.average_sessions_week)
        }
    val illustrationDrawableId =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> CommonResourcesR.drawable.laurels_bicolor_primary
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> CommonResourcesR.drawable.clock_bicolor_primary
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> CommonResourcesR.drawable.figure_and_clock_bicolor_primary
            DisplayStatisticType.LONGEST_STREAK -> CommonResourcesR.drawable.trophy_bicolor_primary
            DisplayStatisticType.CURRENT_STREAK -> CommonResourcesR.drawable.calendar_checked_bicolor_primary
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> CommonResourcesR.drawable.figure_and_calendar_bicolor_primary
        }
    val illustrationContentDescription =
        when (statistic.type) {
            DisplayStatisticType.TOTAL_SESSIONS_NUMBER -> {
                stringResource(CommonResourcesR.string.laurels_icon_content_description)
            }
            DisplayStatisticType.TOTAL_EXERCISE_TIME -> {
                stringResource(CommonResourcesR.string.clock_icon_content_description)
            }
            DisplayStatisticType.AVERAGE_SESSION_LENGTH -> {
                stringResource(CommonResourcesR.string.figure_and_clock_icon_content_description)
            }
            DisplayStatisticType.LONGEST_STREAK -> {
                stringResource(CommonResourcesR.string.trophy_icon_content_description)
            }
            DisplayStatisticType.CURRENT_STREAK -> {
                stringResource(CommonResourcesR.string.calendar_icon_content_description)
            }
            DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK -> {
                stringResource(
                    CommonResourcesR.string.figure_and_calendar_icon_content_description,
                )
            }
        }
    Card(
        modifier = modifier,
        colors =
            CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surface,
                contentColor = MaterialTheme.colorScheme.onSurface,
            ),
        border =
            BorderStroke(
                width = dimensionResource(CommonResourcesR.dimen.stroke_012),
                color = MaterialTheme.colorScheme.onSurface,
            ),
    ) {
        Column(
            modifier =
                Modifier
                    .padding(dimensionResource(CommonResourcesR.dimen.spacing_2))
                    .fillMaxHeight(),
        ) {
            Image(
                modifier =
                    Modifier
                        .size(adaptDpToFontScale(dimensionResource(R.dimen.stats_card_illustration_size)))
                        .align(Alignment.CenterHorizontally)
                        .padding(
                            bottom = dimensionResource(CommonResourcesR.dimen.spacing_3),
                        ),
                painter = painterResource(id = illustrationDrawableId),
                contentDescription = illustrationContentDescription,
            )
            if (shouldFillMaxHeight) {
                Spacer(modifier = Modifier.weight(1f))
            }
            Text(
                text =
                    if (statistic.type == DisplayStatisticType.LONGEST_STREAK || statistic.type == DisplayStatisticType.CURRENT_STREAK) {
                        val numberOfDays = statistic.displayValue.toInt()
                        pluralStringResource(
                            CommonResourcesR.plurals.statistics_number_of_days,
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
                style = MaterialTheme.typography.bodyLarge,
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

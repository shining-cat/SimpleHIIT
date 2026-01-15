/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.mobile.ui.statistics.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.OnSurfaceTextButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticCardComponent
import fr.shiningcat.simplehiit.android.mobile.ui.statistics.components.StatisticsHeaderComponent
import fr.shiningcat.simplehiit.android.shared.core.ui.utils.StickyFooterArrangement
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewState

@Composable
fun StatisticsNominalContent(
    modifier: Modifier = Modifier,
    deleteAllSessionsForUser: (User) -> Unit = {},
    nominalViewState: StatisticsViewState.Nominal,
    uiArrangement: UiArrangement,
    openUserPicker: () -> Unit = {},
    hiitLogger: HiitLogger? = null,
) {
    Column(
        modifier =
            modifier
                .fillMaxSize()
                .padding(
                    top = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(R.dimen.spacing_1) else 0.dp,
                    start = dimensionResource(R.dimen.spacing_1),
                    end = dimensionResource(R.dimen.spacing_1),
                ),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        if (uiArrangement == UiArrangement.VERTICAL) {
            // we don't want the name to scroll along with the grid (sticky header)
            StatisticsHeaderComponent(
                currentUserName = nominalViewState.user.name,
                showUsersSwitch = nominalViewState.showUsersSwitch,
                uiArrangement = uiArrangement,
                openUserPicker = openUserPicker,
            )
        }
        //
        val fontscale = LocalDensity.current.fontScale
        val columnsCount = if (fontscale > 1.3f && uiArrangement == UiArrangement.VERTICAL) 1 else 2
        val spacingDp = dimensionResource(R.dimen.spacing_2)

        // Group statistics into rows based on column count
        val statisticsRows = nominalViewState.statistics.chunked(columnsCount)

        LazyColumn(
            modifier =
                Modifier
                    .padding(top = if (uiArrangement == UiArrangement.VERTICAL) dimensionResource(R.dimen.spacing_1) else 0.dp)
                    .fillMaxSize(),
            verticalArrangement = StickyFooterArrangement(spacingDp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            if (uiArrangement == UiArrangement.HORIZONTAL) {
                item {
                    StatisticsHeaderComponent(
                        currentUserName = nominalViewState.user.name,
                        showUsersSwitch = nominalViewState.showUsersSwitch,
                        uiArrangement = uiArrangement,
                        openUserPicker = openUserPicker,
                    )
                }
            }

            items(statisticsRows.size) { rowIndex ->
                val rowStatistics = statisticsRows[rowIndex]
                Row(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .run {
                                if (columnsCount == 2) {
                                    this.height(IntrinsicSize.Max)
                                } else {
                                    this
                                }
                            },
                    horizontalArrangement = Arrangement.spacedBy(spacingDp),
                ) {
                    rowStatistics.forEach { statistic ->
                        StatisticCardComponent(
                            modifier = Modifier.weight(1f),
                            statistic = statistic,
                            shouldFillMaxHeight = columnsCount == 2,
                        )
                    }
                }
            }

            item {
                HorizontalDivider(
                    modifier =
                        Modifier
                            .fillMaxWidth()
                            .padding(vertical = dimensionResource(R.dimen.spacing_2)),
                    thickness = Dp.Hairline,
                )
                OnSurfaceTextButton(
                    modifier =
                        Modifier
                            .padding(
                                top = dimensionResource(R.dimen.spacing_3),
                                bottom = dimensionResource(R.dimen.spacing_3),
                            ),
                    onClick = { deleteAllSessionsForUser(nominalViewState.user) },
                ) {
                    Text(
                        text =
                            stringResource(
                                id = R.string.reset_statistics_button_label,
                                nominalViewState.user.name,
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
    val previewUiArrangement = currentUiArrangement()
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
        get() {
            val stats =
                listOf(
                    DisplayedStatistic("73", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                    DisplayedStatistic("5h 23mn 64s", DisplayStatisticType.TOTAL_EXERCISE_TIME),
                    DisplayedStatistic("25", DisplayStatisticType.LONGEST_STREAK),
                    DisplayedStatistic("7", DisplayStatisticType.CURRENT_STREAK),
                    DisplayedStatistic("15mn 13s", DisplayStatisticType.AVERAGE_SESSION_LENGTH),
                    DisplayedStatistic("3,5", DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK),
                )
            return sequenceOf(
                StatisticsViewState.Nominal(
                    user = User(name = "Sven Svensson"),
                    statistics = stats,
                    showUsersSwitch = true,
                ),
                StatisticsViewState.Nominal(
                    user = User(name = "Alice"),
                    statistics = stats,
                    showUsersSwitch = false,
                ),
                StatisticsViewState.Nominal(
                    user = User(name = "Bob"),
                    statistics = stats + stats + stats + stats + stats, // Lots of stats for scrolling
                    showUsersSwitch = true,
                ),
            )
        }
}

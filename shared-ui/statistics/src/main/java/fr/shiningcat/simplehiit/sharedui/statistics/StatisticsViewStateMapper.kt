/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase

class StatisticsViewStateMapper(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    @Suppress("UNUSED_PARAMETER")
    private val logger: HiitLogger,
) {
    fun mapUsersError(errorCode: DomainError): StatisticsViewState =
        if (errorCode == DomainError.NO_USERS_FOUND) {
            StatisticsViewState.NoUsers
        } else {
            StatisticsViewState.FatalError(errorCode.code)
        }

    fun map(
        showUsersSwitch: Boolean,
        userStats: UserStatistics,
    ): StatisticsViewState {
        if (userStats.totalNumberOfSessions == 0) {
            return StatisticsViewState.NoSessions(
                user = userStats.user,
                showUsersSwitch = showUsersSwitch,
            )
        }
        //
        val cumulatedTimeOfExerciseFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = userStats.cumulatedTimeOfExerciseMs,
                formatStyle = DurationFormatStyle.SHORT,
            )
        val averageSessionLengthFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = userStats.averageSessionLengthMs,
                formatStyle = DurationFormatStyle.SHORT,
            )
        val displayStatistics =
            listOf(
                DisplayedStatistic(
                    userStats.totalNumberOfSessions.toString(),
                    DisplayStatisticType.TOTAL_SESSIONS_NUMBER,
                ),
                DisplayedStatistic(
                    cumulatedTimeOfExerciseFormatted,
                    DisplayStatisticType.TOTAL_EXERCISE_TIME,
                ),
                DisplayedStatistic(
                    userStats.longestStreakDays.toString(),
                    DisplayStatisticType.LONGEST_STREAK,
                ),
                DisplayedStatistic(
                    userStats.currentStreakDays.toString(),
                    DisplayStatisticType.CURRENT_STREAK,
                ),
                DisplayedStatistic(
                    averageSessionLengthFormatted,
                    DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                ),
                DisplayedStatistic(
                    userStats.averageNumberOfSessionsPerWeek,
                    DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                ),
            )
        return StatisticsViewState.Nominal(
            user = userStats.user,
            statistics = displayStatistics,
            showUsersSwitch = showUsersSwitch,
        )
    }
}

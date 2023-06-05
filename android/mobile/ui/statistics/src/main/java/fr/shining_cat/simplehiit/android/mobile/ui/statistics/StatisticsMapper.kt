package fr.shining_cat.simplehiit.android.mobile.ui.statistics

import fr.shining_cat.simplehiit.commondomain.models.DisplayStatisticType
import fr.shining_cat.simplehiit.commondomain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.UserStatistics
import fr.shining_cat.simplehiit.commondomain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

class StatisticsMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val hiitLogger: HiitLogger
) {

    fun map(
        userStats: UserStatistics,
        durationStringFormatter: DurationStringFormatter
    ): StatisticsViewState {
        if (userStats.totalNumberOfSessions == 0) return StatisticsViewState.NoSessions(user = userStats.user)
        //
        val cumulatedTimeOfExerciseFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                userStats.cumulatedTimeOfExerciseMs,
                durationStringFormatter
            )
        val averageSessionLengthFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                userStats.averageSessionLengthMs,
                durationStringFormatter
            )
        val displayStatistics = listOf(
            DisplayedStatistic(
                userStats.totalNumberOfSessions.toString(),
                DisplayStatisticType.TOTAL_SESSIONS_NUMBER
            ),
            DisplayedStatistic(
                cumulatedTimeOfExerciseFormatted,
                DisplayStatisticType.TOTAL_EXERCISE_TIME
            ),
            DisplayedStatistic(
                userStats.longestStreakDays.toString(),
                DisplayStatisticType.LONGEST_STREAK
            ),
            DisplayedStatistic(
                userStats.currentStreakDays.toString(),
                DisplayStatisticType.CURRENT_STREAK
            ),
            DisplayedStatistic(
                averageSessionLengthFormatted,
                DisplayStatisticType.AVERAGE_SESSION_LENGTH
            ),
            DisplayedStatistic(
                userStats.averageNumberOfSessionsPerWeek,
                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK
            ),
        )
        return StatisticsViewState.Nominal(
            user = userStats.user,
            statistics = displayStatistics
        )
    }
}
package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.domain.models.UserStatistics
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class StatisticsMapper @Inject constructor(private val hiitLogger: HiitLogger) {

    fun map(userStats: UserStatistics): StatisticsViewState {
        return StatisticsViewState.StatisticsNominal(
            userName = userStats.userName,
            totalNumberOfSessions = userStats.totalNumberOfSessions,
            cumulatedTimeOfExerciseSeconds = userStats.cumulatedTimeOfExerciseSeconds,
            averageSessionLengthSeconds = userStats.averageSessionLengthSeconds,
            longestStreakDays = userStats.longestStreakDays,
            currentStreakDays = userStats.currentStreakDays,
            averageNumberOfSessionsPerWeek = userStats.averageNumberOfSessionsPerWeek,
        )
    }
}
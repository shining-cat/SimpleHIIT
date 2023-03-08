package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.domain.models.UserStatistics
import fr.shining_cat.simplehiit.domain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class StatisticsMapper @Inject constructor(
    private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
    private val hiitLogger: HiitLogger
) {


    fun map(
        userStats: UserStatistics,
        formatStringHoursMinutesSeconds: String,
        formatStringHoursMinutesNoSeconds: String,
        formatStringHoursNoMinutesNoSeconds: String,
        formatStringMinutesSeconds: String,
        formatStringMinutesNoSeconds: String,
        formatStringSeconds: String
    ): StatisticsViewState {
        val cumulatedTimeOfExerciseFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                userStats.cumulatedTimeOfExerciseMs,
                formatStringHoursMinutesSeconds,
                formatStringHoursMinutesNoSeconds,
                formatStringHoursNoMinutesNoSeconds,
                formatStringMinutesSeconds,
                formatStringMinutesNoSeconds,
                formatStringSeconds
            )
        val averageSessionLengthFormatted =
            formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                userStats.averageSessionLengthMs,
                formatStringHoursMinutesSeconds,
                formatStringHoursMinutesNoSeconds,
                formatStringHoursNoMinutesNoSeconds,
                formatStringMinutesSeconds,
                formatStringMinutesNoSeconds,
                formatStringSeconds
            )
        return StatisticsViewState.StatisticsNominal(
            user = userStats.user,
            totalNumberOfSessions = userStats.totalNumberOfSessions,
            cumulatedTimeOfExerciseFormatted = cumulatedTimeOfExerciseFormatted,
            averageSessionLengthFormatted = averageSessionLengthFormatted,
            longestStreakDays = userStats.longestStreakDays,
            currentStreakDays = userStats.currentStreakDays,
            averageNumberOfSessionsPerWeek = userStats.averageNumberOfSessionsPerWeek
        )
    }
}
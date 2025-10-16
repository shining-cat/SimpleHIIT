package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.DisplayStatisticType
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import javax.inject.Inject

class StatisticsViewStateMapper
    @Inject
    constructor(
        private val formatLongDurationMsAsSmallestHhMmSsStringUseCase: FormatLongDurationMsAsSmallestHhMmSsStringUseCase,
        @Suppress("UNUSED_PARAMETER")
        private val hiitLogger: HiitLogger,
    ) {
        fun map(
            allUsers: List<User>,
            selectedUserStatistics: UserStatistics,
        ): StatisticsViewState {
            if (selectedUserStatistics.totalNumberOfSessions == 0) {
                return StatisticsViewState.NoSessions(
                    allUsers = allUsers,
                    selectedUser = selectedUserStatistics.user,
                )
            }
            //
            val cumulatedTimeOfExerciseFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = selectedUserStatistics.cumulatedTimeOfExerciseMs,
                    formatStyle = DurationFormatStyle.SHORT,
                )
            val averageSessionLengthFormatted =
                formatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = selectedUserStatistics.averageSessionLengthMs,
                    formatStyle = DurationFormatStyle.SHORT,
                )
            val displayStatistics =
                listOf(
                    DisplayedStatistic(
                        selectedUserStatistics.totalNumberOfSessions.toString(),
                        DisplayStatisticType.TOTAL_SESSIONS_NUMBER,
                    ),
                    DisplayedStatistic(
                        cumulatedTimeOfExerciseFormatted,
                        DisplayStatisticType.TOTAL_EXERCISE_TIME,
                    ),
                    DisplayedStatistic(
                        selectedUserStatistics.longestStreakDays.toString(),
                        DisplayStatisticType.LONGEST_STREAK,
                    ),
                    DisplayedStatistic(
                        selectedUserStatistics.currentStreakDays.toString(),
                        DisplayStatisticType.CURRENT_STREAK,
                    ),
                    DisplayedStatistic(
                        averageSessionLengthFormatted,
                        DisplayStatisticType.AVERAGE_SESSION_LENGTH,
                    ),
                    DisplayedStatistic(
                        selectedUserStatistics.averageNumberOfSessionsPerWeek,
                        DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK,
                    ),
                )
            return StatisticsViewState.Nominal(
                allUsers = allUsers,
                selectedUser = selectedUserStatistics.user,
                selectedUserStatistics = displayStatistics,
            )
        }
    }

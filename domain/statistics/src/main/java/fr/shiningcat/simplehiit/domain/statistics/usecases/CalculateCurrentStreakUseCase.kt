package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CalculateCurrentStreakUseCase(
    private val consecutiveDaysOrCloserUseCase: ConsecutiveDaysOrCloserUseCase,
    private val defaultDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    suspend fun execute(
        timestamps: List<Long>,
        now: Long,
    ): Int =
        withContext(defaultDispatcher) {
            // "now" does not represent a session but the time of evaluation for the current streak.
            // if the original list is empty, the expected current streak length is 0
            // So if we check on the same day, or before the end of the following day of the last session, the current streak is not considered broken
            // On the contrary: if "now" is evaluated as NON_CONSECUTIVE_DAYS with the last actual session the expected current streak length is 0
            val timestampsIncludingNow = timestamps.plus(now)
            val sortedTimestampsFromLast = timestampsIncludingNow.sortedByDescending { it }
            var streakCounter = 0
            streakLoop@ for ((index, timestamp) in sortedTimestampsFromLast.withIndex()) {
                if (index + 1 == sortedTimestampsFromLast.size) break
                //
                val timeStamp2 = sortedTimestampsFromLast[index + 1]
                val areConsecutiveDaysOrLess =
                    consecutiveDaysOrCloserUseCase.execute(timestamp, timeStamp2)
                when (areConsecutiveDaysOrLess) {
                    Consecutiveness.SAME_DAY -> {
                        if (index == 0) {
                            // if "now" and the last session are SAME_DAY, then we want to count today in the streak
                            streakCounter++
                        } else {
                            // the two sessions occurred on the same day, not increasing counter, but not breaking streak
                        }
                    }
                    Consecutiveness.CONSECUTIVE_DAYS -> {
                        streakCounter++
                    }
                    Consecutiveness.NON_CONSECUTIVE_DAYS -> {
                        break@streakLoop
                    }
                }
            }
            streakCounter
        }
}

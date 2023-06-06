package fr.shining_cat.simplehiit.domain.statistics.usecases

import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CalculateCurrentStreakUseCase @Inject constructor(
    private val consecutiveDaysOrCloserUseCase: ConsecutiveDaysOrCloserUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(timestamps: List<Long>, now: Long): Int {
        return withContext(defaultDispatcher) {
            //"now" does not represent a session but the time of evaluation for the current streak.
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
                simpleHiitLogger.d(
                    "CalculateCurrentStreakUseCase",
                    "execute::areConsecutiveDaysOrLess = $areConsecutiveDaysOrLess"
                )
                when (areConsecutiveDaysOrLess) {
                    Consecutiveness.SAME_DAY -> {
                        if (index == 0) {
                            // if "now" and the last session are SAME_DAY, then we want to count today in the streak
                            streakCounter++
                        } else {
                            //the two sessions occurred on the same day, not increasing counter, but not breaking streak
                        }
                    }

                    Consecutiveness.CONSECUTIVE_DAYS -> streakCounter++
                    Consecutiveness.NON_CONSECUTIVE_DAYS -> break@streakLoop
                }
            }
            streakCounter
        }
    }

}
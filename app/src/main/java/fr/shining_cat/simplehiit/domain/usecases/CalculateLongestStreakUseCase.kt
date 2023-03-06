package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class CalculateLongestStreakUseCase @Inject constructor(
    private val consecutiveDaysOrCloserUseCase: ConsecutiveDaysOrCloserUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(timestamps: List<Long>, now: Long): Int {
        val timestampsIncludingNow = timestamps + now
        val sortedTimestampsFromLast = timestampsIncludingNow.sortedByDescending { it }
        var longestStreak = 0
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
                Consecutiveness.NON_CONSECUTIVE_DAYS -> {
                    // if the current evaluated streak is the longest, update result
                    if (streakCounter > longestStreak) longestStreak = streakCounter
                    //current evaluated streak was broken, reset rolling counter for next streak
                    streakCounter = 0
                }
            }
        }
        return maxOf(streakCounter, longestStreak)
    }

}
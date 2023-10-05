package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.util.Calendar
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * This usecase checks for consecutiveness of 2 timestamps in terms of CALENDAR days.
 * It does not use the number of hours between each to decide (ie NOT checking if they are
 * distant by less than 24h), but checks if the 2 timestamps are separated by a full calendar
 * day. If not, then they are from 2 consecutive days or the same day
 *
 * A day is here defined as the period between 00:00 and 23:59:59:999
 * The local time zone AT THE TIME OF EVALUATION will be used
 */

enum class Consecutiveness { SAME_DAY, CONSECUTIVE_DAYS, NON_CONSECUTIVE_DAYS }

class ConsecutiveDaysOrCloserUseCase @Inject constructor(
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    @Suppress("UNUSED_PARAMETER")
    private val hiitLogger: HiitLogger
) {

    suspend fun execute(timeStamp1: Long, timeStamp2: Long): Consecutiveness {
        return withContext(defaultDispatcher) {
            when (getNumberOfFullDaysBetween2Timestamps(timeStamp1, timeStamp2)) {
                0 -> Consecutiveness.SAME_DAY
                1 -> Consecutiveness.CONSECUTIVE_DAYS
                else -> Consecutiveness.NON_CONSECUTIVE_DAYS
            }
        }
    }

    private suspend fun getNumberOfFullDaysBetween2Timestamps(
        timeStamp1: Long,
        timeStamp2: Long
    ): Int {
        return withContext(defaultDispatcher) {
            if (timeStamp1 == timeStamp2) {
                0
            } else {
                val earlyTimestampCal = Calendar.getInstance()
                earlyTimestampCal.timeInMillis = minOf(timeStamp1, timeStamp2)
                val lateTimestampCal = Calendar.getInstance()
                lateTimestampCal.timeInMillis = maxOf(timeStamp1, timeStamp2)
                val lateAtMidnight = setTimePartOfDateToMidnight(lateTimestampCal)
                val earlyAtMidnight = setTimePartOfDateToMidnight(earlyTimestampCal)
                TimeUnit.MILLISECONDS.toDays(lateAtMidnight - earlyAtMidnight).toInt()
            }
        }
    }

    private fun setTimePartOfDateToMidnight(calendar: Calendar): Long {
        // reset hour, minutes, seconds and millis
        calendar[Calendar.HOUR_OF_DAY] = 0
        calendar[Calendar.MINUTE] = 0
        calendar[Calendar.SECOND] = 0
        calendar[Calendar.MILLISECOND] = 0
        return calendar.timeInMillis
    }

}
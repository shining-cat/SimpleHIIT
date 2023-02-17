package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlin.math.round

/**
 * This calculates an average over an arbitrary 7-days period,
 * rather than any form of predefined calendar week
 */
class CalculateAverageSessionsPerWeekUseCase(
    private val simpleHiitLogger: HiitLogger
) {

    private val weekLengthAsMs = 604800000L

    fun execute(timestamps: List<Long>, now: Long): Double {
        if (timestamps.isEmpty()) return 0.0
        val sortedTimestamps = timestamps.sorted()
        val elapsedTotalTimeMs = now - sortedTimestamps.first()
        val numberOfElapsedWeeks = elapsedTotalTimeMs.toDouble() / weekLengthAsMs.toDouble()
        val numberOfSessions = timestamps.size
        val averageSessionsPer7DaysPeriod =
            numberOfSessions.toDouble() / numberOfElapsedWeeks.toDouble()
        val roundedAverageSessionsPer7DaysPeriod =
            round(averageSessionsPer7DaysPeriod * 100.0) / 100.0
        return roundedAverageSessionsPer7DaysPeriod
    }

}
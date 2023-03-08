package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject
import kotlin.math.round

/**
 * This calculates an average over an arbitrary 7-days period,
 * rather than any form of predefined calendar week
 */
class CalculateAverageSessionsPerWeekUseCase @Inject constructor(
    private val simpleHiitLogger: HiitLogger
) {

    private val weekLengthAsMs = 604800000L

    fun execute(timestamps: List<Long>, now: Long): String {
        if (timestamps.isEmpty()) return "0"
        val sortedTimestamps = timestamps.sorted()
        val elapsedTotalTimeMs = now - sortedTimestamps.first()
        val numberOfElapsedWeeks = elapsedTotalTimeMs.toDouble() / weekLengthAsMs.toDouble()
        val numberOfSessions = timestamps.size
        val averageSessionsPer7DaysPeriod =
            numberOfSessions.toDouble() / numberOfElapsedWeeks
        val roundedAverageSessionsPer7DaysPeriod =
            round(averageSessionsPer7DaysPeriod * 100.0) / 100.0
        return if(roundedAverageSessionsPer7DaysPeriod - roundedAverageSessionsPer7DaysPeriod.toInt() == 0.00){
            //remove trailing 0
            roundedAverageSessionsPer7DaysPeriod.toInt().toString()
        } else {
            roundedAverageSessionsPer7DaysPeriod.toString()
        }
    }

}
package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.utils.HiitLogger

class CalculateLongestStreakUseCase(
    private val consecutiveDaysOrCloserUseCase: ConsecutiveDaysOrCloserUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(timestamps: List<Long>):Int{
        val sortedTimestampsFromLast = timestamps.sorted()
        var streakGreatestLength = 0
        streakLoop@ for((index, timestamp) in sortedTimestampsFromLast.withIndex()){

        }
        return streakGreatestLength
    }

}
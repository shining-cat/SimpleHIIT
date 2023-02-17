package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.utils.HiitLogger

class CalculateCurrentStreakUseCase(
    private val consecutiveDaysOrCloserUseCase: ConsecutiveDaysOrCloserUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(sessions: List<Session>):Int{
        val sortedSessionsFromLast = sessions.sortedByDescending { it.timeStamp }
        var streakCounter = 0
        streakLoop@ for((index, session) in sortedSessionsFromLast.withIndex()){
            if(index + 1 == sortedSessionsFromLast.size) break
            //
            val timeStamp1 = session.timeStamp
            val timeStamp2 = sortedSessionsFromLast[index + 1].timeStamp
            val areConsecutiveDaysOrLess = consecutiveDaysOrCloserUseCase.execute(timeStamp1, timeStamp2)
            when(areConsecutiveDaysOrLess){
                Consecutiveness.SAME_DAY -> {/*the two sessions occured on the same day, not increasing counter, but not breaking streak*/}
                Consecutiveness.CONSECUTIVE_DAYS -> streakCounter++
                Consecutiveness.NON_CONSECUTIVE_DAYS -> break@streakLoop
            }
        }
        return streakCounter
    }

}
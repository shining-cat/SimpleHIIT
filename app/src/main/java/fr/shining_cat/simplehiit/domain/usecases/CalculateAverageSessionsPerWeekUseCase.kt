package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.utils.HiitLogger

class CalculateAverageSessionsPerWeekUseCase(
    private val simpleHiitLogger: HiitLogger
) {

    fun execute(sessions: List<Session>):Int{
        val sortedSessions = sessions.sortedBy { it.timeStamp }
        return 1
    }

}
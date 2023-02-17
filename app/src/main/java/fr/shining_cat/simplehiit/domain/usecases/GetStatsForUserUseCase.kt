package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.models.UserStatistics
import fr.shining_cat.simplehiit.utils.HiitLogger
import java.util.concurrent.TimeUnit

class GetStatsForUserUseCase(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val calculateCurrentStreakUseCase: CalculateCurrentStreakUseCase,
    private val calculateLongestStreakUseCase: CalculateLongestStreakUseCase,
    private val calculateAverageSessionsPerWeekUseCase: CalculateAverageSessionsPerWeekUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User): UserStatistics {
        val sessionsForUser = simpleHiitRepository.getSessionsForUser(user)
        return when(sessionsForUser){
            is Output.Error -> {
                simpleHiitLogger.e("GetStatsForUserUseCase", "failed getting sessions, returning default values", sessionsForUser.exception)
                UserStatistics()
            }
            is Output.Success -> {
                mapListOfSessionsToStatistics(sessionsForUser.result)
            }
        }
    }

    private fun mapListOfSessionsToStatistics(sessions: List<Session>):UserStatistics{
        return if(sessions.isEmpty()){
            UserStatistics()
        } else{
            val totalNumberOfSessions = sessions.size
            val cumulatedTimeOfExerciseSeconds = sessions.sumOf{it.durationSeconds}
            val averageSessionLengthSeconds = (cumulatedTimeOfExerciseSeconds.toDouble() / totalNumberOfSessions.toDouble()).toInt()
            UserStatistics(
                totalNumberOfSessions = totalNumberOfSessions,
                cumulatedTimeOfExerciseSeconds = cumulatedTimeOfExerciseSeconds,
                averageSessionLengthSeconds = averageSessionLengthSeconds,
                longestStreakDays = calculateLongestStreakUseCase.execute(sessions),
                currentStreakDays = calculateCurrentStreakUseCase.execute(sessions),
                averageNumberOfSessionsPerWeek = calculateAverageSessionsPerWeekUseCase.execute(sessions)
            )
        }
    }

}
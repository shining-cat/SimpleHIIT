package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.models.UserStatistics
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

class GetStatsForUserUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val calculateCurrentStreakUseCase: CalculateCurrentStreakUseCase,
    private val calculateLongestStreakUseCase: CalculateLongestStreakUseCase,
    private val calculateAverageSessionsPerWeekUseCase: CalculateAverageSessionsPerWeekUseCase,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User, now: Long): UserStatistics {
        val sessionsForUser = simpleHiitRepository.getSessionsForUser(user)
        return when (sessionsForUser) {
            is Output.Error -> {
                simpleHiitLogger.e(
                    "GetStatsForUserUseCase",
                    "failed getting sessions, returning default values",
                    sessionsForUser.exception
                )
                UserStatistics(userName = user.name)
            }
            is Output.Success -> {
                mapListOfSessionsToStatistics(
                    userName = user.name,
                    sessions = sessionsForUser.result,
                    now = now
                )
            }
        }
    }

    private fun mapListOfSessionsToStatistics(
        userName: String,
        sessions: List<Session>,
        now: Long
    ): UserStatistics {
        return if (sessions.isEmpty()) {
            UserStatistics(userName = userName)
        } else {
            val totalNumberOfSessions = sessions.size
            val cumulatedTimeOfExerciseSeconds = sessions.sumOf { it.durationSeconds }
            val averageSessionLengthSeconds =
                (cumulatedTimeOfExerciseSeconds.toDouble() / totalNumberOfSessions.toDouble()).toInt()
            val timestampsList = sessions.map { it.timeStamp }
            UserStatistics(
                userName = userName,
                totalNumberOfSessions = totalNumberOfSessions,
                cumulatedTimeOfExerciseSeconds = cumulatedTimeOfExerciseSeconds,
                averageSessionLengthSeconds = averageSessionLengthSeconds,
                longestStreakDays = calculateLongestStreakUseCase.execute(timestampsList, now),
                currentStreakDays = calculateCurrentStreakUseCase.execute(timestampsList, now),
                averageNumberOfSessionsPerWeek = calculateAverageSessionsPerWeekUseCase.execute(
                    timestampsList,
                    now
                )
            )
        }
    }

}
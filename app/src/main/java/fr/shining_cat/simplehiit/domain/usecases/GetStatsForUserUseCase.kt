package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.SessionRecord
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

    suspend fun execute(user: User, now: Long): Output<UserStatistics> {
        val sessionsForUserOutput = simpleHiitRepository.getSessionRecordsForUser(user)
        return when (sessionsForUserOutput) {
            is Output.Error -> {
                simpleHiitLogger.e(
                    "GetStatsForUserUseCase",
                    "failed getting sessions, returning default values",
                    sessionsForUserOutput.exception
                )
                sessionsForUserOutput
            }
            is Output.Success -> {
                Output.Success(
                    mapListOfSessionRecordsToStatistics(
                        user = user,
                        sessionRecords = sessionsForUserOutput.result,
                        now = now
                    )
                )
            }
        }
    }

    private fun mapListOfSessionRecordsToStatistics(
        user: User,
        sessionRecords: List<SessionRecord>,
        now: Long
    ): UserStatistics {
        return if (sessionRecords.isEmpty()) {
            UserStatistics(user = user)
        } else {
            val totalNumberOfSessionRecords = sessionRecords.size
            val cumulatedTimeOfExerciseMs = sessionRecords.sumOf { it.durationMs }
            val averageSessionLengthMs =
                (cumulatedTimeOfExerciseMs.toDouble() / totalNumberOfSessionRecords.toDouble()).toLong()
            val timestampsList = sessionRecords.map { it.timeStamp }
            UserStatistics(
                user = user,
                totalNumberOfSessions = totalNumberOfSessionRecords,
                cumulatedTimeOfExerciseMs = cumulatedTimeOfExerciseMs,
                averageSessionLengthMs = averageSessionLengthMs,
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
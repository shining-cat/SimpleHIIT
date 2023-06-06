package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.Output
import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.SessionRecord
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.models.UserStatistics
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.DefaultDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetStatsForUserUseCase @Inject constructor(
    private val simpleHiitRepository: SimpleHiitRepository,
    private val calculateCurrentStreakUseCase: CalculateCurrentStreakUseCase,
    private val calculateLongestStreakUseCase: CalculateLongestStreakUseCase,
    private val calculateAverageSessionsPerWeekUseCase: CalculateAverageSessionsPerWeekUseCase,
    @DefaultDispatcher private val defaultDispatcher: CoroutineDispatcher,
    private val simpleHiitLogger: HiitLogger
) {

    suspend fun execute(user: User, now: Long): Output<UserStatistics> {
        return withContext(defaultDispatcher) {
            val sessionsForUserOutput = simpleHiitRepository.getSessionRecordsForUser(user)
            when (sessionsForUserOutput) {
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
    }

    private suspend fun mapListOfSessionRecordsToStatistics(
        user: User,
        sessionRecords: List<SessionRecord>,
        now: Long
    ): UserStatistics {
        return withContext(defaultDispatcher) {
            if (sessionRecords.isEmpty()) {
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

}
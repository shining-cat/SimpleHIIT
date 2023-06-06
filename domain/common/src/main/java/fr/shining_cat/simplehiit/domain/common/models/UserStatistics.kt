package fr.shining_cat.simplehiit.domain.common.models

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport

@ExcludeFromJacocoGeneratedReport
data class UserStatistics(
    val user: User,
    val totalNumberOfSessions: Int = 0,
    val cumulatedTimeOfExerciseMs: Long = 0L,
    val averageSessionLengthMs: Long = 0,
    val longestStreakDays: Int = 0,
    val currentStreakDays: Int = 0,
    val averageNumberOfSessionsPerWeek: String = "0"
)

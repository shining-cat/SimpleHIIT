package fr.shining_cat.simplehiit.domain.models

data class UserStatistics(
    val userName:String,
    val totalNumberOfSessions: Int = 0,
    val cumulatedTimeOfExerciseSeconds: Long = 0L,
    val averageSessionLengthSeconds: Int = 0,
    val longestStreakDays: Int = 0,
    val currentStreakDays: Int = 0,
    val averageNumberOfSessionsPerWeek: Double = 0.00
)

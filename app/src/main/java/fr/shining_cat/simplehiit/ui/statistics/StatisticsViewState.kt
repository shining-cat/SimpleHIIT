package fr.shining_cat.simplehiit.ui.statistics

sealed class StatisticsViewState {
    object StatisticsLoading : StatisticsViewState()
    data class StatisticsNominal(
        val userName:String,
        val totalNumberOfSessions: Int,
        val cumulatedTimeOfExerciseSeconds: Long,
        val averageSessionLengthSeconds: Int,
        val longestStreakDays: Int,
        val currentStreakDays: Int,
        val averageNumberOfSessionsPerWeek: Double
    ) : StatisticsViewState()

    data class StatisticsError(val errorCode: String) : StatisticsViewState()
}

package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.domain.models.User

sealed class StatisticsViewState {
    object StatisticsLoading : StatisticsViewState()
    data class StatisticsNominal(
        val user:User,
        val totalNumberOfSessions: Int,
        val cumulatedTimeOfExerciseFormatted: String,
        val averageSessionLengthFormatted: String,
        val longestStreakDays: Int,
        val currentStreakDays: Int,
        val averageNumberOfSessionsPerWeek: String
    ) : StatisticsViewState()

    data class StatisticsError(val errorCode: String, val user: User) : StatisticsViewState()
    data class StatisticsFatalError(val errorCode: String) : StatisticsViewState()
    object StatisticsNoUsers : StatisticsViewState()
}
sealed class StatisticsDialog(){
    object None: StatisticsDialog()
    data class SelectUserDialog(val users: List<User>):StatisticsDialog()
    data class SettingsDialogConfirmDeleteAllSessionsForUser(val user:User) : StatisticsDialog()
    object StatisticsDialogConfirmWholeReset : StatisticsDialog()
}

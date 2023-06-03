package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.commondomain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.commondomain.models.User

sealed class StatisticsViewState {
    object Loading : StatisticsViewState()
    data class Nominal(
        val user: User,
        val statistics: List<DisplayedStatistic>
    ) : StatisticsViewState()
    data class NoSessions(val user: User) : StatisticsViewState()

    data class Error(val errorCode: String, val user: User) : StatisticsViewState()
    data class FatalError(val errorCode: String) : StatisticsViewState()
    object NoUsers : StatisticsViewState()
}
sealed class StatisticsDialog(){
    object None: StatisticsDialog()
    data class SelectUser(val users: List<User>):StatisticsDialog()
    data class ConfirmDeleteAllSessionsForUser(val user: User) : StatisticsDialog()
    object ConfirmWholeReset : StatisticsDialog()
}

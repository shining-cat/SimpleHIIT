package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.User

sealed interface StatisticsViewState {
    object Loading : StatisticsViewState

    data class Nominal(
        val allUsers: List<User>,
        val selectedUser: User,
        val selectedUserStatistics: List<DisplayedStatistic>,
    ) : StatisticsViewState

    data class NoSessions(
        val allUsers: List<User>,
        val selectedUser: User,
    ) : StatisticsViewState

    data class Error(
        val allUsers: List<User>,
        val selectedUser: User,
        val errorCode: String,
    ) : StatisticsViewState

    data class FatalError(
        val errorCode: String,
    ) : StatisticsViewState

    object NoUsers : StatisticsViewState
}

sealed interface StatisticsDialog {
    object None : StatisticsDialog

    data class ConfirmDeleteAllSessionsForUser(
        val user: User,
    ) : StatisticsDialog

    object ConfirmWholeReset : StatisticsDialog
}

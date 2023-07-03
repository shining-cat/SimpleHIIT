package fr.shining_cat.simplehiit.android.mobile.ui.statistics

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shining_cat.simplehiit.domain.common.models.User

@ExcludeFromJacocoGeneratedReport
sealed interface StatisticsViewState {
    object Loading : StatisticsViewState
    data class Nominal(
        val user: User,
        val statistics: List<DisplayedStatistic>,
        val showUsersSwitch: Boolean
    ) : StatisticsViewState

    data class NoSessions(
        val user: User,
        val showUsersSwitch: Boolean
    ) : StatisticsViewState

    data class Error(val errorCode: String, val user: User, val showUsersSwitch: Boolean) :
        StatisticsViewState

    data class FatalError(val errorCode: String) : StatisticsViewState
    object NoUsers : StatisticsViewState
}

@ExcludeFromJacocoGeneratedReport
sealed interface StatisticsDialog {
    object None : StatisticsDialog
    data class SelectUser(val users: List<User>) : StatisticsDialog
    data class ConfirmDeleteAllSessionsForUser(val user: User) : StatisticsDialog
    object ConfirmWholeReset : StatisticsDialog
}

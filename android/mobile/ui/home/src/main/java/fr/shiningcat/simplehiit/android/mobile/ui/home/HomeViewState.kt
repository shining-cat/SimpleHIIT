package fr.shiningcat.simplehiit.android.mobile.ui.home

import fr.shiningcat.simplehiit.domain.common.models.LaunchSessionWarning
import fr.shiningcat.simplehiit.domain.common.models.User

sealed interface HomeViewState {
    object Loading : HomeViewState

    data class Nominal(
        val numberCumulatedCycles: Int,
        val cycleLength: String,
        val users: List<User>,
        val totalSessionLengthFormatted: String,
        val warning: LaunchSessionWarning? = null,
    ) : HomeViewState

    data class MissingUsers(
        val numberCumulatedCycles: Int,
        val cycleLength: String,
        val totalSessionLengthFormatted: String,
    ) : HomeViewState

    data class Error(
        val errorCode: String,
    ) : HomeViewState
}

sealed interface HomeDialog {
    object None : HomeDialog

    object ConfirmWholeReset : HomeDialog
}

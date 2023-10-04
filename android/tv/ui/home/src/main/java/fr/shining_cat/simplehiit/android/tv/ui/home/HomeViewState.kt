package fr.shining_cat.simplehiit.android.tv.ui.home

import fr.shining_cat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shining_cat.simplehiit.domain.common.models.User

@ExcludeFromJacocoGeneratedReport
sealed interface HomeViewState {
    object Loading : HomeViewState
    data class Nominal(
        val numberCumulatedCycles: Int,
        val cycleLength: String,
        val users: List<User>,
        val totalSessionLengthFormatted: String
    ) : HomeViewState

    data class MissingUsers(
        val numberCumulatedCycles: Int,
        val cycleLength: String,
        val totalSessionLengthFormatted: String
    ) : HomeViewState

    data class Error(val errorCode: String) : HomeViewState
}

@ExcludeFromJacocoGeneratedReport
sealed interface HomeDialog {
    object None : HomeDialog
    object ConfirmWholeReset : HomeDialog
}

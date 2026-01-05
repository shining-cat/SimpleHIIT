package fr.shiningcat.simplehiit.android.shared.statistics

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsDialog
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsPresenter
import fr.shiningcat.simplehiit.sharedui.statistics.StatisticsViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Thin ViewModel wrapper for the Statistics screen.
 * Provides Android lifecycle-aware scope and exposes state to the UI.
 * All business logic is delegated to StatisticsPresenter.
 */
class StatisticsViewModel(
    private val statisticsPresenter: StatisticsPresenter,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val screenViewState: StateFlow<StatisticsViewState> =
        statisticsPresenter.screenViewState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = StatisticsViewState.Loading,
            )

    val dialogViewState: StateFlow<StatisticsDialog> =
        statisticsPresenter.dialogState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = StatisticsDialog.None,
            )

    init {
        // Observe users and load stats for first user when available
        viewModelScope.launch(context = mainDispatcher) {
            statisticsPresenter.observeUsers().collect { /* no-op, updates happen in presenter */ }
        }
    }

    fun retrieveStatsForUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.retrieveStatsForUser(user)
        }
    }

    fun openPickUser() {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.openPickUser()
        }
    }

    fun deleteAllSessionsForUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.deleteAllSessionsForUser(user)
        }
    }

    fun deleteAllSessionsForUserConfirmation(user: User) {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.deleteAllSessionsForUserConfirmation(user)
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.resetWholeApp()
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.resetWholeAppConfirmation()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(mainDispatcher) {
            statisticsPresenter.cancelDialog()
        }
    }
}

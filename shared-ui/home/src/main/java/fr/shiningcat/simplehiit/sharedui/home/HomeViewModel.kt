package fr.shiningcat.simplehiit.sharedui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class HomeViewModel(
    private val homeInteractor: HomeInteractor,
    private val homeViewStateMapper: HomeViewStateMapper,
    private val mainDispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) : ViewModel() {
    val screenViewState =
        homeInteractor
            .getHomeSettings()
            .map { homeViewStateMapper.map(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = HomeViewState.Loading,
            )

    private val _dialogViewState = MutableStateFlow<HomeDialog>(HomeDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(HomeDialog.None)
        }
    }

    fun modifyNumberCycles(modification: NumberCycleModification) {
        val change =
            when (modification) {
                NumberCycleModification.INCREASE -> +1
                NumberCycleModification.DECREASE -> -1
            }
        val currentViewState = screenViewState.value
        if (currentViewState is HomeViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentNumberCycles = currentViewState.numberCumulatedCycles
                if (currentNumberCycles + change > 0) {
                    homeInteractor.setTotalRepetitionsNumber(currentNumberCycles + change)
                } else {
                    logger.e(
                        "HomeViewModel",
                        "modifyNumberCycles::can't reach negative values",
                    )
                }
            }
        } else {
            logger.e(
                "HomeViewModel",
                "modifyNumberCycles::current state does not allow this now",
            )
        }
    }

    enum class NumberCycleModification { INCREASE, DECREASE }

    fun toggleSelectedUser(user: User) {
        logger.d(
            "HomeViewModel",
            "toggleSelectedUser::user:: was selected = ${user.selected}, toggling to ${!user.selected}",
        )
        viewModelScope.launch(context = mainDispatcher) {
            homeInteractor.toggleUserSelected(user.copy(selected = !user.selected))
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(HomeDialog.ConfirmWholeReset)
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch(context = mainDispatcher) {
            homeInteractor.resetWholeApp()
        }
    }
}

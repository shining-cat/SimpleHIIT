package fr.shiningcat.simplehiit.android.tv.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel
    @Inject
    constructor(
        private val homeInteractor: HomeInteractor,
        private val homeViewStateMapper: HomeViewStateMapper,
        @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
        private val hiitLogger: HiitLogger,
    ) : ViewModel() {
        // Reactive data stream - automatically starts/stops based on UI lifecycle
        val screenViewState =
            homeInteractor
                .getHomeSettings()
                .map { homeViewStateMapper.map(it) }
                .stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                    initialValue = HomeViewState.Loading,
                )

        // UI-driven state - managed manually for dialogs and one-time events
        private val _dialogViewState = MutableStateFlow<HomeDialog>(HomeDialog.None)
        val dialogViewState = _dialogViewState.asStateFlow()

        init {
            hiitLogger.d("HomeViewModel", "initialized with reactive state management")
        }

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
                        hiitLogger.e(
                            "HomeViewModel",
                            "modifyNumberCycles::can't reach negative values",
                        )
                    }
                }
            } else {
                hiitLogger.e(
                    "HomeViewModel",
                    "modifyNumberCycles::current state does not allow this now",
                )
            }
        }

        enum class NumberCycleModification { INCREASE, DECREASE }

        fun toggleSelectedUser(user: User) {
            hiitLogger.d(
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

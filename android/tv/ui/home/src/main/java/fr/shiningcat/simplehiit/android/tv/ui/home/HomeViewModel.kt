package fr.shiningcat.simplehiit.android.tv.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.di.MainDispatcher
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val homeInteractor: HomeInteractor,
    private val homeViewStateMapper: HomeViewStateMapper,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) : ViewModel() {

    private val _screenViewState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<HomeDialog>(HomeDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    private var isInitialized = false

    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            viewModelScope.launch(context = mainDispatcher) {
                homeInteractor.getHomeSettings().collect {
                    _screenViewState.emit(
                        homeViewStateMapper.map(it, durationStringFormatter)
                    )
                }
            }
            isInitialized = true
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(HomeDialog.None)
        }
    }

    fun modifyNumberCycles(modification: NumberCycleModification) {
        val change = when (modification) {
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
                        "modifyNumberCycles::can't reach negative values"
                    )
                }
            }
        } else {
            hiitLogger.e(
                "HomeViewModel",
                "modifyNumberCycles::current state does not allow this now"
            )
        }
    }

    enum class NumberCycleModification { INCREASE, DECREASE }

    fun toggleSelectedUser(user: User) {
        hiitLogger.d(
            "HomeViewModel",
            "toggleSelectedUser::user:: was selected = ${user.selected}, toggling to ${!user.selected}"
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
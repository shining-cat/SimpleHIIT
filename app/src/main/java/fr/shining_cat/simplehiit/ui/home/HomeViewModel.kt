package fr.shining_cat.simplehiit.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.*
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getHomeSettingsUseCase: GetHomeSettingsUseCase,
    private val setTotalRepetitionsNumberUseCase: SetTotalRepetitionsNumberUseCase,
    private val toggleUserSelectedUseCase: ToggleUserSelectedUseCase,
    private val resetWholeAppUseCase: ResetWholeAppUseCase,
    private val homeMapper: HomeMapper,
    private val hiitLogger: HiitLogger
) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState = MutableStateFlow<HomeViewState>(HomeViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<HomeDialog>(HomeDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    private var isInitialized = false

    fun init(durationStringFormatter: DurationStringFormatter) {
        if(!isInitialized) {
            viewModelScope.launch {
                getHomeSettingsUseCase.execute().collect() {
                    _screenViewState.emit(
                        homeMapper.map( it, durationStringFormatter)
                    )
                }
            }
            isInitialized = true
        }
    }

    fun cancelDialog() {
        logD("HomeViewModel", "cancelDialog")
        viewModelScope.launch {
            _dialogViewState.emit(HomeDialog.None)
        }
    }

    fun openInputNumberCyclesDialog(currentValue: Int) {
        viewModelScope.launch {
            _dialogViewState.emit(
                HomeDialog.InputNumberCycles(initialNumberOfCycles = currentValue)
            )
        }
    }

    fun validateInputNumberCycles(input: String):Constants.InputError{
        return if (input.length < 3 && input.toIntOrNull() is Int) Constants.InputError.NONE else Constants.InputError.WRONG_FORMAT
    }

    fun updateNumberCumulatedCycles(value: String) {
        if(validateInputNumberCycles(value) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setTotalRepetitionsNumberUseCase.execute(value.toInt())
                _dialogViewState.emit(HomeDialog.None)
            }
        } else{
            logD("HomeViewModel", "updateNumberCumulatedCycles:: invalid input, this should never happen")
        }
    }

    fun toggleSelectedUser(user: User) {
        logD("HomeViewModel","toggleSelectedUser::user:: was selected = ${user.selected}, toggling to ${!user.selected}")
        viewModelScope.launch {
            toggleUserSelectedUseCase.execute(user.copy(selected = !user.selected))
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch {
            _dialogViewState.emit(HomeDialog.ConfirmWholeReset)
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch {
            resetWholeAppUseCase.execute()
        }
    }

}
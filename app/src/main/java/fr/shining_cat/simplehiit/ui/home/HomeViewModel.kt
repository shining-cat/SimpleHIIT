package fr.shining_cat.simplehiit.ui.home

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.domain.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetTotalRepetitionsNumberUseCase
import fr.shining_cat.simplehiit.domain.usecases.UpdateUserUseCase
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
    private val updateUserUseCase: UpdateUserUseCase,
    private val resetWholeAppUseCase: ResetWholeAppUseCase,
    private val homeMapper: HomeMapper,
    private val hiitLogger: HiitLogger
) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState = MutableStateFlow<HomeViewState>(HomeViewState.HomeLoading)
    val screenViewState = _screenViewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<HomeDialog>(HomeDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    fun init(
        formatStringHoursMinutesSeconds: String,
        formatStringHoursMinutesNoSeconds: String,
        formatStringHoursNoMinutesNoSeconds: String,
        formatStringMinutesSeconds: String,
        formatStringMinutesNoSeconds: String,
        formatStringSeconds: String
    ) {
        viewModelScope.launch {
            getHomeSettingsUseCase.execute().collect() {
                _screenViewState.emit(
                    homeMapper.map(
                        it,
                        formatStringHoursMinutesSeconds,
                        formatStringHoursMinutesNoSeconds,
                        formatStringHoursNoMinutesNoSeconds,
                        formatStringMinutesSeconds,
                        formatStringMinutesNoSeconds,
                        formatStringSeconds
                    )
                )
            }
        }
    }

    fun cancelDialog() {
        logD("HomeViewModel", "cancelDialog")
        viewModelScope.launch {
            _dialogViewState.emit(HomeDialog.None)
        }
    }

    fun openInputNumberCyclesDialog(currentValue: Int) {
        logD("HomeViewModel", "openInputNumberCyclesDialog::currentValue = $currentValue")
        viewModelScope.launch {
            _dialogViewState.emit(
                HomeDialog.HomeDialogInputNumberCycles(initialNumberOfCycles = currentValue)
            )
        }
    }

    fun validateInputNumberCycles(input: String):Boolean{
        return input.toIntOrNull() is Int
    }

    fun updateNumberCumulatedCycles(value: String) {
        if(validateInputNumberCycles(value)) {
            viewModelScope.launch {
                setTotalRepetitionsNumberUseCase.execute(value.toInt())
                _dialogViewState.emit(HomeDialog.None)
            }
        } else{
            logD("HomeViewModel", "updateNumberCumulatedCycles:: invalid input, this should never happen")
        }
    }

    fun toggleSelectedUser(user: User) {
        viewModelScope.launch {
            updateUserUseCase.execute(user.copy(selected = !user.selected))
        }
    }

    fun resetWholeApp(errorCode: String) {
        viewModelScope.launch {
            _dialogViewState.emit(HomeDialog.HomeDialogConfirmWholeReset(errorCode))
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch {
            resetWholeAppUseCase.execute()
        }
    }

}
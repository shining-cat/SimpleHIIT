package fr.shining_cat.simplehiit.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.commondomain.Constants
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commondomain.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.SetTotalRepetitionsNumberUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.ToggleUserSelectedUseCase
import fr.shining_cat.simplehiit.commondomain.usecases.ValidateInputNumberCyclesUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.MainDispatcher
import kotlinx.coroutines.CoroutineDispatcher
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
    private val validateInputNumberCyclesUseCase: ValidateInputNumberCyclesUseCase,
    private val homeMapper: HomeMapper,
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
                getHomeSettingsUseCase.execute().collect {
                    _screenViewState.emit(
                        homeMapper.map(it, durationStringFormatter)
                    )
                }
            }
            isInitialized = true
        }
    }

    fun cancelDialog() {
        hiitLogger.d("HomeViewModel", "cancelDialog")
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(HomeDialog.None)
        }
    }

    fun openInputNumberCyclesDialog(currentValue: Int) {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(
                HomeDialog.InputNumberCycles(initialNumberOfCycles = currentValue)
            )
        }
    }

    fun validateInputNumberCycles(input: String): Constants.InputError {
        return validateInputNumberCyclesUseCase.execute(input)
    }

    fun updateNumberCumulatedCycles(value: String) {
        if (validateInputNumberCycles(value) == Constants.InputError.NONE) {
            viewModelScope.launch(context = mainDispatcher) {
                setTotalRepetitionsNumberUseCase.execute(value.toInt())
                _dialogViewState.emit(HomeDialog.None)
            }
        } else {
            hiitLogger.d(
                "HomeViewModel",
                "updateNumberCumulatedCycles:: invalid input, this should never happen"
            )
        }
    }

    fun toggleSelectedUser(user: User) {
        hiitLogger.d(
            "HomeViewModel",
            "toggleSelectedUser::user:: was selected = ${user.selected}, toggling to ${!user.selected}"
        )
        viewModelScope.launch(context = mainDispatcher) {
            toggleUserSelectedUseCase.execute(user.copy(selected = !user.selected))
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(HomeDialog.ConfirmWholeReset)
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch(context = mainDispatcher) {
            resetWholeAppUseCase.execute()
        }
    }

}
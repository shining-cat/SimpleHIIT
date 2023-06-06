package fr.shining_cat.simplehiit.android.mobile.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.MainDispatcher
import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.GetHomeSettingsUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.SetTotalRepetitionsNumberUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ToggleUserSelectedUseCase
import fr.shining_cat.simplehiit.domain.home.usecases.ValidateInputNumberCyclesUseCase
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
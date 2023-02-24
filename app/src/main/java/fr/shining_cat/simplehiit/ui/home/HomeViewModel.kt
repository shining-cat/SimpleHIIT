package fr.shining_cat.simplehiit.ui.home

import androidx.lifecycle.ViewModel
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
import kotlinx.coroutines.flow.collect
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

    private val _viewState = MutableStateFlow<HomeViewState>(HomeViewState.HomeLoading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getHomeSettingsUseCase.execute().collect(){
                _viewState.emit(homeMapper.map(it))
            }
        }
    }

    fun editNumberCumulatedCycles(value:Int){
        viewModelScope.launch {
            setTotalRepetitionsNumberUseCase.execute(value)
        }
    }

    fun toggleSelectedUser(user: User){
        viewModelScope.launch {
            updateUserUseCase.execute(user.copy(selected = !user.selected))
        }
    }

    fun resetWholeApp(errorCode: String){
        viewModelScope.launch {
            _viewState.emit(HomeViewState.HomeDialogConfirmWholeReset(errorCode))
        }
    }

    fun resetWholeAppConfirmationDeleteEverything(){
        viewModelScope.launch {
            resetWholeAppUseCase.execute()
        }
    }

}
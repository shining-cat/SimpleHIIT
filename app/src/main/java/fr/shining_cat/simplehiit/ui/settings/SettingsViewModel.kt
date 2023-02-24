package fr.shining_cat.simplehiit.ui.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.*
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val getGeneralSettingsUseCase: GetGeneralSettingsUseCase,
    private val setWorkPeriodLengthUseCase: SetWorkPeriodLengthUseCase,
    private val setRestPeriodLengthUseCase: SetRestPeriodLengthUseCase,
    private val setNumberOfWorkPeriodsUseCase: SetNumberOfWorkPeriodsUseCase,
    private val setBeepSoundUseCase: SetBeepSoundUseCase,
    private val setSessionStartCountDownUseCase: SetSessionStartCountDownUseCase,
    private val setPeriodStartCountDownUseCase: SetPeriodStartCountDownUseCase,
    private val updateUserUseCase: UpdateUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val setSelectedExerciseTypesUseCase: SetSelectedExerciseTypesUseCase,
    private val resetAllSettingsUseCase: ResetAllSettingsUseCase,
    private val mapper: SettingsMapper,
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {

    private val _viewState = MutableStateFlow<SettingsViewState>(SettingsViewState.SettingsLoading)
    val viewState = _viewState.asStateFlow()

    init {
        viewModelScope.launch {
            getGeneralSettingsUseCase.execute().collect(){
                _viewState.emit(mapper.map(it))
            }
        }
    }

    fun setWorkPeriodLength(durationMs: Long) {
        viewModelScope.launch {
            setWorkPeriodLengthUseCase.execute(durationMs)
        }
    }

    fun setNumberOfWorkPeriods(number: Int) {
        viewModelScope.launch {
            setNumberOfWorkPeriodsUseCase.execute(number)
        }
    }

    fun setBeepSound(active:Boolean) {
        viewModelScope.launch {
            setBeepSoundUseCase.execute(active)
        }
    }

    fun setSessionStartCountDown(durationMs: Long) {
        viewModelScope.launch {
            setSessionStartCountDownUseCase.execute(durationMs)
        }
    }

    fun setPeriodStartCountDown(durationMs: Long) {
        viewModelScope.launch {
            setPeriodStartCountDownUseCase.execute(durationMs)
        }
    }

     fun setRestPeriodLength(durationMs: Long) {
        viewModelScope.launch {
            setRestPeriodLengthUseCase.execute(durationMs)
        }
    }

    fun createUser(user: User){
        viewModelScope.launch {
            createUserUseCase.execute(user)
        }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            updateUserUseCase.execute(user)
        }
    }

    fun toggleSelectedExercise(exerciseTypeToggled: ExerciseTypeSelected){
        val currentViewState = viewState.value
        if(currentViewState is SettingsViewState.SettingsNominal) {
            val currentList: List<ExerciseTypeSelected> = currentViewState.exerciseTypes
            val toggledList: List<ExerciseTypeSelected> = currentList.map {
                if (it.type == exerciseTypeToggled.type) it.copy(selected = !it.selected)
                else it
            }
            viewModelScope.launch {
                setSelectedExerciseTypesUseCase.execute(toggledList)
            }
        } else{
            hiitLogger.e("SettingsViewModel", "toggleSelectedExercise::current state does not allow this now")
        }
    }

    fun resetAllSettings(){
        viewModelScope.launch {
            resetAllSettingsUseCase.execute()
        }
    }
}
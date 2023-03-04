package fr.shining_cat.simplehiit.ui.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.*
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
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
    private val deleteUserUseCase: DeleteUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val setSelectedExerciseTypesUseCase: SetSelectedExerciseTypesUseCase,
    private val resetAllSettingsUseCase: ResetAllSettingsUseCase,
    private val mapper: SettingsMapper,
    private val hiitLogger: HiitLogger

) : AbstractLoggerViewModel(hiitLogger) {

    private val _screenViewState =
        MutableStateFlow<SettingsViewState>(SettingsViewState.SettingsLoading)
    val screenViewState = _screenViewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<SettingsDialog>(SettingsDialog.None)
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
            getGeneralSettingsUseCase.execute().collect() {
                _screenViewState.emit(
                    mapper.map(
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

    fun editWorkPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.workPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.SettingsDialogEditWorkPeriodLength(currentValueAsSeconds)
                )
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editWorkPeriodLength::current state does not allow this now"
            )
        }
    }

    fun setWorkPeriodLength(inputSecondsAsString: String) {
        if (validatePeriodLength(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setWorkPeriodLengthUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logD(
                "SettingsViewModel",
                "setWorkPeriodLength:: invalid input, this should never happen"
            )
        }
    }

    fun validatePeriodLength(input: String): Constants.InputError {
        if ((input.toLongOrNull() is Long).not()) {
            return Constants.InputError.WRONG_FORMAT
        }
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            val periodLengthSeconds = input.toLong()
            val periodCountDownLengthSeconds = currentViewState.periodsStartCountDownLengthAsSeconds.toLong()
            if (periodLengthSeconds < periodCountDownLengthSeconds) {
                return Constants.InputError.VALUE_TOO_SMALL
            }
        }
        return Constants.InputError.NONE
    }

    fun editRestPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.restPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.SettingsDialogEditRestPeriodLength(currentValueAsSeconds)
                )
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editRestPeriodLength::current state does not allow this now"
            )
        }
    }

    fun setRestPeriodLength(inputSecondsAsString: String) {
        if (validatePeriodLength(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setRestPeriodLengthUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logD(
                "SettingsViewModel",
                "setRestPeriodLength:: invalid input, this should never happen"
            )
        }
    }

    fun editNumberOfWorkPeriods() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValue = currentViewState.numberOfWorkPeriods
                _dialogViewState.emit(SettingsDialog.SettingsDialogInputNumberCycles(currentValue))
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editRestPeriodLength::current state does not allow this now"
            )
        }
    }

    fun setNumberOfWorkPeriods(value: String) {
        if (validateNumberOfWorkPeriods(value) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setNumberOfWorkPeriodsUseCase.execute(value.toInt())
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logD(
                "SettingsViewModel",
                "setNumberOfWorkPeriods:: invalid input, this should never happen"
            )
        }
    }

    fun validateNumberOfWorkPeriods(input: String): Constants.InputError {
        return if ((input.toIntOrNull() is Int).not()) {
            Constants.InputError.WRONG_FORMAT
        } else Constants.InputError.NONE
    }

    fun toggleBeepSound() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                setBeepSoundUseCase.execute(!currentViewState.beepSoundCountDownActive)
            }
        } else {
            hiitLogger.e("SettingsViewModel", "setBeepSound::current state does not allow this now")
        }
    }

    fun editSessionStartCountDown() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.sessionStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.SettingsDialogEditSessionStartCountDown(currentValueAsSeconds)
                )
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editSessionStartCountDown::current state does not allow this now"
            )
        }
    }

    fun setSessionStartCountDown(inputSecondsAsString: String) {
        if (validateInputSessionStartCountdown(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setSessionStartCountDownUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logD(
                "SettingsViewModel",
                "setSessionStartCountDown:: invalid input, this should never happen"
            )
        }
    }

    fun validateInputSessionStartCountdown(input: String): Constants.InputError {
        return if ((input.toLongOrNull() is Long).not()) {
            Constants.InputError.WRONG_FORMAT
        } else Constants.InputError.NONE
    }

    fun editPeriodStartCountDown() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.periodsStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.SettingsDialogEditPeriodStartCountDown(currentValueAsSeconds)
                )
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editPeriodStartCountDown::current state does not allow this now"
            )
        }
    }

    fun setPeriodStartCountDown(inputSecondsAsString: String) {
        if (validateInputPeriodStartCountdown(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch {
                setPeriodStartCountDownUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logD(
                "SettingsViewModel",
                "setPeriodStartCountDown:: invalid input, this should never happen"
            )
        }
    }

    fun validateInputPeriodStartCountdown(input: String): Constants.InputError {
        if ((input.toLongOrNull() is Long).not()) {
            return Constants.InputError.WRONG_FORMAT
        }
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            val workPeriodLengthSeconds = currentViewState.workPeriodLengthAsSeconds.toLong()
            val restPeriodLengthSeconds = currentViewState.restPeriodLengthAsSeconds.toLong()
            val periodCountDownLengthSeconds = input.toLong()
            if (workPeriodLengthSeconds < periodCountDownLengthSeconds || restPeriodLengthSeconds < periodCountDownLengthSeconds) {
                return Constants.InputError.VALUE_TOO_BIG
            }
        }
        return Constants.InputError.NONE
    }

    fun addUser() {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogAddUser)
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogEditUser(user))
        }
    }

    fun saveUser(user: User) {
        if (user.id == 0L) createUser(user) else updateUser(user)
    }

    private fun createUser(user: User) {
        viewModelScope.launch {
            val result = createUserUseCase.execute(user)
            when(result){
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e("SettingsViewModel", "createUser::error happened:${result.errorCode}", result.exception)
                    _dialogViewState.emit(SettingsDialog.InputCausedError(result.errorCode.code))
                }
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            val result = updateUserUseCase.execute(user)
            when(result){
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e("SettingsViewModel", "updateUser::error happened:${result.errorCode}", result.exception)
                    _dialogViewState.emit(SettingsDialog.InputCausedError(result.errorCode.code))
                }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogConfirmDeleteUser(user))
        }
    }

    fun deleteUserConfirmation(user: User) {
        viewModelScope.launch {
            val result = deleteUserUseCase.execute(user)
            when(result){
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e("SettingsViewModel", "deleteUserConfirmation::error happened:${result.errorCode}", result.exception)
                    _dialogViewState.emit(SettingsDialog.InputCausedError(result.errorCode.code))
                }
            }
        }
    }

    fun toggleSelectedExercise(exerciseTypeToggled: ExerciseTypeSelected) {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            val currentList: List<ExerciseTypeSelected> = currentViewState.exerciseTypes
            val toggledList: List<ExerciseTypeSelected> = currentList.map {
                if (it.type == exerciseTypeToggled.type) it.copy(selected = !it.selected)
                else it
            }
            viewModelScope.launch {
                setSelectedExerciseTypesUseCase.execute(toggledList)
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "toggleSelectedExercise::current state does not allow this now"
            )
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogConfirmResetAllSettings)
        }
    }

    fun resetAllSettingsConfirmation() {
        viewModelScope.launch {
            resetAllSettingsUseCase.execute()
            _dialogViewState.emit(SettingsDialog.None)
        }
    }

    fun validateInputString(input: String): Constants.InputError {
        return if(input.length < 25) Constants.InputError.NONE else Constants.InputError.TOO_LONG
    }

    fun cancelDialog() {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.None)
        }
    }
}
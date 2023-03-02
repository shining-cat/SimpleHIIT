package fr.shining_cat.simplehiit.ui.settings

import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.*
import fr.shining_cat.simplehiit.ui.AbstractLoggerViewModel
import fr.shining_cat.simplehiit.ui.home.HomeDialog
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
                _screenViewState.emit(mapper.map(
                    it,
                    formatStringHoursMinutesSeconds,
                    formatStringHoursMinutesNoSeconds,
                    formatStringHoursNoMinutesNoSeconds,
                    formatStringMinutesSeconds,
                    formatStringMinutesNoSeconds,
                    formatStringSeconds
                ))
            }
        }
    }
    fun editWorkPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.workPeriodLengthAsSeconds
                _dialogViewState.emit(SettingsDialog.SettingsDialogEditWorkPeriodLength(currentValueAsSeconds))
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editWorkPeriodLength::current state does not allow this now"
            )
        }
    }

    fun setWorkPeriodLength(inputSecondsAsString: String) {
        if (validateInputLong(inputSecondsAsString)) {
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
    fun editRestPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.restPeriodLengthAsSeconds
                _dialogViewState.emit(SettingsDialog.SettingsDialogEditRestPeriodLength(currentValueAsSeconds))
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editRestPeriodLength::current state does not allow this now"
            )
        }
    }

    fun setRestPeriodLength(inputSecondsAsString: String) {
        if (validateInputLong(inputSecondsAsString)) {
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
        if (validateInputInt(value)) {
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
                _dialogViewState.emit(SettingsDialog.SettingsDialogEditSessionStartCountDown(currentValueAsSeconds))
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editSessionStartCountDown::current state does not allow this now"
            )
        }
    }

    fun setSessionStartCountDown(inputSecondsAsString: String) {
        if (validateInputLong(inputSecondsAsString)) {
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

    fun editPeriodStartCountDown() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.SettingsNominal) {
            viewModelScope.launch {
                val currentValueAsSeconds = currentViewState.periodsStartCountDownLengthAsSeconds
                _dialogViewState.emit(SettingsDialog.SettingsDialogEditPeriodStartCountDown(currentValueAsSeconds))
            }
        } else {
            hiitLogger.e(
                "SettingsViewModel",
                "editPeriodStartCountDown::current state does not allow this now"
            )
        }
    }

    fun setPeriodStartCountDown(inputSecondsAsString: String) {
        if (validateInputLong(inputSecondsAsString)) {
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

    fun editUser(user: User) {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogEditUser(user))
        }
    }

    fun addUser() {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogAddUser)
        }
    }

    fun saveUser(user: User) {
        if (user.id == 0L) createUser(user) else updateUser(user)
    }

    private fun createUser(user: User) {
        viewModelScope.launch {
            createUserUseCase.execute(user)
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch {
            updateUserUseCase.execute(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.SettingsDialogConfirmDeleteUser(user))
        }
    }

    fun deleteUserConfirmation(user: User) {
        viewModelScope.launch {
            deleteUserUseCase.execute(user)
        }
    }

    fun validateInputInt(input: String): Boolean {
        return (input.length < 5 && input.toIntOrNull() is Int)
    }

    fun validateInputLong(input: String): Boolean {
        return (input.length < 6 && input.toLongOrNull() is Long)
    }

    fun validateInputString(input: String): Boolean {
        return (input.length < 24)
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
        }
    }

    fun cancelDialog() {
        viewModelScope.launch {
            _dialogViewState.emit(SettingsDialog.None)
        }
    }
}
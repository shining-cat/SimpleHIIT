package fr.shining_cat.simplehiit.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.di.MainDispatcher
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.usecases.CreateUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.DeleteUserUseCase
import fr.shining_cat.simplehiit.domain.usecases.GetGeneralSettingsUseCase
import fr.shining_cat.simplehiit.domain.usecases.ResetAllSettingsUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetBeepSoundUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetNumberOfWorkPeriodsUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetPeriodStartCountDownUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetRestPeriodLengthUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetSelectedExerciseTypesUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetSessionStartCountDownUseCase
import fr.shining_cat.simplehiit.domain.usecases.SetWorkPeriodLengthUseCase
import fr.shining_cat.simplehiit.domain.usecases.UpdateUserNameUseCase
import fr.shining_cat.simplehiit.domain.usecases.ValidateInputPeriodStartCountdownUseCase
import fr.shining_cat.simplehiit.domain.usecases.ValidateInputSessionStartCountdownUseCase
import fr.shining_cat.simplehiit.domain.usecases.ValidateInputUserNameUseCase
import fr.shining_cat.simplehiit.domain.usecases.ValidateNumberOfWorkPeriodsUseCase
import fr.shining_cat.simplehiit.domain.usecases.ValidatePeriodLengthUseCase
import fr.shining_cat.simplehiit.utils.HiitLogger
import kotlinx.coroutines.CoroutineDispatcher
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
    private val updateUserNameUseCase: UpdateUserNameUseCase,
    private val deleteUserUseCase: DeleteUserUseCase,
    private val createUserUseCase: CreateUserUseCase,
    private val setSelectedExerciseTypesUseCase: SetSelectedExerciseTypesUseCase,
    private val resetAllSettingsUseCase: ResetAllSettingsUseCase,
    private val validatePeriodLengthUseCase: ValidatePeriodLengthUseCase,
    private val validateNumberOfWorkPeriodsUseCase: ValidateNumberOfWorkPeriodsUseCase,
    private val validateInputSessionStartCountdownUseCase: ValidateInputSessionStartCountdownUseCase,
    private val validateInputPeriodStartCountdownUseCase: ValidateInputPeriodStartCountdownUseCase,
    private val validateInputUserNameUseCase: ValidateInputUserNameUseCase,
    private val mapper: SettingsMapper,
    @MainDispatcher private val mainDispatcher: CoroutineDispatcher,
    private val hiitLogger: HiitLogger
) : ViewModel() {

    private val _screenViewState =
        MutableStateFlow<SettingsViewState>(SettingsViewState.Loading)
    val screenViewState = _screenViewState.asStateFlow()

    private val _dialogViewState = MutableStateFlow<SettingsDialog>(SettingsDialog.None)
    val dialogViewState = _dialogViewState.asStateFlow()

    private var isInitialized = false

    fun init(durationStringFormatter: DurationStringFormatter) {
        if (!isInitialized) {
            viewModelScope.launch(context = mainDispatcher) {
                getGeneralSettingsUseCase.execute().collect() {
                    _screenViewState.emit(
                        mapper.map(it, durationStringFormatter)
                    )
                }
            }
            isInitialized = true
        }
    }

    fun editWorkPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentValueAsSeconds = currentViewState.workPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditWorkPeriodLength(currentValueAsSeconds)
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
        if (validatePeriodLengthInput(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch(context = mainDispatcher) {
                setWorkPeriodLengthUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            hiitLogger.d(
                "SettingsViewModel",
                "setWorkPeriodLength:: invalid input, this should never happen"
            )
        }
    }

    fun validatePeriodLengthInput(input: String): Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validatePeriodLengthUseCase.execute(
                input,
                currentViewState.periodsStartCountDownLengthAsSeconds.toLong())
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    fun editRestPeriodLength() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentValueAsSeconds = currentViewState.restPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditRestPeriodLength(currentValueAsSeconds)
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
        if (validatePeriodLengthInput(inputSecondsAsString) == Constants.InputError.NONE) {
            viewModelScope.launch(context = mainDispatcher) {
                setRestPeriodLengthUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            hiitLogger.d(
                "SettingsViewModel",
                "setRestPeriodLength:: invalid input, this should never happen"
            )
        }
    }

    fun editNumberOfWorkPeriods() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentValue = currentViewState.numberOfWorkPeriods
                _dialogViewState.emit(SettingsDialog.EditNumberCycles(currentValue))
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
            viewModelScope.launch(context = mainDispatcher) {
                setNumberOfWorkPeriodsUseCase.execute(value.toInt())
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            hiitLogger.d(
                "SettingsViewModel",
                "setNumberOfWorkPeriods:: invalid input, this should never happen"
            )
        }
    }

    fun validateNumberOfWorkPeriods(input: String): Constants.InputError {
        return validateNumberOfWorkPeriodsUseCase.execute(input)
    }

    fun toggleBeepSound() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                setBeepSoundUseCase.execute(!currentViewState.beepSoundCountDownActive)
            }
        } else {
            hiitLogger.e("SettingsViewModel", "setBeepSound::current state does not allow this now")
        }
    }

    fun editSessionStartCountDown() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentValueAsSeconds = currentViewState.sessionStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditSessionStartCountDown(currentValueAsSeconds)
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
            viewModelScope.launch(context = mainDispatcher) {
                setSessionStartCountDownUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            hiitLogger.d(
                "SettingsViewModel",
                "setSessionStartCountDown:: invalid input, this should never happen"
            )
        }
    }

    fun validateInputSessionStartCountdown(input: String): Constants.InputError {
        return validateInputSessionStartCountdownUseCase.execute(input)
    }

    fun editPeriodStartCountDown() {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            viewModelScope.launch(context = mainDispatcher) {
                val currentValueAsSeconds = currentViewState.periodsStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditPeriodStartCountDown(currentValueAsSeconds)
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
            viewModelScope.launch(context = mainDispatcher) {
                setPeriodStartCountDownUseCase.execute(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            hiitLogger.d(
                "SettingsViewModel",
                "setPeriodStartCountDown:: invalid input, this should never happen"
            )
        }
    }

    fun validateInputPeriodStartCountdown(input: String): Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validateInputPeriodStartCountdownUseCase.execute(
                input = input,
                workPeriodLengthSeconds = currentViewState.workPeriodLengthAsSeconds.toLong(),
                restPeriodLengthSeconds = currentViewState.restPeriodLengthAsSeconds.toLong()
            )
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    fun addUser(userName: String = "") {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.AddUser(userName = userName))
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.EditUser(user))
        }
    }

    fun saveUser(user: User) {
        if (user.id == 0L) createUser(user) else updateUser(user)
    }

    private fun createUser(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            val result = createUserUseCase.execute(user)
            when (result) {
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e(
                        "SettingsViewModel",
                        "createUser::error happened:${result.errorCode}",
                        result.exception
                    )
                    _dialogViewState.emit(SettingsDialog.Error(errorCode = result.errorCode.code))
                }
            }
        }
    }

    private fun updateUser(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            val result = updateUserNameUseCase.execute(user)
            when (result) {
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e(
                        "SettingsViewModel",
                        "updateUser::error happened:${result.errorCode}",
                        result.exception
                    )
                    _dialogViewState.emit(SettingsDialog.Error(errorCode = result.errorCode.code))
                }
            }
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.ConfirmDeleteUser(user))
        }
    }

    fun deleteUserConfirmation(user: User) {
        viewModelScope.launch(context = mainDispatcher) {
            val result = deleteUserUseCase.execute(user)
            when (result) {
                is Output.Success -> _dialogViewState.emit(SettingsDialog.None)
                is Output.Error -> {
                    hiitLogger.e(
                        "SettingsViewModel",
                        "deleteUserConfirmation::error happened:${result.errorCode}",
                        result.exception
                    )
                    _dialogViewState.emit(SettingsDialog.Error(result.errorCode.code))
                }
            }
        }
    }

    fun toggleSelectedExercise(exerciseTypeToggled: ExerciseTypeSelected) {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            //todo: extract to usecase :
            val currentList: List<ExerciseTypeSelected> = currentViewState.exerciseTypes
            val toggledList: List<ExerciseTypeSelected> = currentList.map {
                if (it.type == exerciseTypeToggled.type) it.copy(selected = !it.selected)
                else it
            }
            viewModelScope.launch(context = mainDispatcher) {
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
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.ConfirmResetAllSettings)
        }
    }

    fun resetAllSettingsConfirmation() {
        viewModelScope.launch(context = mainDispatcher) {
            resetAllSettingsUseCase.execute()
            _dialogViewState.emit(SettingsDialog.None)
        }
    }

    fun validateInputUserNameString(user: User): Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validateInputUserNameUseCase.execute(
                user = user,
                existingUsers = currentViewState.users
            )
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.None)
        }
    }
}
package fr.shining_cat.simplehiit.android.mobile.ui.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.common.usecases.CreateUserUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.DeleteUserUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.GetGeneralSettingsUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ResetAllSettingsUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SaveSelectedExerciseTypesUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetBeepSoundUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetNumberOfWorkPeriodsUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetPeriodStartCountDownUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetRestPeriodLengthUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetSessionStartCountDownUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.SetWorkPeriodLengthUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ToggleExerciseTypeInListUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.UpdateUserNameUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ValidateInputPeriodStartCountdownUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ValidateInputSessionStartCountdownUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ValidateInputUserNameUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ValidateNumberOfWorkPeriodsUseCase
import fr.shining_cat.simplehiit.domain.common.usecases.ValidatePeriodLengthUseCase
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.commonutils.di.MainDispatcher
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
    private val saveSelectedExerciseTypesUseCase: SaveSelectedExerciseTypesUseCase,
    private val resetAllSettingsUseCase: ResetAllSettingsUseCase,
    private val validatePeriodLengthUseCase: ValidatePeriodLengthUseCase,
    private val validateNumberOfWorkPeriodsUseCase: ValidateNumberOfWorkPeriodsUseCase,
    private val validateInputSessionStartCountdownUseCase: ValidateInputSessionStartCountdownUseCase,
    private val validateInputPeriodStartCountdownUseCase: ValidateInputPeriodStartCountdownUseCase,
    private val validateInputUserNameUseCase: ValidateInputUserNameUseCase,
    private val toggleExerciseTypeInListUseCase: ToggleExerciseTypeInListUseCase,
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
        if (validatePeriodLengthInput(inputSecondsAsString) == fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) {
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

    fun validatePeriodLengthInput(input: String): fr.shining_cat.simplehiit.domain.common.Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validatePeriodLengthUseCase.execute(
                input,
                currentViewState.periodsStartCountDownLengthAsSeconds.toLong()
            )
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE
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
        if (validatePeriodLengthInput(inputSecondsAsString) == fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) {
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
        if (validateNumberOfWorkPeriods(value) == fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) {
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

    fun validateNumberOfWorkPeriods(input: String): fr.shining_cat.simplehiit.domain.common.Constants.InputError {
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
        if (validateInputSessionStartCountdown(inputSecondsAsString) == fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) {
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

    fun validateInputSessionStartCountdown(input: String): fr.shining_cat.simplehiit.domain.common.Constants.InputError {
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
        if (validateInputPeriodStartCountdown(inputSecondsAsString) == fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) {
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

    fun validateInputPeriodStartCountdown(input: String): fr.shining_cat.simplehiit.domain.common.Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validateInputPeriodStartCountdownUseCase.execute(
                input = input,
                workPeriodLengthSeconds = currentViewState.workPeriodLengthAsSeconds.toLong(),
                restPeriodLengthSeconds = currentViewState.restPeriodLengthAsSeconds.toLong()
            )
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE
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
                is fr.shining_cat.simplehiit.domain.common.Output.Success -> _dialogViewState.emit(
                    SettingsDialog.None
                )

                is fr.shining_cat.simplehiit.domain.common.Output.Error -> {
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
                is fr.shining_cat.simplehiit.domain.common.Output.Success -> _dialogViewState.emit(
                    SettingsDialog.None
                )

                is fr.shining_cat.simplehiit.domain.common.Output.Error -> {
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
                is fr.shining_cat.simplehiit.domain.common.Output.Success -> _dialogViewState.emit(
                    SettingsDialog.None
                )

                is fr.shining_cat.simplehiit.domain.common.Output.Error -> {
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
            val toggledList = toggleExerciseTypeInListUseCase.execute(
                currentList = currentViewState.exerciseTypes,
                exerciseTypeToToggle = exerciseTypeToggled
            )
            viewModelScope.launch(context = mainDispatcher) {
                saveSelectedExerciseTypesUseCase.execute(toggledList)
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

    fun validateInputUserNameString(user: User): fr.shining_cat.simplehiit.domain.common.Constants.InputError {
        val currentViewState = screenViewState.value
        if (currentViewState is SettingsViewState.Nominal) {
            return validateInputUserNameUseCase.execute(
                user = user,
                existingUsers = currentViewState.users
            )
        }
        //we don't really expect to be able to land in here if current state is not Nominal
        return fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE
    }

    fun cancelDialog() {
        viewModelScope.launch(context = mainDispatcher) {
            _dialogViewState.emit(SettingsDialog.None)
        }
    }
}
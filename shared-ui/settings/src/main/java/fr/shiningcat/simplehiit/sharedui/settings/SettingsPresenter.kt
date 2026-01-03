package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Pure Kotlin presenter for Settings feature.
 * Contains all business logic, dialog state management, and validation coordination.
 * No Android framework dependencies - fully testable with unit tests.
 */
class SettingsPresenter(
    private val settingsInteractor: SettingsInteractor,
    private val mapper: SettingsViewStateMapper,
    private val dispatcher: CoroutineDispatcher,
    private val logger: HiitLogger,
) {
    private val presenterScope = CoroutineScope(SupervisorJob() + dispatcher)

    private val screenViewStateInternal: StateFlow<SettingsViewState> =
        settingsInteractor
            .getGeneralSettings()
            .map { mapper.map(it) }
            .stateIn(
                scope = presenterScope,
                started = SharingStarted.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = SettingsViewState.Loading,
            )

    private val _dialogViewState = MutableStateFlow<SettingsDialog>(SettingsDialog.None)
    private val _restartTrigger = MutableSharedFlow<Unit>(replay = 0)

    val screenViewState: Flow<SettingsViewState> = screenViewStateInternal
    val dialogViewState: StateFlow<SettingsDialog> = _dialogViewState.asStateFlow()
    val restartTrigger: Flow<Unit> = _restartTrigger.asSharedFlow()

    // Work period operations
    fun editWorkPeriodLength() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                val currentValueAsSeconds = currentViewState.workPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditWorkPeriodLength(currentValueAsSeconds),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editWorkPeriodLength::current state does not allow this now",
            )
        }
    }

    fun setWorkPeriodLength(inputSecondsAsString: String) {
        if (validatePeriodLengthInput(inputSecondsAsString) == Constants.InputError.NONE) {
            presenterScope.launch {
                settingsInteractor.setWorkPeriodLength(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logger.d(
                "SettingsPresenter",
                "setWorkPeriodLength:: invalid input, this should never happen",
            )
        }
    }

    fun validatePeriodLengthInput(input: String): Constants.InputError {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            return settingsInteractor.validatePeriodLength(
                input,
                currentViewState.periodsStartCountDownLengthAsSeconds.toLong(),
            )
        }
        // we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    // Rest period operations
    fun editRestPeriodLength() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                val currentValueAsSeconds = currentViewState.restPeriodLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditRestPeriodLength(currentValueAsSeconds),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editRestPeriodLength::current state does not allow this now",
            )
        }
    }

    fun setRestPeriodLength(inputSecondsAsString: String) {
        if (validatePeriodLengthInput(inputSecondsAsString) == Constants.InputError.NONE) {
            presenterScope.launch {
                settingsInteractor.setRestPeriodLength(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logger.d(
                "SettingsPresenter",
                "setRestPeriodLength:: invalid input, this should never happen",
            )
        }
    }

    // Number of work periods
    fun editNumberOfWorkPeriods() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                val currentValue = currentViewState.numberOfWorkPeriods
                _dialogViewState.emit(SettingsDialog.EditNumberCycles(currentValue))
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editNumberOfWorkPeriods::current state does not allow this now",
            )
        }
    }

    fun setNumberOfWorkPeriods(value: String) {
        if (validateNumberOfWorkPeriods(value) == Constants.InputError.NONE) {
            presenterScope.launch {
                settingsInteractor.setNumberOfWorkPeriods(value.toInt())
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logger.d(
                "SettingsPresenter",
                "setNumberOfWorkPeriods:: invalid input, this should never happen",
            )
        }
    }

    fun validateNumberOfWorkPeriods(input: String): Constants.InputError = settingsInteractor.validateNumberOfWorkPeriods(input)

    // Beep sound
    fun toggleBeepSound() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                settingsInteractor.setBeepSound(!currentViewState.beepSoundCountDownActive)
            }
        } else {
            logger.e("SettingsPresenter", "toggleBeepSound::current state does not allow this now")
        }
    }

    // Session countdown
    fun editSessionStartCountDown() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                val currentValueAsSeconds = currentViewState.sessionStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditSessionStartCountDown(currentValueAsSeconds),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editSessionStartCountDown::current state does not allow this now",
            )
        }
    }

    fun setSessionStartCountDown(inputSecondsAsString: String) {
        if (validateInputSessionStartCountdown(inputSecondsAsString) == Constants.InputError.NONE) {
            presenterScope.launch {
                settingsInteractor.setSessionStartCountDown(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logger.d(
                "SettingsPresenter",
                "setSessionStartCountDown:: invalid input, this should never happen",
            )
        }
    }

    fun validateInputSessionStartCountdown(input: String): Constants.InputError =
        settingsInteractor.validateInputSessionStartCountdown(input)

    // Period countdown
    fun editPeriodStartCountDown() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                val currentValueAsSeconds = currentViewState.periodsStartCountDownLengthAsSeconds
                _dialogViewState.emit(
                    SettingsDialog.EditPeriodStartCountDown(currentValueAsSeconds),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editPeriodStartCountDown::current state does not allow this now",
            )
        }
    }

    fun setPeriodStartCountDown(inputSecondsAsString: String) {
        if (validateInputPeriodStartCountdown(inputSecondsAsString) == Constants.InputError.NONE) {
            presenterScope.launch {
                settingsInteractor.setPeriodStartCountDown(inputSecondsAsString.toLong() * 1000L)
                _dialogViewState.emit(SettingsDialog.None)
            }
        } else {
            logger.d(
                "SettingsPresenter",
                "setPeriodStartCountDown:: invalid input, this should never happen",
            )
        }
    }

    fun validateInputPeriodStartCountdown(input: String): Constants.InputError {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            return settingsInteractor.validateInputPeriodStartCountdown(
                input = input,
                workPeriodLengthSeconds = currentViewState.workPeriodLengthAsSeconds.toLong(),
                restPeriodLengthSeconds = currentViewState.restPeriodLengthAsSeconds.toLong(),
            )
        }
        // we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    // User management
    fun addUser(userName: String = "") {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.AddUser(userName = userName))
        }
    }

    fun editUser(user: User) {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.EditUser(user))
        }
    }

    fun saveUser(user: User) {
        if (user.id == 0L) createUser(user) else updateUser(user)
    }

    private fun createUser(user: User) {
        presenterScope.launch {
            val result = settingsInteractor.createUser(user)
            when (result) {
                is Output.Success -> {
                    _dialogViewState.emit(SettingsDialog.None)
                }
                is Output.Error -> {
                    logger.e(
                        "SettingsPresenter",
                        "createUser::error happened:${result.errorCode}",
                        result.exception,
                    )
                    _dialogViewState.emit(SettingsDialog.Error(errorCode = result.errorCode.code))
                }
            }
        }
    }

    private fun updateUser(user: User) {
        presenterScope.launch {
            val result = settingsInteractor.updateUserName(user)
            when (result) {
                is Output.Success -> {
                    _dialogViewState.emit(SettingsDialog.None)
                }
                is Output.Error -> {
                    logger.e(
                        "SettingsPresenter",
                        "updateUser::error happened:${result.errorCode}",
                        result.exception,
                    )
                    _dialogViewState.emit(SettingsDialog.Error(errorCode = result.errorCode.code))
                }
            }
        }
    }

    fun deleteUser(user: User) {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.ConfirmDeleteUser(user))
        }
    }

    fun deleteUserConfirmation(user: User) {
        presenterScope.launch {
            val result = settingsInteractor.deleteUser(user)
            when (result) {
                is Output.Success -> {
                    _dialogViewState.emit(SettingsDialog.None)
                }
                is Output.Error -> {
                    logger.e(
                        "SettingsPresenter",
                        "deleteUserConfirmation::error happened:${result.errorCode}",
                        result.exception,
                    )
                    _dialogViewState.emit(SettingsDialog.Error(result.errorCode.code))
                }
            }
        }
    }

    fun validateInputUserNameString(user: User): Constants.InputError {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            return settingsInteractor.validateInputUserName(
                user = user,
                existingUsers = currentViewState.users,
            )
        }
        // we don't really expect to be able to land in here if current state is not Nominal
        return Constants.InputError.NONE
    }

    // Exercise types
    fun toggleSelectedExercise(exerciseTypeToggled: ExerciseTypeSelected) {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            val toggledList =
                settingsInteractor.toggleExerciseTypeInList(
                    currentList = currentViewState.exerciseTypes,
                    exerciseTypeToToggle = exerciseTypeToggled,
                )
            presenterScope.launch {
                settingsInteractor.saveSelectedExerciseTypes(toggledList)
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "toggleSelectedExercise::current state does not allow this now",
            )
        }
    }

    // App settings
    fun editLanguage() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                _dialogViewState.emit(
                    SettingsDialog.PickLanguage(currentViewState.currentLanguage),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editLanguage::current state does not allow this now",
            )
        }
    }

    fun setLanguage(language: AppLanguage) {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.None)
            settingsInteractor.setAppLanguage(language)
        }
    }

    fun editTheme() {
        val currentViewState = screenViewStateInternal.value
        if (currentViewState is SettingsViewState.Nominal) {
            presenterScope.launch {
                _dialogViewState.emit(
                    SettingsDialog.PickTheme(currentViewState.currentTheme),
                )
            }
        } else {
            logger.e(
                "SettingsPresenter",
                "editTheme::current state does not allow this now",
            )
        }
    }

    fun setTheme(theme: AppTheme) {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.None)
            settingsInteractor.setAppTheme(theme)
            _restartTrigger.emit(Unit)
        }
    }

    // Reset
    fun resetAllSettings() {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.ConfirmResetAllSettings)
        }
    }

    fun resetAllSettingsConfirmation() {
        presenterScope.launch {
            settingsInteractor.resetAllSettings()
            _dialogViewState.emit(SettingsDialog.None)
        }
    }

    // Dialog control
    fun cancelDialog() {
        presenterScope.launch {
            _dialogViewState.emit(SettingsDialog.None)
        }
    }
}

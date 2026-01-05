package fr.shiningcat.simplehiit.android.shared.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.settings.SettingsDialog
import fr.shiningcat.simplehiit.sharedui.settings.SettingsPresenter
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Thin Android ViewModel wrapper for Settings feature.
 * Delegates all business logic to SettingsPresenter (pure Kotlin).
 * Responsibilities:
 * - Manage Android lifecycle (extends ViewModel)
 * - Provide viewModelScope for coroutine launching
 * - Expose StateFlows to UI
 */
class SettingsViewModel(
    private val presenter: SettingsPresenter,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val screenViewState: StateFlow<SettingsViewState> =
        presenter.screenViewState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = SettingsViewState.Loading,
            )

    val dialogViewState: StateFlow<SettingsDialog> = presenter.dialogViewState

    val restartTrigger: Flow<Unit> = presenter.restartTrigger

    fun editWorkPeriodLength() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editWorkPeriodLength()
        }
    }

    fun setWorkPeriodLength(inputSecondsAsString: String) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setWorkPeriodLength(inputSecondsAsString)
        }
    }

    fun validatePeriodLengthInput(input: String) = presenter.validatePeriodLengthInput(input)

    fun editRestPeriodLength() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editRestPeriodLength()
        }
    }

    fun setRestPeriodLength(inputSecondsAsString: String) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setRestPeriodLength(inputSecondsAsString)
        }
    }

    fun editNumberOfWorkPeriods() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editNumberOfWorkPeriods()
        }
    }

    fun setNumberOfWorkPeriods(value: String) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setNumberOfWorkPeriods(value)
        }
    }

    fun validateNumberOfWorkPeriods(input: String) = presenter.validateNumberOfWorkPeriods(input)

    fun toggleBeepSound() {
        viewModelScope.launch(mainDispatcher) {
            presenter.toggleBeepSound()
        }
    }

    fun editSessionStartCountDown() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editSessionStartCountDown()
        }
    }

    fun setSessionStartCountDown(inputSecondsAsString: String) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setSessionStartCountDown(inputSecondsAsString)
        }
    }

    fun validateInputSessionStartCountdown(input: String) = presenter.validateInputSessionStartCountdown(input)

    fun editPeriodStartCountDown() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editPeriodStartCountDown()
        }
    }

    fun setPeriodStartCountDown(inputSecondsAsString: String) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setPeriodStartCountDown(inputSecondsAsString)
        }
    }

    fun validateInputPeriodStartCountdown(input: String) = presenter.validateInputPeriodStartCountdown(input)

    fun addUser(userName: String = "") {
        viewModelScope.launch(mainDispatcher) {
            presenter.addUser(userName)
        }
    }

    fun editUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            presenter.editUser(user)
        }
    }

    fun saveUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            presenter.saveUser(user)
        }
    }

    fun deleteUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            presenter.deleteUser(user)
        }
    }

    fun deleteUserConfirmation(user: User) {
        viewModelScope.launch(mainDispatcher) {
            presenter.deleteUserConfirmation(user)
        }
    }

    fun validateInputUserNameString(user: User) = presenter.validateInputUserNameString(user)

    fun toggleSelectedExercise(exerciseTypeToggled: ExerciseTypeSelected) {
        viewModelScope.launch(mainDispatcher) {
            presenter.toggleSelectedExercise(exerciseTypeToggled)
        }
    }

    fun editLanguage() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editLanguage()
        }
    }

    fun setLanguage(language: AppLanguage) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setLanguage(language)
        }
    }

    fun editTheme() {
        viewModelScope.launch(mainDispatcher) {
            presenter.editTheme()
        }
    }

    fun setTheme(theme: AppTheme) {
        viewModelScope.launch(mainDispatcher) {
            presenter.setTheme(theme)
        }
    }

    fun resetAllSettings() {
        viewModelScope.launch(mainDispatcher) {
            presenter.resetAllSettings()
        }
    }

    fun resetAllSettingsConfirmation() {
        viewModelScope.launch(mainDispatcher) {
            presenter.resetAllSettingsConfirmation()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(mainDispatcher) {
            presenter.cancelDialog()
        }
    }
}

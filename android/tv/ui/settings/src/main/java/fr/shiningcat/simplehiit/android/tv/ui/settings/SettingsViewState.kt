package fr.shiningcat.simplehiit.android.tv.ui.settings

import fr.shiningcat.simplehiit.commonutils.ExcludeFromJacocoGeneratedReport
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User

@ExcludeFromJacocoGeneratedReport
sealed interface SettingsViewState {
    object Loading : SettingsViewState
    data class Nominal(
        val workPeriodLengthAsSeconds: String,
        val restPeriodLengthAsSeconds: String,
        val numberOfWorkPeriods: String,
        val totalCycleLength: String,
        val beepSoundCountDownActive: Boolean,
        val sessionStartCountDownLengthAsSeconds: String,
        val periodsStartCountDownLengthAsSeconds: String,
        val users: List<User>,
        val exerciseTypes: List<ExerciseTypeSelected>
    ) : SettingsViewState

    data class Error(val errorCode: String) : SettingsViewState
}

@ExcludeFromJacocoGeneratedReport
sealed interface SettingsDialog {
    object None : SettingsDialog
    data class EditWorkPeriodLength(val valueSeconds: String) : SettingsDialog
    data class EditRestPeriodLength(val valueSeconds: String) : SettingsDialog
    data class EditNumberCycles(val numberOfCycles: String) : SettingsDialog
    data class EditSessionStartCountDown(val valueSeconds: String) : SettingsDialog
    data class EditPeriodStartCountDown(val valueSeconds: String) : SettingsDialog
    data class AddUser(val userName: String) : SettingsDialog
    data class EditUser(val user: User) : SettingsDialog
    data class ConfirmDeleteUser(val user: User) : SettingsDialog
    object ConfirmResetAllSettings : SettingsDialog
    data class Error(val errorCode: String) : SettingsDialog
}
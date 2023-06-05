package fr.shining_cat.simplehiit.android.mobile.ui.settings

import fr.shining_cat.simplehiit.commondomain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.commondomain.models.User

sealed class SettingsViewState {
    object Loading : SettingsViewState()
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
    ) : SettingsViewState()

    data class Error(val errorCode: String) : SettingsViewState()
}

sealed class SettingsDialog {
    object None : SettingsDialog()
    data class EditWorkPeriodLength(val valueSeconds: String) : SettingsDialog()
    data class EditRestPeriodLength(val valueSeconds: String) : SettingsDialog()
    data class EditNumberCycles(val numberOfCycles: String) : SettingsDialog()
    data class EditSessionStartCountDown(val valueSeconds: String) : SettingsDialog()
    data class EditPeriodStartCountDown(val valueSeconds: String) : SettingsDialog()
    data class AddUser(val userName: String) : SettingsDialog()
    data class EditUser(val user: User) : SettingsDialog()
    data class ConfirmDeleteUser(val user: User) : SettingsDialog()
    object ConfirmResetAllSettings : SettingsDialog()
    data class Error(val errorCode: String) : SettingsDialog()
}
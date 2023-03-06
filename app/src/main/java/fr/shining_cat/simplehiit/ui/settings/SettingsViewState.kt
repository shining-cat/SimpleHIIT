package fr.shining_cat.simplehiit.ui.settings

import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User

sealed class SettingsViewState {
    object SettingsLoading : SettingsViewState()
    data class SettingsNominal(
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

    data class SettingsError(val errorCode: String) : SettingsViewState()
}

sealed class SettingsDialog {
    object None : SettingsDialog()
    data class SettingsDialogEditWorkPeriodLength(val valueSeconds: String) : SettingsDialog()
    data class SettingsDialogEditRestPeriodLength(val valueSeconds: String) : SettingsDialog()
    data class SettingsDialogEditNumberCycles(val numberOfCycles: String) : SettingsDialog()
    data class SettingsDialogEditSessionStartCountDown(val valueSeconds: String) : SettingsDialog()
    data class SettingsDialogEditPeriodStartCountDown(val valueSeconds: String) : SettingsDialog()
    data class SettingsDialogAddUser(val userName: String) : SettingsDialog()
    data class SettingsDialogEditUser(val user: User) : SettingsDialog()
    data class SettingsDialogConfirmDeleteUser(val user: User) : SettingsDialog()
    object SettingsDialogConfirmResetAllSettings : SettingsDialog()
    data class SettingsDialogError(val errorCode: String) : SettingsDialog()
}
package fr.shining_cat.simplehiit.android.mobile.ui.settings.contents

import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.ErrorDialog
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.SettingsDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.SettingsViewState
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsAddUserDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditNumberCyclesDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodLengthDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodStartCountDownDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditSessionStartCountDownDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditUserDialog
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun SettingsContentHolder(
    editWorkPeriodLength: () -> Unit,
    saveWorkPeriodLength: (String) -> Unit,
    editRestPeriodLength: () -> Unit,
    saveRestPeriodLength: (String) -> Unit,
    validatePeriodLengthInput: (String) -> Constants.InputError,
    editNumberOfWorkPeriods: () -> Unit,
    validateNumberOfWorkPeriodsInput: (String) -> Constants.InputError,
    saveNumberOfWorkPeriod: (String) -> Unit,
    toggleBeepSound: () -> Unit,
    editSessionStartCountDown: () -> Unit,
    validateSessionCountDownLengthInput: (String) -> Constants.InputError,
    saveSessionStartCountDown: (String) -> Unit,
    editPeriodStartCountDown: () -> Unit,
    validatePeriodCountDownLengthInput: (String) -> Constants.InputError,
    savePeriodStartCountDown: (String) -> Unit,
    editUser: (User) -> Unit,
    addUser: () -> Unit,
    saveUser: (User) -> Unit,
    deleteUser: (User) -> Unit,
    deleteUserCancel: (User) -> Unit,
    deleteUserConfirm: (User) -> Unit,
    toggleExerciseType: (ExerciseTypeSelected) -> Unit,
    validateInputNameString: (User) -> Constants.InputError,
    resetSettings: () -> Unit,
    resetSettingsConfirmation: () -> Unit,
    cancelDialog: () -> Unit,
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog
) {
    when (screenViewState) {
        SettingsViewState.Loading -> BasicLoading()

        is SettingsViewState.Error -> SettingsErrorContent(
            errorCode = screenViewState.errorCode,
            resetSettings = resetSettings
        )

        is SettingsViewState.Nominal -> SettingsNominalContent(
            editWorkPeriodLength = editWorkPeriodLength,
            editRestPeriodLength = editRestPeriodLength,
            editNumberOfWorkPeriods = editNumberOfWorkPeriods,
            toggleBeepSound = toggleBeepSound,
            editSessionStartCountDown = editSessionStartCountDown,
            editPeriodStartCountDown = editPeriodStartCountDown,
            editUser = editUser,
            addUser = addUser,
            toggleExerciseType = toggleExerciseType,
            resetSettings = resetSettings,
            viewState = screenViewState
        )
    }
    when (dialogViewState) {
        SettingsDialog.None -> {/*do nothing*/
        }

        is SettingsDialog.EditWorkPeriodLength -> SettingsEditPeriodLengthDialog(
            dialogTitle = stringResource(id = R.string.work_period_length_label),
            savePeriodLength = saveWorkPeriodLength,
            validatePeriodLengthInput = validatePeriodLengthInput,
            periodLengthSeconds = dialogViewState.valueSeconds,
            onCancel = cancelDialog,
        )

        is SettingsDialog.EditRestPeriodLength -> SettingsEditPeriodLengthDialog(
            dialogTitle = stringResource(id = R.string.rest_period_length_label),
            savePeriodLength = saveRestPeriodLength,
            validatePeriodLengthInput = validatePeriodLengthInput,
            periodLengthSeconds = dialogViewState.valueSeconds,
            onCancel = cancelDialog,
        )

        is SettingsDialog.EditNumberCycles -> SettingsEditNumberCyclesDialog(
            saveNumber = saveNumberOfWorkPeriod,
            validateNumberCyclesInput = validateNumberOfWorkPeriodsInput,
            numberOfCycles = dialogViewState.numberOfCycles,
            onCancel = cancelDialog
        )

        is SettingsDialog.EditSessionStartCountDown -> SettingsEditSessionStartCountDownDialog(
            saveCountDownLength = saveSessionStartCountDown,
            validateCountDownLengthInput = validateSessionCountDownLengthInput,
            countDownLengthSeconds = dialogViewState.valueSeconds,
            onCancel = cancelDialog
        )

        is SettingsDialog.EditPeriodStartCountDown -> SettingsEditPeriodStartCountDownDialog(
            saveCountDownLength = savePeriodStartCountDown,
            validateCountDownLengthInput = validatePeriodCountDownLengthInput,
            countDownLengthSeconds = dialogViewState.valueSeconds,
            onCancel = cancelDialog
        )

        is SettingsDialog.AddUser -> SettingsAddUserDialog(
            saveUserName = { saveUser(User(name = it)) },
            userName = dialogViewState.userName,
            validateUserNameInput = { validateInputNameString(User(name = it)) },
            onCancel = cancelDialog
        )

        is SettingsDialog.EditUser -> SettingsEditUserDialog(
            saveUserName = { saveUser(dialogViewState.user.copy(name = it)) },
            deleteUser = { deleteUser(dialogViewState.user) },
            validateUserNameInput = { validateInputNameString(dialogViewState.user.copy(name = it)) },
            userName = dialogViewState.user.name,
            onCancel = cancelDialog
        )

        is SettingsDialog.ConfirmDeleteUser -> WarningDialog(
            message = stringResource(id = R.string.delete_confirmation_button_label),
            proceedButtonLabel = stringResource(id = R.string.delete_button_label),
            proceedAction = { deleteUserConfirm(dialogViewState.user) },
            dismissAction = { deleteUserCancel(dialogViewState.user) } //coming back to the edit user dialog instead of closing simply the dialog
        )

        SettingsDialog.ConfirmResetAllSettings -> WarningDialog(
            message = stringResource(id = R.string.reset_settings_confirmation_button_label),
            proceedButtonLabel = stringResource(id = R.string.reset_button_label),
            proceedAction = resetSettingsConfirmation,
            dismissAction = cancelDialog
        )

        is SettingsDialog.Error -> ErrorDialog(
            errorMessage = "",
            errorCode = dialogViewState.errorCode,
            dismissButtonLabel = stringResource(id = R.string.close_button_content_label),
            dismissAction = cancelDialog
        )
    }
}
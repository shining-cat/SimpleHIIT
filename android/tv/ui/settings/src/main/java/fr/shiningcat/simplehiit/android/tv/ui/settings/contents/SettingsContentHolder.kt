package fr.shiningcat.simplehiit.android.tv.ui.settings.contents

import android.content.res.Configuration
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ErrorDialog
import fr.shiningcat.simplehiit.android.tv.ui.common.components.WarningDialog
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsViewState
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsAddUserDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsEditNumberCyclesDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsEditPeriodLengthDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsEditPeriodStartCountDownDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsEditSessionStartCountDownDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs.SettingsEditUserDialog
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun SettingsContentHolder(
    editWorkPeriodLength: () -> Unit = {},
    saveWorkPeriodLength: (String) -> Unit = {},
    editRestPeriodLength: () -> Unit = {},
    saveRestPeriodLength: (String) -> Unit = {},
    validatePeriodLengthInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    editNumberOfWorkPeriods: () -> Unit = {},
    validateNumberOfWorkPeriodsInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    saveNumberOfWorkPeriod: (String) -> Unit = {},
    toggleBeepSound: () -> Unit = {},
    editSessionStartCountDown: () -> Unit = {},
    validateSessionCountDownLengthInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    saveSessionStartCountDown: (String) -> Unit = {},
    editPeriodStartCountDown: () -> Unit = {},
    validatePeriodCountDownLengthInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    savePeriodStartCountDown: (String) -> Unit = {},
    editUser: (User) -> Unit = {},
    addUser: () -> Unit = {},
    saveUser: (User) -> Unit = {},
    deleteUser: (User) -> Unit = {},
    deleteUserCancel: (User) -> Unit = {},
    deleteUserConfirm: (User) -> Unit = {},
    toggleExerciseType: (ExerciseTypeSelected) -> Unit = {},
    validateInputNameString: (User) -> Constants.InputError = { Constants.InputError.NONE },
    resetSettings: () -> Unit = {},
    resetSettingsConfirmation: () -> Unit = {},
    cancelDialog: () -> Unit = {},
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog,
    hiitLogger: HiitLogger? = null
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
            viewState = screenViewState,
            hiitLogger = hiitLogger
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

// Previews
@OptIn(ExperimentalTvMaterial3Api::class)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsContentHolderPreviewPhonePortrait(
    @PreviewParameter(SettingsContentHolderPreviewParameterProvider::class) viewState: SettingsViewState
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsContentHolder(
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None
            )
        }
    }
}

internal class SettingsContentHolderPreviewParameterProvider :
    PreviewParameterProvider<SettingsViewState> {

    private val exerciseTypeSelectedAllTrue = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = true
        )
    }
    private val exerciseTypeSelectedAllFalse = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = false
        )
    }
    private val exerciseTypeSelectedMixed = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = (ExerciseType.values().indexOf(it) % 2 == 0)
        )
    }

    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser = listOf(
        User(name = "user 1"),
        User(name = "user 2"),
        User(name = "user 3"),
        User(name = "user 4"),
        User(name = "user 5")
    )

    override val values: Sequence<SettingsViewState>
        get() = sequenceOf(
            SettingsViewState.Loading,
            SettingsViewState.Error(
                errorCode = "this is an error code"
            ),
            SettingsViewState.Nominal(
                workPeriodLengthAsSeconds = "15",
                restPeriodLengthAsSeconds = "5",
                numberOfWorkPeriods = "4",
                totalCycleLength = "3mn 20s",
                beepSoundCountDownActive = true,
                sessionStartCountDownLengthAsSeconds = "20",
                periodsStartCountDownLengthAsSeconds = "5",
                users = emptyList(),
                exerciseTypes = exerciseTypeSelectedAllTrue
            ),
            SettingsViewState.Nominal(
                workPeriodLengthAsSeconds = "15",
                restPeriodLengthAsSeconds = "5",
                numberOfWorkPeriods = "4",
                totalCycleLength = "3mn 20s",
                beepSoundCountDownActive = false,
                sessionStartCountDownLengthAsSeconds = "20",
                periodsStartCountDownLengthAsSeconds = "5",
                users = listOfOneUser,
                exerciseTypes = exerciseTypeSelectedAllTrue
            ),
            SettingsViewState.Nominal(
                workPeriodLengthAsSeconds = "15",
                restPeriodLengthAsSeconds = "5",
                numberOfWorkPeriods = "4",
                totalCycleLength = "3mn 20s",
                beepSoundCountDownActive = true,
                sessionStartCountDownLengthAsSeconds = "20",
                periodsStartCountDownLengthAsSeconds = "5",
                users = listOfTwoUser,
                exerciseTypes = exerciseTypeSelectedAllFalse
            ),
            SettingsViewState.Nominal(
                workPeriodLengthAsSeconds = "15",
                restPeriodLengthAsSeconds = "5",
                numberOfWorkPeriods = "4",
                totalCycleLength = "3mn 20s",
                beepSoundCountDownActive = false,
                sessionStartCountDownLengthAsSeconds = "20",
                periodsStartCountDownLengthAsSeconds = "5",
                users = listOfMoreUser,
                exerciseTypes = exerciseTypeSelectedMixed
            ),
        )
}

package fr.shiningcat.simplehiit.android.mobile.ui.settings.contents

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.BasicLoading
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.ErrorDialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.WarningDialog
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.settings.SettingsDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.SettingsViewState
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsAddUserDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditNumberCyclesDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodLengthDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodStartCountDownDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditSessionStartCountDownDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditUserDialog
import fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs.SettingsPickLanguageDialog
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User

@Composable
fun SettingsContentHolder(
    modifier: Modifier = Modifier,
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
    editLanguage: () -> Unit = {},
    saveLanguage: (AppLanguage) -> Unit = {},
    resetSettings: () -> Unit = {},
    resetSettingsConfirmation: () -> Unit = {},
    cancelDialog: () -> Unit = {},
    uiArrangement: UiArrangement,
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog,
) {
    Box(modifier = modifier) {
        when (screenViewState) {
            SettingsViewState.Loading -> BasicLoading(modifier = Modifier.fillMaxSize())

            is SettingsViewState.Error ->
                SettingsErrorContent(
                    modifier = Modifier.fillMaxSize(),
                    errorCode = screenViewState.errorCode,
                    resetSettings = resetSettings,
                )

            is SettingsViewState.Nominal ->
                SettingsNominalContent(
                    modifier = Modifier.fillMaxSize(),
                    editWorkPeriodLength = editWorkPeriodLength,
                    editRestPeriodLength = editRestPeriodLength,
                    editNumberOfWorkPeriods = editNumberOfWorkPeriods,
                    toggleBeepSound = toggleBeepSound,
                    editSessionStartCountDown = editSessionStartCountDown,
                    editPeriodStartCountDown = editPeriodStartCountDown,
                    editUser = editUser,
                    addUser = addUser,
                    toggleExerciseType = toggleExerciseType,
                    editLanguage = editLanguage,
                    resetSettings = resetSettings,
                    viewState = screenViewState,
                    uiArrangement = uiArrangement,
                )
        }
    }

    when (dialogViewState) {
        SettingsDialog.None -> { // do nothing
        }

        is SettingsDialog.EditWorkPeriodLength ->
            SettingsEditPeriodLengthDialog(
                dialogTitle = stringResource(id = R.string.work_period_length_label),
                savePeriodLength = saveWorkPeriodLength,
                validatePeriodLengthInput = validatePeriodLengthInput,
                periodLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )

        is SettingsDialog.EditRestPeriodLength ->
            SettingsEditPeriodLengthDialog(
                dialogTitle = stringResource(id = R.string.rest_period_length_label),
                savePeriodLength = saveRestPeriodLength,
                validatePeriodLengthInput = validatePeriodLengthInput,
                periodLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )

        is SettingsDialog.EditNumberCycles ->
            SettingsEditNumberCyclesDialog(
                saveNumber = saveNumberOfWorkPeriod,
                validateNumberCyclesInput = validateNumberOfWorkPeriodsInput,
                numberOfCycles = dialogViewState.numberOfCycles,
                onCancel = cancelDialog,
            )

        is SettingsDialog.EditSessionStartCountDown ->
            SettingsEditSessionStartCountDownDialog(
                saveCountDownLength = saveSessionStartCountDown,
                validateCountDownLengthInput = validateSessionCountDownLengthInput,
                countDownLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )

        is SettingsDialog.EditPeriodStartCountDown ->
            SettingsEditPeriodStartCountDownDialog(
                saveCountDownLength = savePeriodStartCountDown,
                validateCountDownLengthInput = validatePeriodCountDownLengthInput,
                countDownLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )

        is SettingsDialog.AddUser ->
            SettingsAddUserDialog(
                saveUserName = { saveUser(User(name = it)) },
                userName = dialogViewState.userName,
                validateUserNameInput = { validateInputNameString(User(name = it)) },
                onCancel = cancelDialog,
            )

        is SettingsDialog.EditUser ->
            SettingsEditUserDialog(
                saveUserName = { saveUser(dialogViewState.user.copy(name = it)) },
                deleteUser = { deleteUser(dialogViewState.user) },
                validateUserNameInput = { validateInputNameString(dialogViewState.user.copy(name = it)) },
                userName = dialogViewState.user.name,
                onCancel = cancelDialog,
            )

        is SettingsDialog.ConfirmDeleteUser ->
            WarningDialog(
                message = stringResource(id = R.string.delete_confirmation_button_label),
                proceedButtonLabel = stringResource(id = R.string.delete_button_label),
                proceedAction = { deleteUserConfirm(dialogViewState.user) },
                dismissAction = { deleteUserCancel(dialogViewState.user) },
            )

        is SettingsDialog.PickLanguage ->
            SettingsPickLanguageDialog(
                currentLanguage = dialogViewState.currentLanguage,
                onLanguageSelected = saveLanguage,
                onCancel = cancelDialog,
            )

        SettingsDialog.ConfirmResetAllSettings ->
            WarningDialog(
                message = stringResource(id = R.string.reset_settings_confirmation_button_label),
                proceedButtonLabel = stringResource(id = R.string.reset_button_label),
                proceedAction = resetSettingsConfirmation,
                dismissAction = cancelDialog,
            )

        is SettingsDialog.Error ->
            ErrorDialog(
                // Explicitly using common ErrorDialog
                errorMessage = "", // Consider passing a real message if available
                errorCode = dialogViewState.errorCode,
                dismissButtonLabel = stringResource(id = R.string.close_button_content_label),
                dismissAction = cancelDialog,
            )
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SettingsContentHolderPreviewPhoneLandscape(
    @PreviewParameter(SettingsContentHolderPreviewParameterProvider::class) viewState: SettingsViewState,
) {
    SimpleHiitMobileTheme {
        Surface {
            SettingsContentHolder(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None,
            )
        }
    }
}

internal class SettingsContentHolderPreviewParameterProvider : PreviewParameterProvider<SettingsViewState> {
    private val exerciseTypeSelectedAllTrue =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = true,
            )
        }
    private val exerciseTypeSelectedAllFalse =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = false,
            )
        }
    private val exerciseTypeSelectedMixed =
        ExerciseType.entries.map {
            ExerciseTypeSelected(
                type = it,
                selected = (ExerciseType.entries.indexOf(it) % 2 == 0),
            )
        }

    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser =
        listOf(
            User(name = "user 1"),
            User(name = "user 2"),
            User(name = "user 3"),
            User(name = "user 4"),
            User(name = "user 5"),
        )

    override val values: Sequence<SettingsViewState>
        get() =
            sequenceOf(
                SettingsViewState.Loading,
                SettingsViewState.Error(
                    errorCode = "this is an error code",
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
                    exerciseTypes = exerciseTypeSelectedAllTrue,
                    currentLanguage = AppLanguage.SYSTEM_DEFAULT,
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
                    exerciseTypes = exerciseTypeSelectedAllTrue,
                    currentLanguage = AppLanguage.ENGLISH,
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
                    exerciseTypes = exerciseTypeSelectedAllFalse,
                    currentLanguage = AppLanguage.FRENCH,
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
                    exerciseTypes = exerciseTypeSelectedMixed,
                    currentLanguage = AppLanguage.SWEDISH,
                ),
            )
}

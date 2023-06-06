package fr.shining_cat.simplehiit.android.mobile.ui.settings

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.android.mobile.common.components.ErrorDialog
import fr.shining_cat.simplehiit.android.mobile.common.components.WarningDialog
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.android.mobile.ui.settings.contents.SettingsErrorContent
import fr.shining_cat.simplehiit.android.mobile.ui.settings.contents.SettingsNominalContent
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsAddUserDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditNumberCyclesDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodLengthDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditPeriodStartCountDownDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditSessionStartCountDownDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs.SettingsEditUserDialog
import fr.shining_cat.simplehiit.commonutils.HiitLogger

@Composable
fun SettingsScreen(
    navController: NavController,
    hiitLogger: HiitLogger,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    hiitLogger.d("SettingsScreen", "INIT")
    val durationsFormatter = DurationStringFormatter(
        hoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        hoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        hoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        minutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        minutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        seconds = stringResource(id = R.string.seconds_short)
    )
    viewModel.init(durationsFormatter)
    val screenViewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    SettingsScreen(
        onNavigateUp = { navController.navigateUp() },
        editWorkPeriodLength = { viewModel.editWorkPeriodLength() },
        saveWorkPeriodLength = { viewModel.setWorkPeriodLength(it) },
        editRestPeriodLength = { viewModel.editRestPeriodLength() },
        saveRestPeriodLength = { viewModel.setRestPeriodLength(it) },
        validatePeriodLengthInput = { viewModel.validatePeriodLengthInput(it) },
        editNumberOfWorkPeriod = { viewModel.editNumberOfWorkPeriods() },
        validateNumberOfWorkPeriodsInput = { viewModel.validateNumberOfWorkPeriods(it) },
        saveNumberOfWorkPeriod = { viewModel.setNumberOfWorkPeriods(it) },
        toggleBeepSound = { viewModel.toggleBeepSound() },
        editSessionStartCountDown = { viewModel.editSessionStartCountDown() },
        validateSessionCountDownLengthInput = { viewModel.validateInputSessionStartCountdown(it) },
        saveSessionStartCountDown = { viewModel.setSessionStartCountDown(it) },
        editPeriodStartCountDown = { viewModel.editPeriodStartCountDown() },
        validatePeriodCountDownLengthInput = { viewModel.validateInputPeriodStartCountdown(it) },
        savePeriodStartCountDown = { viewModel.setPeriodStartCountDown(it) },
        editUser = { viewModel.editUser(it) },
        addUser = { viewModel.addUser() },
        saveUser = { viewModel.saveUser(it) },
        deleteUser = { viewModel.deleteUser(it) },
        deleteUserCancel = { viewModel.editUser(it) },
        deleteUserConfirm = { viewModel.deleteUserConfirmation(it) },
        toggleExerciseType = { viewModel.toggleSelectedExercise(it) },
        validateInputNameString = { viewModel.validateInputUserNameString(it) },
        resetSettings = { viewModel.resetAllSettings() },
        resetSettingsConfirmation = { viewModel.resetAllSettingsConfirmation() },
        cancelDialog = { viewModel.cancelDialog() },
        screenViewState = screenViewState,
        dialogViewState = dialogViewState
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SettingsScreen(
    onNavigateUp: () -> Boolean = { false },
    editWorkPeriodLength: () -> Unit = {},
    saveWorkPeriodLength: (String) -> Unit = {},
    editRestPeriodLength: () -> Unit = {},
    saveRestPeriodLength: (String) -> Unit = {},
    validatePeriodLengthInput: (String) -> Constants.InputError,
    editNumberOfWorkPeriod: () -> Unit = {},
    validateNumberOfWorkPeriodsInput: (String) -> Constants.InputError,
    saveNumberOfWorkPeriod: (String) -> Unit = {},
    toggleBeepSound: () -> Unit = {},
    editSessionStartCountDown: () -> Unit = {},
    validateSessionCountDownLengthInput: (String) -> Constants.InputError,
    saveSessionStartCountDown: (String) -> Unit = {},
    editPeriodStartCountDown: () -> Unit = {},
    validatePeriodCountDownLengthInput: (String) -> Constants.InputError,
    savePeriodStartCountDown: (String) -> Unit = {},
    editUser: (User) -> Unit = {},
    addUser: () -> Unit = {},
    saveUser: (User) -> Unit = {},
    deleteUser: (User) -> Unit = {},
    deleteUserCancel: (User) -> Unit,
    deleteUserConfirm: (User) -> Unit = {},
    toggleExerciseType: (ExerciseTypeSelected) -> Unit = {},
    validateInputNameString: (User) -> Constants.InputError,
    resetSettings: () -> Unit = {},
    resetSettingsConfirmation: () -> Unit = {},
    cancelDialog: () -> Unit = {},
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog
) {
    Scaffold(
        topBar = {
            SettingsTopBar(onNavigateUp = onNavigateUp)
        },
        content = { paddingValues ->
            SettingsContent(
                innerPadding = paddingValues,
                editWorkPeriodLength = editWorkPeriodLength,
                saveWorkPeriodLength = saveWorkPeriodLength,
                editRestPeriodLength = editRestPeriodLength,
                saveRestPeriodLength = saveRestPeriodLength,
                validatePeriodLengthInput = validatePeriodLengthInput,
                editNumberOfWorkPeriods = editNumberOfWorkPeriod,
                validateNumberOfWorkPeriodsInput = validateNumberOfWorkPeriodsInput,
                saveNumberOfWorkPeriod = saveNumberOfWorkPeriod,
                toggleBeepSound = toggleBeepSound,
                editSessionStartCountDown = editSessionStartCountDown,
                validateSessionCountDownLengthInput = validateSessionCountDownLengthInput,
                saveSessionStartCountDown = saveSessionStartCountDown,
                editPeriodStartCountDown = editPeriodStartCountDown,
                validatePeriodCountDownLengthInput = validatePeriodCountDownLengthInput,
                savePeriodStartCountDown = savePeriodStartCountDown,
                editUser = editUser,
                addUser = addUser,
                saveUser = saveUser,
                deleteUser = deleteUser,
                deleteUserCancel = deleteUserCancel,
                deleteUserConfirm = deleteUserConfirm,
                toggleExerciseType = toggleExerciseType,
                validateInputNameString = validateInputNameString,
                resetSettings = resetSettings,
                resetSettingsConfirmation = resetSettingsConfirmation,
                cancelDialog = cancelDialog,
                screenViewState = screenViewState,
                dialogViewState = dialogViewState
            )
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsTopBar(onNavigateUp: () -> Boolean = { false }) {
    TopAppBar(
        colors = TopAppBarDefaults.smallTopAppBarColors(containerColor = MaterialTheme.colorScheme.primary),
        navigationIcon = {
            IconButton(onClick = { onNavigateUp() }) {
                Icon(
                    imageVector = ImageVector.vectorResource(R.drawable.arrow_back),
                    contentDescription = stringResource(id = R.string.back_button_content_label),
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.settings_page_title),
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge
            )
        }
    )
}

@Composable
fun SettingsContent(
    innerPadding: PaddingValues,
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
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when (screenViewState) {
            SettingsViewState.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator() //TODO: this loading is stuck at the top because the parent column is scrollable, then this child can't fillMaxHeight
                }
            }
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
                validateUserNameInput = { validateInputNameString(User(name = it))},
                onCancel = cancelDialog
            )
            is SettingsDialog.EditUser -> SettingsEditUserDialog(
                saveUserName = { saveUser(dialogViewState.user.copy(name = it)) },
                deleteUser = { deleteUser(dialogViewState.user) },
                validateUserNameInput = { validateInputNameString(dialogViewState.user.copy(name = it))},
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
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class) pairOfStates: Pair<SettingsViewState, SettingsDialog>
) {
    SimpleHiitTheme {
        SettingsScreen(
            validatePeriodLengthInput = { Constants.InputError.NONE },
            validateNumberOfWorkPeriodsInput = { Constants.InputError.NONE },
            validateSessionCountDownLengthInput = { Constants.InputError.NONE },
            validatePeriodCountDownLengthInput = { Constants.InputError.NONE },
            deleteUserCancel = {},
            validateInputNameString = { Constants.InputError.NONE },
            screenViewState = pairOfStates.first,
            dialogViewState = pairOfStates.second
        )
    }
}

internal class SettingsScreenPreviewParameterProvider :
    PreviewParameterProvider<Pair<SettingsViewState, SettingsDialog>> {

    private val exerciseTypeSelectedAlltrue = ExerciseType.values().toList().map {
        ExerciseTypeSelected(
            type = it,
            selected = true
        )
    }
    private val exerciseTypeSelectedAllfalse = ExerciseType.values().toList().map {
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

    override val values: Sequence<Pair<SettingsViewState, SettingsDialog>>
        get() = sequenceOf(
            Pair(SettingsViewState.Loading, SettingsDialog.None),
            Pair(
                SettingsViewState.Error(
                    errorCode = "this is an error code"
                ),
                SettingsDialog.None
            ),
            Pair(
                SettingsViewState.Nominal(
                    workPeriodLengthAsSeconds = "15",
                    restPeriodLengthAsSeconds = "5",
                    numberOfWorkPeriods = "4",
                    totalCycleLength = "3mn 20s",
                    beepSoundCountDownActive = true,
                    sessionStartCountDownLengthAsSeconds = "20",
                    periodsStartCountDownLengthAsSeconds = "5",
                    users = emptyList(),
                    exerciseTypes = exerciseTypeSelectedAlltrue
                ),
                SettingsDialog.None
            ),
            Pair(
                SettingsViewState.Nominal(
                    workPeriodLengthAsSeconds = "15",
                    restPeriodLengthAsSeconds = "5",
                    numberOfWorkPeriods = "4",
                    totalCycleLength = "3mn 20s",
                    beepSoundCountDownActive = false,
                    sessionStartCountDownLengthAsSeconds = "20",
                    periodsStartCountDownLengthAsSeconds = "5",
                    users = listOfOneUser,
                    exerciseTypes = exerciseTypeSelectedAlltrue
                ),
                SettingsDialog.None
            ),
            Pair(
                SettingsViewState.Nominal(
                    workPeriodLengthAsSeconds = "15",
                    restPeriodLengthAsSeconds = "5",
                    numberOfWorkPeriods = "4",
                    totalCycleLength = "3mn 20s",
                    beepSoundCountDownActive = true,
                    sessionStartCountDownLengthAsSeconds = "20",
                    periodsStartCountDownLengthAsSeconds = "5",
                    users = listOfTwoUser,
                    exerciseTypes = exerciseTypeSelectedAllfalse
                ),
                SettingsDialog.None
            ),
            Pair(
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
                SettingsDialog.None
            ),
        )
}

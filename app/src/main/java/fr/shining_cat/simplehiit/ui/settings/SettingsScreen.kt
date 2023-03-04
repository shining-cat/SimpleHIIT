package fr.shining_cat.simplehiit.ui.settings

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
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.models.ExerciseType
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.ui.components.ConfirmDialog
import fr.shining_cat.simplehiit.ui.components.ErrorDialog
import fr.shining_cat.simplehiit.ui.home.HomeDialog
import fr.shining_cat.simplehiit.ui.home.HomeScreenPreviewParameterProvider
import fr.shining_cat.simplehiit.ui.home.HomeViewState
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme
import kotlin.random.Random

@Composable
fun SettingsScreen(
    navController: NavController,
    viewModel: SettingsViewModel = hiltViewModel()
) {
    viewModel.logD("SettingsScreen", "INIT")
    viewModel.init(
        formatStringHoursMinutesSeconds = stringResource(id = R.string.hours_minutes_seconds_short),
        formatStringHoursMinutesNoSeconds = stringResource(id = R.string.hours_minutes_no_seconds_short),
        formatStringHoursNoMinutesNoSeconds = stringResource(id = R.string.hours_no_minutes_no_seconds_short),
        formatStringMinutesSeconds = stringResource(id = R.string.minutes_seconds_short),
        formatStringMinutesNoSeconds = stringResource(id = R.string.minutes_no_seconds_short),
        formatStringSeconds = stringResource(id = R.string.seconds_short)
    )
    val screenViewState = viewModel.screenViewState.collectAsState().value
    val dialogViewState = viewModel.dialogViewState.collectAsState().value
    //
    SettingsScreen(
        onNavigateUp = { navController.navigateUp() },
        editWorkPeriodLength = { viewModel.editWorkPeriodLength() },
        saveWorkPeriodLength = { viewModel.setWorkPeriodLength(it) },
        editRestPeriodLength = { viewModel.editRestPeriodLength() },
        saveRestPeriodLength = { viewModel.setRestPeriodLength(it) },
        validatePeriodLengthInput = { viewModel.validatePeriodLength(it) },
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
        validateStringInput = { viewModel.validateInputString(it) },
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
    validateStringInput: (String) -> Constants.InputError,
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
                validateStringInput = validateStringInput,
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
    validateStringInput: (String) -> Constants.InputError,
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
            SettingsViewState.SettingsLoading -> CircularProgressIndicator()
            is SettingsViewState.SettingsError -> SettingsContentBrokenState(
                errorCode = screenViewState.errorCode,
                resetSettings = resetSettings
            )
            is SettingsViewState.SettingsNominal -> SettingsContentNominal(
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
            is SettingsDialog.SettingsDialogEditWorkPeriodLength -> SettingsContentInputWorkPeriodLengthDialog(
                saveWorkPeriodLength = saveWorkPeriodLength,
                validateWorkPeriodLengthInput = validatePeriodLengthInput,
                workPeriodLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )
            is SettingsDialog.SettingsDialogEditRestPeriodLength -> SettingsContentInputRestPeriodLengthDialog(
                saveRestPeriodLength = saveRestPeriodLength,
                validateRestPeriodLengthInput = validatePeriodLengthInput,
                restPeriodLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog,
            )
            is SettingsDialog.SettingsDialogInputNumberCycles -> SettingsContentInputNumberCyclesDialog(
                saveNumber = saveNumberOfWorkPeriod,
                validateNumberCyclesInput = validateNumberOfWorkPeriodsInput,
                numberOfCycles = dialogViewState.numberOfCycles,
                onCancel = cancelDialog
            )
            is SettingsDialog.SettingsDialogEditSessionStartCountDown -> SettingsContentInputSessionCountDownLengthDialog(
                saveCountDownLength = saveSessionStartCountDown,
                validateCountDownLengthInput = validateSessionCountDownLengthInput,
                countDownLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog
            )
            is SettingsDialog.SettingsDialogEditPeriodStartCountDown -> SettingsContentInputPeriodCountDownLengthDialog(
                saveCountDownLength = savePeriodStartCountDown,
                validateCountDownLengthInput = validatePeriodCountDownLengthInput,
                countDownLengthSeconds = dialogViewState.valueSeconds,
                onCancel = cancelDialog
            )
            SettingsDialog.SettingsDialogAddUser -> SettingsContentCreateUserDialog(
                saveUserName = { saveUser(User(name = it)) },
                validateUserNameInput = validateStringInput,
                onCancel = cancelDialog
            )
            is SettingsDialog.SettingsDialogEditUser -> SettingsContentEditUserDialog(
                saveUserName = { saveUser(dialogViewState.user.copy(name = it)) },
                deleteUser = { deleteUser(dialogViewState.user) },
                validateUserNameInput = validateStringInput,
                userName = dialogViewState.user.name,
                onCancel = cancelDialog
            )
            is SettingsDialog.SettingsDialogConfirmDeleteUser -> ConfirmDialog(
                message = stringResource(id = R.string.delete_confirmation_button_label),
                buttonConfirmLabel = stringResource(id = R.string.delete_button_label),
                onConfirm = { deleteUserConfirm(dialogViewState.user) },
                onCancel = { deleteUserCancel(dialogViewState.user) } //coming back to the edit user dialog instead of closing simply the dialog
            )
            SettingsDialog.SettingsDialogConfirmResetAllSettings -> ConfirmDialog(
                message = stringResource(id = R.string.reset_settings_confirmation_button_label),
                buttonConfirmLabel = stringResource(id = R.string.reset_button_label),
                onConfirm = resetSettingsConfirmation,
                onCancel = cancelDialog
            )
            is SettingsDialog.InputCausedError -> ErrorDialog(
                errorMessage = "",
                errorCode = dialogViewState.errorCode,
                onCancel = cancelDialog
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
            validateStringInput = { Constants.InputError.NONE },
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
    private val exerciseTypeSelectedMixed = ExerciseType.values().toList().map{
        ExerciseTypeSelected(
            type = it,
            selected = (ExerciseType.values().indexOf(it) %2 == 0)
        )
    }

    private val listOfOneUser = listOf(User(name = "user 1"))
    private val listOfTwoUser = listOf(User(name = "user 1"), User(name = "user 2"))
    private val listOfMoreUser = listOf(User(name = "user 1"), User(name = "user 2"), User(name = "user 3"), User(name = "user 4"), User(name = "user 5"))

    override val values: Sequence<Pair<SettingsViewState, SettingsDialog>>
        get() = sequenceOf(
            Pair(SettingsViewState.SettingsLoading, SettingsDialog.None),
            Pair(
                SettingsViewState.SettingsError(
                    errorCode = "this is an error code"
                ),
                SettingsDialog.None
            ),
            Pair(
                SettingsViewState.SettingsNominal(
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
                SettingsViewState.SettingsNominal(
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
                SettingsViewState.SettingsNominal(
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
                SettingsViewState.SettingsNominal(
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

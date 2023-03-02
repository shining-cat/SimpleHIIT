package fr.shining_cat.simplehiit.ui.settings

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.models.User

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
        editWorkPeriodLength = {viewModel.editWorkPeriodLength()},
        saveWorkPeriodLength = {viewModel.setWorkPeriodLength(it)},
        editRestPeriodLength = {viewModel.editRestPeriodLength()},
        saveRestPeriodLength = {viewModel.setRestPeriodLength(it)},
        editNumberOfWorkPeriod = {viewModel.editNumberOfWorkPeriods()},
        saveNumberOfWorkPeriod = {viewModel.setNumberOfWorkPeriods(it)},
        toggleBeepSound = {viewModel.toggleBeepSound()},
        editSessionStartCountDown = {viewModel.editSessionStartCountDown()},
        saveSessionStartCountDown = {viewModel.setSessionStartCountDown(it)},
        editPeriodStartCountDown = {viewModel.editPeriodStartCountDown()},
        savePeriodStartCountDown = {viewModel.setPeriodStartCountDown(it)},
        editUser = {viewModel.editUser(it)},
        addUser = {viewModel.addUser()},
        saveUser = {viewModel.saveUser(it)},
        deleteUser = {viewModel.deleteUser(it)},
        deleteUserCancel = {viewModel.deleteUser(it)},
        deleteUserConfirm = {viewModel.deleteUserConfirmation(it)},
        toggleExerciseType = {viewModel.toggleSelectedExercise(it)},
        validateNumberInput = {viewModel.validateInputString(it)},
        validateStringInput = {viewModel.validateInputInt(it)},
        resetSettings = {viewModel.resetAllSettings()},
        resetSettingsConfirmation = {viewModel.resetAllSettingsConfirmation()},
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
    editNumberOfWorkPeriod: () -> Unit = {},
    saveNumberOfWorkPeriod: (String) -> Unit = {},
    toggleBeepSound: () -> Unit = {},
    editSessionStartCountDown: (String) -> Unit = {},
    saveSessionStartCountDown: (String) -> Unit = {},
    editPeriodStartCountDown: (String) -> Unit = {},
    savePeriodStartCountDown: (String) -> Unit = {},
    editUser: (User) -> Unit = {},
    addUser: () -> Unit = {},
    saveUser: (User) -> Unit = {},
    deleteUser: (User) -> Unit = {},
    deleteUserCancel: (User) -> Unit,
    deleteUserConfirm: (User) -> Unit = {},
    toggleExerciseType: (ExerciseTypeSelected) -> Unit = {},
    validateNumberInput: (String) -> Boolean,
    validateStringInput: (String) -> Boolean,
    resetSettings: (String) -> Unit = {},
    resetSettingsConfirmation: (String) -> Unit = {},
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
                editNumberOfWorkPeriod = editNumberOfWorkPeriod,
                saveNumberOfWorkPeriod = saveNumberOfWorkPeriod,
                toggleBeepSound = toggleBeepSound,
                editSessionStartCountDown = editSessionStartCountDown,
                saveSessionStartCountDown = saveSessionStartCountDown,
                editPeriodStartCountDown = editPeriodStartCountDown,
                savePeriodStartCountDown = savePeriodStartCountDown,
                editUser = editUser,
                addUser = addUser,
                saveUser = saveUser,
                deleteUser = deleteUser,
                deleteUserCancel = deleteUserCancel,
                deleteUserConfirm = deleteUserConfirm,
                toggleExerciseType = toggleExerciseType,
                validateNumberInput = validateNumberInput,
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
    editNumberOfWorkPeriod: () -> Unit,
    saveNumberOfWorkPeriod: (String) -> Unit,
    toggleBeepSound: () -> Unit,
    editSessionStartCountDown: (String) -> Unit,
    saveSessionStartCountDown: (String) -> Unit,
    editPeriodStartCountDown: (String) -> Unit,
    savePeriodStartCountDown: (String) -> Unit,
    editUser: (User) -> Unit,
    addUser: () -> Unit,
    saveUser: (User) -> Unit,
    deleteUser: (User) -> Unit,
    deleteUserCancel: (User) -> Unit,
    deleteUserConfirm: (User) -> Unit,
    toggleExerciseType: (ExerciseTypeSelected) -> Unit,
    validateNumberInput: (String) -> Boolean,
    validateStringInput: (String) -> Boolean,
    resetSettings: (String) -> Unit,
    resetSettingsConfirmation: (String) -> Unit,
    cancelDialog: () -> Unit,
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog
) {
    Column(
        modifier = Modifier
            .fillMaxSize() //TODO: handle landscape layout
            .padding(paddingValues = innerPadding),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when(screenViewState){
            is SettingsViewState.SettingsError -> TODO()
            SettingsViewState.SettingsLoading -> TODO()
            is SettingsViewState.SettingsNominal -> TODO()
        }
        when(dialogViewState){
            SettingsDialog.None -> TODO()
            SettingsDialog.SettingsDialogAddUser -> TODO()
            is SettingsDialog.SettingsDialogConfirmDeleteUser -> TODO()
            SettingsDialog.SettingsDialogConfirmResetAllSettings -> TODO()
            is SettingsDialog.SettingsDialogEditPeriodStartCountDown -> TODO()
            is SettingsDialog.SettingsDialogEditRestPeriodLength -> TODO()
            is SettingsDialog.SettingsDialogEditSessionStartCountDown -> TODO()
            is SettingsDialog.SettingsDialogEditUser -> TODO()
            is SettingsDialog.SettingsDialogEditWorkPeriodLength -> TODO()
            is SettingsDialog.SettingsDialogInputNumberCycles -> TODO()
        }
    }
}
// Previews

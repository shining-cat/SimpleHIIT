package fr.shining_cat.simplehiit.android.mobile.ui.settings

import android.content.res.Configuration
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.hilt.navigation.compose.hiltViewModel
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.NavigateUpTopBar
import fr.shining_cat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shining_cat.simplehiit.android.mobile.ui.settings.contents.SettingsContentHolder
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import fr.shining_cat.simplehiit.domain.common.Constants
import fr.shining_cat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User

@Composable
fun SettingsScreen(
    navigateTo: (String) -> Unit,
    uiArrangement: UiArrangement,
    viewModel: SettingsViewModel = hiltViewModel(),
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger
) {
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
        navigateTo = navigateTo,
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
        uiArrangement = uiArrangement,
        screenViewState = screenViewState,
        dialogViewState = dialogViewState
    )
}

@Composable
private fun SettingsScreen(
    navigateTo: (String) -> Unit = {},
    editWorkPeriodLength: () -> Unit = {},
    saveWorkPeriodLength: (String) -> Unit = {},
    editRestPeriodLength: () -> Unit = {},
    saveRestPeriodLength: (String) -> Unit = {},
    validatePeriodLengthInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    editNumberOfWorkPeriod: () -> Unit = {},
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
    uiArrangement: UiArrangement,
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            NavigationSideBar(
                navigateTo = navigateTo,
                currentDestination = fr.shining_cat.simplehiit.android.common.Screen.Settings,
                showStatisticsButton = screenViewState is SettingsViewState.Nominal && screenViewState.users.isNotEmpty()
            )
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                NavigateUpTopBar(
                    navigateUp = { navigateTo(fr.shining_cat.simplehiit.android.common.Screen.Home.route); true }, //forcing nav to home instead of up to avoid popping the backstack(which is possible after orientation change)
                    title = R.string.settings_page_title
                )
            }
            SettingsContentHolder(
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
                uiArrangement = uiArrangement,
                screenViewState = screenViewState,
                dialogViewState = dialogViewState
            )
        }
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    widthDp = 400
)
@Preview(
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    widthDp = 400
)
@Composable
private fun SettingsScreenPreviewPhonePortrait(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class) viewState: SettingsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SettingsScreen(
                uiArrangement = UiArrangement.VERTICAL,
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showSystemUi = true,
    device = Devices.TABLET,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsScreenPreviewTabletLandscape(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class) viewState: SettingsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SettingsScreen(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None
            )
        }
    }
}

@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_NO,
    heightDp = 400
)
@Preview(
    showSystemUi = true,
    device = "spec:parent=pixel_4,orientation=landscape",
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    heightDp = 400
)
@Composable
private fun SettingsScreenPreviewPhoneLandscape(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class) viewState: SettingsViewState
) {
    SimpleHiitMobileTheme {
        Surface {
            SettingsScreen(
                uiArrangement = UiArrangement.HORIZONTAL,
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None
            )
        }
    }
}

internal class SettingsScreenPreviewParameterProvider :
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

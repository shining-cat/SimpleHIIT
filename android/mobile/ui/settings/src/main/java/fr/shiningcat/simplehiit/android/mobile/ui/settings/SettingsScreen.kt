/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
@file:KeepForCompose

package fr.shiningcat.simplehiit.android.mobile.ui.settings

import androidx.activity.compose.LocalActivity
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigateUpTopBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.NavigationSideBar
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.mainContentInsets
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.settings.contents.SettingsContentHolder
import fr.shiningcat.simplehiit.android.shared.core.Screen
import fr.shiningcat.simplehiit.android.shared.settings.SettingsViewModel
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.commonutils.annotations.KeepForCompose
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.settings.SettingsDialog
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewState
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun SettingsScreen(
    navigateTo: (Screen) -> Unit,
    uiArrangement: UiArrangement,
    viewModel: SettingsViewModel = koinViewModel(),
    @Suppress("UNUSED_PARAMETER")
    hiitLogger: HiitLogger,
) {
    val screenViewState = viewModel.screenViewState.collectAsStateWithLifecycle().value
    val dialogViewState = viewModel.dialogViewState.collectAsStateWithLifecycle().value

    // Observe restart trigger and recreate activity when theme changes
    val activity = LocalActivity.current
    LaunchedEffect(Unit) {
        viewModel.restartTrigger.collectLatest {
            activity?.recreate()
        }
    }

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
        editLanguage = { viewModel.editLanguage() },
        saveLanguage = { viewModel.setLanguage(it) },
        editTheme = { viewModel.editTheme() },
        saveTheme = { viewModel.setTheme(it) },
        resetSettings = { viewModel.resetAllSettings() },
        resetSettingsConfirmation = { viewModel.resetAllSettingsConfirmation() },
        cancelDialog = { viewModel.cancelDialog() },
        uiArrangement = uiArrangement,
        screenViewState = screenViewState,
        dialogViewState = dialogViewState,
    )
}

@Composable
private fun SettingsScreen(
    navigateTo: (Screen) -> Unit = {},
    editWorkPeriodLength: () -> Unit = {},
    saveWorkPeriodLength: (String) -> Unit = {},
    editRestPeriodLength: () -> Unit = {},
    saveRestPeriodLength: (String) -> Unit = {},
    validatePeriodLengthInput: (String) -> InputError? = { null },
    editNumberOfWorkPeriod: () -> Unit = {},
    validateNumberOfWorkPeriodsInput: (String) -> InputError? = { null },
    saveNumberOfWorkPeriod: (String) -> Unit = {},
    toggleBeepSound: () -> Unit = {},
    editSessionStartCountDown: () -> Unit = {},
    validateSessionCountDownLengthInput: (String) -> InputError? = { null },
    saveSessionStartCountDown: (String) -> Unit = {},
    editPeriodStartCountDown: () -> Unit = {},
    validatePeriodCountDownLengthInput: (String) -> InputError? = { null },
    savePeriodStartCountDown: (String) -> Unit = {},
    editUser: (User) -> Unit = {},
    addUser: () -> Unit = {},
    saveUser: (User) -> Unit = {},
    deleteUser: (User) -> Unit = {},
    deleteUserCancel: (User) -> Unit = {},
    deleteUserConfirm: (User) -> Unit = {},
    toggleExerciseType: (ExerciseTypeSelected) -> Unit = {},
    validateInputNameString: (User) -> InputError? = { null },
    editLanguage: () -> Unit = {},
    saveLanguage: (AppLanguage) -> Unit = {},
    editTheme: () -> Unit = {},
    saveTheme: (AppTheme) -> Unit = {},
    resetSettings: () -> Unit = {},
    resetSettingsConfirmation: () -> Unit = {},
    cancelDialog: () -> Unit = {},
    uiArrangement: UiArrangement,
    screenViewState: SettingsViewState,
    dialogViewState: SettingsDialog,
) {
    Row(modifier = Modifier.fillMaxSize()) {
        AnimatedVisibility(visible = uiArrangement == UiArrangement.HORIZONTAL) {
            NavigationSideBar(
                navigateTo = navigateTo,
                currentDestination = Screen.Settings,
                showStatisticsButton = screenViewState is SettingsViewState.Nominal && screenViewState.users.isNotEmpty(),
            )
        }
        Column(
            modifier =
                Modifier
                    .mainContentInsets(uiArrangement)
                    .fillMaxSize(),
        ) {
            AnimatedVisibility(visible = uiArrangement == UiArrangement.VERTICAL) {
                // forcing nav to home instead of up to avoid popping the backstack(which is possible after orientation change)
                NavigateUpTopBar(
                    icon = R.drawable.arrow_back,
                    label = R.string.settings_page_title,
                    onClick = {
                        navigateTo(Screen.Home)
                        true
                    },
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
                editLanguage = editLanguage,
                saveLanguage = saveLanguage,
                editTheme = editTheme,
                saveTheme = saveTheme,
                resetSettings = resetSettings,
                resetSettingsConfirmation = resetSettingsConfirmation,
                cancelDialog = cancelDialog,
                uiArrangement = uiArrangement,
                screenViewState = screenViewState,
                dialogViewState = dialogViewState,
            )
        }
    }
}

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SettingsScreenPreview(
    @PreviewParameter(SettingsScreenPreviewParameterProvider::class) viewState: SettingsViewState,
) {
    val previewUiArrangement = currentUiArrangement()
    SimpleHiitMobileTheme {
        Surface {
            SettingsScreen(
                uiArrangement = previewUiArrangement,
                screenViewState = viewState,
                dialogViewState = SettingsDialog.None,
            )
        }
    }
}

internal class SettingsScreenPreviewParameterProvider : PreviewParameterProvider<SettingsViewState> {
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
                    currentTheme = AppTheme.FOLLOW_SYSTEM,
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
                    currentTheme = AppTheme.LIGHT,
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
                    currentTheme = AppTheme.DARK,
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
                    currentTheme = AppTheme.FOLLOW_SYSTEM,
                ),
            )
}

package fr.shiningcat.simplehiit.android.tv.ui.settings.contents

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.SettingsViewState
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.SettingsExercisesSelectedComponent
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.SettingsFieldComponent
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.SettingsToggleComponent
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.SettingsUsersComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User
import kotlinx.coroutines.delay

@Composable
fun SettingsNominalContent(
    editWorkPeriodLength: () -> Unit = {},
    editRestPeriodLength: () -> Unit = {},
    editNumberOfWorkPeriods: () -> Unit = {},
    toggleBeepSound: () -> Unit = {},
    editSessionStartCountDown: () -> Unit = {},
    editPeriodStartCountDown: () -> Unit = {},
    editUser: (User) -> Unit = {},
    addUser: () -> Unit = {},
    toggleExerciseType: (ExerciseTypeSelected) -> Unit = {},
    editLanguage: () -> Unit = {},
    resetSettings: () -> Unit = {},
    viewState: SettingsViewState.Nominal,
    hiitLogger: HiitLogger? = null,
) {
    val firstButtonFocusRequester = remember { FocusRequester() }
    LaunchedEffect(Unit) {
        // wait a bit to increase awareness of the user of the focusing on the first button
        delay(200L)
        // calling focus on the first setting on opening. This we only want to apply on first composition
        firstButtonFocusRequester.requestFocus()
    }

    // we only lose focus when toggling our exercisetype buttons, so these are the only ones we need to manually help to survive recomposition
    val exerciseButtonsFocusRequesters =
        remember { viewState.exerciseTypes.associate { it.type.name to FocusRequester() } }
    var focusedExerciseButton by remember { mutableStateOf("") }
    LaunchedEffect(key1 = viewState.exerciseTypes) {
        if (focusedExerciseButton.isNotBlank()) {
            exerciseButtonsFocusRequesters.getValue(focusedExerciseButton).requestFocus()
        }
    }

    LazyColumn(
        modifier =
            Modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_2))
                .fillMaxSize(),
    ) {
        item {
            // cheating to avoid truncating zoom-in focus effect on first item
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_2)))
        }
        item {
            SettingsFieldComponent(
                modifier = Modifier.focusRequester(firstButtonFocusRequester),
                label = stringResource(id = R.string.work_period_length_label),
                value =
                    stringResource(
                        id = R.string.seconds_setting_value,
                        viewState.workPeriodLengthAsSeconds,
                    ),
                onClick = editWorkPeriodLength,
                hiitLogger = hiitLogger,
            )
        }
        item {
            SettingsFieldComponent(
                label = stringResource(id = R.string.rest_period_length_label),
                value =
                    stringResource(
                        id = R.string.seconds_setting_value,
                        viewState.restPeriodLengthAsSeconds,
                    ),
                onClick = editRestPeriodLength,
            )
        }
        item {
            SettingsFieldComponent(
                label = stringResource(id = R.string.number_of_periods_label),
                value = viewState.numberOfWorkPeriods,
                secondaryLabel = stringResource(id = R.string.total_cycle_length_label),
                secondaryValue = viewState.totalCycleLength,
                onClick = editNumberOfWorkPeriods,
            )
        }
        item {
            SettingsToggleComponent(
                label = stringResource(id = R.string.beep_sound_setting_label),
                value = viewState.beepSoundCountDownActive,
                onToggle = toggleBeepSound,
            )
        }
        item {
            SettingsFieldComponent(
                label = stringResource(id = R.string.period_start_countdown_length_setting_label),
                value =
                    stringResource(
                        id = R.string.seconds_setting_value,
                        viewState.periodsStartCountDownLengthAsSeconds,
                    ),
                onClick = editPeriodStartCountDown,
            )
        }
        item {
            SettingsFieldComponent(
                label = stringResource(id = R.string.session_start_countdown_length_setting_label),
                value =
                    stringResource(
                        id = R.string.seconds_setting_value,
                        viewState.sessionStartCountDownLengthAsSeconds,
                    ),
                onClick = editSessionStartCountDown,
            )
        }
        item {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            )
        }
        item {
            SettingsUsersComponent(
                users = viewState.users,
                onClickUser = editUser,
                onAddUser = addUser,
            )
        }
        item {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            )
        }
        item {
            SettingsExercisesSelectedComponent(
                exerciseTypes = viewState.exerciseTypes,
                onToggle = {
                    focusedExerciseButton =
                        it.type.name // storing the button acted upon for resetting focus after recomposition
                    toggleExerciseType(it)
                },
                exerciseButtonsFocusRequesters = exerciseButtonsFocusRequesters,
                hiitLogger = hiitLogger,
            )
        }
        item {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            )
        }
        item {
            SettingsFieldComponent(
                label = stringResource(id = R.string.language_setting_label),
                value = getLanguageDisplayName(viewState.currentLanguage),
                onClick = editLanguage,
            )
        }
        item {
            Spacer(
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            )
        }
        item {
            Row(modifier = Modifier.fillMaxWidth()) {
                Spacer(modifier = Modifier.weight(.3f))
                ButtonText(
                    modifier =
                        Modifier
                            .padding(vertical = dimensionResource(R.dimen.spacing_3))
                            .weight(weight = .3f, fill = true),
                    fillHeight = true,
                    fillWidth = true,
                    onClick = resetSettings,
                    label = stringResource(id = R.string.reset_settings_button_label),
                )
                Spacer(modifier = Modifier.weight(.3f))
            }
        }
        item {
            // cheating to avoid truncating zoom-in focus effect on last item
            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_2)))
        }
    }
}

private fun getLanguageDisplayName(language: AppLanguage): String =
    when (language) {
        AppLanguage.SYSTEM_DEFAULT -> "System default"
        AppLanguage.ENGLISH -> "English"
        AppLanguage.FRENCH -> "Français"
        AppLanguage.SWEDISH -> "Svenska"
    }

// Previews
@PreviewTvScreensNoUi
@Composable
private fun SettingsNominalContentPreviewPhonePortrait(
    @PreviewParameter(SettingsNominalContentPreviewParameterProvider::class) viewState: SettingsViewState.Nominal,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsNominalContent(viewState = viewState)
        }
    }
}

internal class SettingsNominalContentPreviewParameterProvider : PreviewParameterProvider<SettingsViewState.Nominal> {
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

    override val values: Sequence<SettingsViewState.Nominal>
        get() =
            sequenceOf(
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
                    beepSoundCountDownActive = true,
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
                    beepSoundCountDownActive = true,
                    sessionStartCountDownLengthAsSeconds = "20",
                    periodsStartCountDownLengthAsSeconds = "5",
                    users = listOfMoreUser,
                    exerciseTypes = exerciseTypeSelectedMixed,
                    currentLanguage = AppLanguage.SWEDISH,
                ),
            )
}

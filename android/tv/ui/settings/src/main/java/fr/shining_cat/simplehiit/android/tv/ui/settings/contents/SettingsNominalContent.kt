package fr.shining_cat.simplehiit.android.tv.ui.settings.contents

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import fr.shining_cat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shining_cat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shining_cat.simplehiit.android.tv.ui.settings.SettingsViewState
import fr.shining_cat.simplehiit.android.tv.ui.settings.components.SettingsExercisesSelectedComponent
import fr.shining_cat.simplehiit.android.tv.ui.settings.components.SettingsFieldComponent
import fr.shining_cat.simplehiit.android.tv.ui.settings.components.SettingsToggleComponent
import fr.shining_cat.simplehiit.android.tv.ui.settings.components.SettingsUsersComponent
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User

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
    resetSettings: () -> Unit = {},
    viewState: SettingsViewState.Nominal
) {
    Column(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        SettingsFieldComponent(
            label = stringResource(id = R.string.work_period_length_label),
            value = stringResource(
                id = R.string.seconds_setting_value,
                viewState.workPeriodLengthAsSeconds
            ),
            onClick = editWorkPeriodLength
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.rest_period_length_label),
            value = stringResource(
                id = R.string.seconds_setting_value,
                viewState.restPeriodLengthAsSeconds
            ),
            onClick = editRestPeriodLength
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.number_of_periods_label),
            value = viewState.numberOfWorkPeriods,
            secondaryLabel = stringResource(id = R.string.total_cycle_length_label),
            secondaryValue = viewState.totalCycleLength,
            onClick = editNumberOfWorkPeriods
        )
        SettingsToggleComponent(
            label = stringResource(id = R.string.beep_sound_setting_label),
            value = viewState.beepSoundCountDownActive,
            onToggle = toggleBeepSound
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.period_start_countdown_length_setting_label),
            value = stringResource(
                id = R.string.seconds_setting_value, viewState.periodsStartCountDownLengthAsSeconds
            ),
            secondaryLabel = stringResource(id = R.string.period_length_too_short_constraint),
            onClick = editPeriodStartCountDown
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.session_start_countdown_length_setting_label),
            value = stringResource(
                id = R.string.seconds_setting_value, viewState.sessionStartCountDownLengthAsSeconds
            ),
            onClick = editSessionStartCountDown
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        SettingsUsersComponent(users = viewState.users, onClickUser = editUser, onAddUser = addUser)
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        SettingsExercisesSelectedComponent(
            exerciseTypes = viewState.exerciseTypes,
            onToggle = toggleExerciseType
        )
        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        )
        ButtonText(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick = resetSettings,
            label = stringResource(id = R.string.reset_settings_button_label)
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
private fun SettingsNominalContentPreviewPhonePortrait(
    @PreviewParameter(SettingsNominalContentPreviewParameterProvider::class) viewState: SettingsViewState.Nominal
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            SettingsNominalContent(viewState = viewState)
        }
    }
}

internal class SettingsNominalContentPreviewParameterProvider :
    PreviewParameterProvider<SettingsViewState.Nominal> {

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

    override val values: Sequence<SettingsViewState.Nominal>
        get() = sequenceOf(
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
                beepSoundCountDownActive = true,
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
                beepSoundCountDownActive = true,
                sessionStartCountDownLengthAsSeconds = "20",
                periodsStartCountDownLengthAsSeconds = "5",
                users = listOfMoreUser,
                exerciseTypes = exerciseTypeSelectedMixed
            )
        )
}

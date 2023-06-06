package fr.shining_cat.simplehiit.android.mobile.ui.settings.contents

import android.content.res.Configuration
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.common.components.ToggleButton
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.domain.common.models.ExerciseType
import fr.shining_cat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.android.mobile.ui.settings.SettingsViewState
import kotlin.math.roundToInt

@Composable
fun SettingsNominalContent(
    editWorkPeriodLength: () -> Unit,
    editRestPeriodLength: () -> Unit,
    editNumberOfWorkPeriods: () -> Unit,
    toggleBeepSound: () -> Unit,
    editSessionStartCountDown: () -> Unit,
    editPeriodStartCountDown: () -> Unit,
    editUser: (User) -> Unit,
    addUser: () -> Unit,
    toggleExerciseType: (ExerciseTypeSelected) -> Unit,
    resetSettings: () -> Unit,
    viewState: SettingsViewState.Nominal,
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        SettingFieldComponent(
            label = stringResource(id = R.string.work_period_length_label),
            value = stringResource(
                id = R.string.seconds_setting_value,
                viewState.workPeriodLengthAsSeconds
            ),
            onClick = editWorkPeriodLength
        )
        SettingFieldComponent(
            label = stringResource(id = R.string.rest_period_length_label),
            value = stringResource(
                id = R.string.seconds_setting_value,
                viewState.restPeriodLengthAsSeconds
            ),
            onClick = editRestPeriodLength
        )
        SettingFieldComponent(
            label = stringResource(id = R.string.number_of_periods_label),
            value = viewState.numberOfWorkPeriods,
            secondaryLabel = stringResource(id = R.string.total_cycle_length_label),
            secondaryValue = viewState.totalCycleLength,
            onClick = editNumberOfWorkPeriods
        )
        SettingToggleComponent(
            label = stringResource(id = R.string.beep_sound_setting_label),
            value = viewState.beepSoundCountDownActive,
            onToggle = toggleBeepSound
        )
        SettingFieldComponent(
            label = stringResource(id = R.string.period_start_countdown_length_setting_label),
            value = stringResource(
                id = R.string.seconds_setting_value, viewState.periodsStartCountDownLengthAsSeconds
            ),
            secondaryLabel = stringResource(id = R.string.period_length_too_short_constraint),
            onClick = editPeriodStartCountDown
        )
        SettingFieldComponent(
            label = stringResource(id = R.string.session_start_countdown_length_setting_label),
            value = stringResource(
                id = R.string.seconds_setting_value, viewState.sessionStartCountDownLengthAsSeconds
            ),
            onClick = editSessionStartCountDown
        )
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        SettingUsersComponent(users = viewState.users, onClickUser = editUser, onAddUser = addUser)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        SettingExercisesSelectedComponent(exerciseTypes = viewState.exerciseTypes, onToggle = toggleExerciseType)
        Divider(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp), thickness = 1.dp
        )
        TextButton(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 16.dp)
                .align(Alignment.CenterHorizontally),
            onClick = resetSettings
        ) {
            Text(text = stringResource(id = R.string.reset_settings_button_label))
        }
    }
}

@Composable
private fun SettingFieldComponent(
    label: String,
    value: String,
    onClick: () -> Unit,
    secondaryLabel: String = "",
    secondaryValue: String = ""
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp)
            .clickable { onClick() },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(horizontalAlignment = Alignment.Start) {
            Text(text = label, style = MaterialTheme.typography.headlineSmall)
            if (secondaryLabel.isNotBlank()) {
                Text(text = secondaryLabel, style = MaterialTheme.typography.labelMedium)
            }
        }
        Column(horizontalAlignment = Alignment.End) {
            Text(text = value, style = MaterialTheme.typography.headlineSmall)
            if (secondaryValue.isNotBlank()) {
                Text(text = secondaryValue, style = MaterialTheme.typography.labelMedium)
            }
        }
    }
}

@Composable
private fun SettingToggleComponent(
    label: String,
    value: Boolean,
    onToggle: () -> Unit,
) {
    Row(
        Modifier
            .fillMaxWidth()
            .defaultMinSize(minHeight = 48.dp)
            .padding(bottom = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, style = MaterialTheme.typography.headlineSmall)
        Switch(
            checked = value,
            onCheckedChange = { onToggle() },
            colors = SwitchDefaults.colors(
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                checkedThumbColor = MaterialTheme.colorScheme.secondary,
                checkedBorderColor = MaterialTheme.colorScheme.primary,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface,
                uncheckedThumbColor = MaterialTheme.colorScheme.primary,
                uncheckedBorderColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}

@Composable
private fun SettingUsersComponent(
    users: List<User>,
    onClickUser: (User) -> Unit,
    onAddUser: () -> Unit
) {
    Column(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp)
    ) {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.users_list_setting_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        LazyRow(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(users.size) {
                val user = users[it]
                OutlinedButton(
                    modifier = Modifier
                        .height(56.dp)
                        .defaultMinSize(minWidth = 112.dp),
                    onClick = { onClickUser(user) }
                ) {
                    Text(text = user.name)
                }
            }
            item {
                Button(
                    modifier = Modifier.height(56.dp),
                    onClick = onAddUser,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary,
                        contentColor = MaterialTheme.colorScheme.onSecondary
                    )
                ) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = stringResource(id = R.string.add_user_button_label),
                        tint = MaterialTheme.colorScheme.onSecondary
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun SettingExercisesSelectedComponent(
    exerciseTypes: List<ExerciseTypeSelected>,
    onToggle: (ExerciseTypeSelected) -> Unit,
) {
    Column {
        Text(
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth(),
            style = MaterialTheme.typography.headlineMedium,
            text = stringResource(id = R.string.selected_exercise_types_list_setting_label)
        )
        Spacer(modifier = Modifier.height(8.dp))
        val itemHeight = 56.dp
        val numberOfColumns = 3
        val spacing = 8.dp
        val rowsCount = (exerciseTypes.size.toFloat() / numberOfColumns.toFloat()).roundToInt()
        val gridHeight = (itemHeight + spacing) * rowsCount
        LazyVerticalGrid(
            modifier = Modifier.height(gridHeight),
            columns = GridCells.Fixed(numberOfColumns),
            verticalArrangement = Arrangement.spacedBy(spacing),
            horizontalArrangement = Arrangement.spacedBy(spacing),
            userScrollEnabled = false
        ) {
            items(exerciseTypes.size) {
                val exerciseTypeSelected = exerciseTypes[it]
                ToggleButton(
                    modifier = Modifier.height(itemHeight),
                    label = exerciseTypeSelected.type.name,
                    selected = exerciseTypeSelected.selected,
                    onToggle = { onToggle(exerciseTypeSelected) }
                )
            }
        }
    }
}


// Previews
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun SettingsContentNominalPreview(
    @PreviewParameter(SettingsContentNominalPreviewParameterProvider::class) viewState: SettingsViewState.Nominal
) {
    SimpleHiitTheme {
        SettingsNominalContent(
            editWorkPeriodLength = {},
            editRestPeriodLength = {},
            editNumberOfWorkPeriods = {},
            toggleBeepSound = {},
            editSessionStartCountDown = {},
            editPeriodStartCountDown = {},
            editUser = {},
            addUser = {},
            toggleExerciseType = {},
            resetSettings = {},
            viewState = viewState
        )
    }
}

internal class SettingsContentNominalPreviewParameterProvider :
    PreviewParameterProvider<SettingsViewState.Nominal> {

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
                exerciseTypes = exerciseTypeSelectedAlltrue
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
                exerciseTypes = exerciseTypeSelectedAlltrue
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
                exerciseTypes = exerciseTypeSelectedAllfalse
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

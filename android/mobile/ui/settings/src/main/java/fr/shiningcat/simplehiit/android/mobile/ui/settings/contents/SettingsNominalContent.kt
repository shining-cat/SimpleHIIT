package fr.shiningcat.simplehiit.android.mobile.ui.settings.contents

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DividerDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.components.OnSurfaceTextButton
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsExercisesSelectedComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsFieldComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsToggleComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsUsersComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.settings.SettingsViewState

@Composable
fun SettingsNominalContent(
    modifier: Modifier = Modifier,
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
    editTheme: () -> Unit = {},
    resetSettings: () -> Unit = {},
    viewState: SettingsViewState.Nominal,
    @Suppress("UNUSED_PARAMETER")
    uiArrangement: UiArrangement,
) {
    Column(
        modifier =
            modifier
                .padding(horizontal = dimensionResource(R.dimen.spacing_1))
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_1)),
    ) {
        SettingsFieldComponent(
            modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_1)),
            label = stringResource(id = R.string.work_period_length_label),
            value =
                stringResource(
                    id = R.string.seconds_setting_value,
                    viewState.workPeriodLengthAsSeconds,
                ),
            onClick = editWorkPeriodLength,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.rest_period_length_label),
            value =
                stringResource(
                    id = R.string.seconds_setting_value,
                    viewState.restPeriodLengthAsSeconds,
                ),
            onClick = editRestPeriodLength,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.number_of_periods_label),
            value = viewState.numberOfWorkPeriods,
            secondaryLabel = stringResource(id = R.string.total_cycle_length_label),
            secondaryValue = viewState.totalCycleLength,
            onClick = editNumberOfWorkPeriods,
        )
        SettingsToggleComponent(
            label = stringResource(id = R.string.beep_sound_setting_label),
            value = viewState.beepSoundCountDownActive,
            onToggle = toggleBeepSound,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.period_start_countdown_length_setting_label),
            value =
                stringResource(
                    id = R.string.seconds_setting_value,
                    viewState.periodsStartCountDownLengthAsSeconds,
                ),
            onClick = editPeriodStartCountDown,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.session_start_countdown_length_setting_label),
            value =
                stringResource(
                    id = R.string.seconds_setting_value,
                    viewState.sessionStartCountDownLengthAsSeconds,
                ),
            onClick = editSessionStartCountDown,
        )
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
            color = DividerDefaults.color,
        )
        SettingsUsersComponent(users = viewState.users, onClickUser = editUser, onAddUser = addUser)
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
            color = DividerDefaults.color,
        )
        SettingsExercisesSelectedComponent(
            exerciseTypes = viewState.exerciseTypes,
            onToggle = toggleExerciseType,
        )
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
            color = DividerDefaults.color,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.language_setting_label),
            value = getLanguageDisplayName(viewState.currentLanguage),
            onClick = editLanguage,
        )
        SettingsFieldComponent(
            label = stringResource(id = R.string.theme_setting_label),
            value = stringResource(id = getThemeDisplayNameResId(viewState.currentTheme)),
            onClick = editTheme,
        )
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
            color = DividerDefaults.color,
        )
        OnSurfaceTextButton(
            modifier =
                Modifier
                    .padding(
                        horizontal = 0.dp,
                        vertical = dimensionResource(R.dimen.spacing_2),
                    ).align(Alignment.CenterHorizontally),
            onClick = resetSettings,
        ) {
            Text(
                text = stringResource(id = R.string.reset_settings_button_label),
                textAlign = TextAlign.Center,
            )
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

/**
 * Returns the string resource ID for the display name of the given theme.
 *
 * @param theme The theme to get the display name for
 * @return The string resource ID for the theme's display name
 */
private fun getThemeDisplayNameResId(theme: AppTheme): Int =
    when (theme) {
        AppTheme.LIGHT -> R.string.theme_light
        AppTheme.DARK -> R.string.theme_dark
        AppTheme.FOLLOW_SYSTEM -> R.string.theme_follow_system
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SettingsNominalContentPreview(
    @PreviewParameter(SettingsNominalContentPreviewParameterProvider::class) viewState: SettingsViewState.Nominal,
) {
    val previewUiArrangement = currentUiArrangement()
    SimpleHiitMobileTheme {
        Surface {
            SettingsNominalContent(
                viewState = viewState,
                uiArrangement = previewUiArrangement,
            )
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
            User(123L, "User 1", selected = true),
            User(234L, "User pouet 2", selected = false),
            User(345L, "User ping 3", selected = true),
            User(345L, "User 4 has a very long name", selected = true),
            User(123L, "User tralala 5", selected = true),
            User(234L, "User tudut 6", selected = false),
            User(345L, "User toto 7", selected = true),
            User(345L, "UserWithAVeryLongNameIndeed 8", selected = true),
            User(345L, "UserWithQuiteALongName 9", selected = true),
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
                    currentTheme = AppTheme.FOLLOW_SYSTEM,
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
                    beepSoundCountDownActive = true,
                    sessionStartCountDownLengthAsSeconds = "20",
                    periodsStartCountDownLengthAsSeconds = "5",
                    users = listOfMoreUser,
                    exerciseTypes = exerciseTypeSelectedMixed,
                    currentLanguage = AppLanguage.SWEDISH,
                    currentTheme = AppTheme.FOLLOW_SYSTEM,
                ),
            )
}

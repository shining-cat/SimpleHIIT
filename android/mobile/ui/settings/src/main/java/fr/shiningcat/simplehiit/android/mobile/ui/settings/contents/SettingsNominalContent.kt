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
import androidx.compose.material3.TextButton
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
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
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.android.mobile.ui.settings.SettingsViewState
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsExercisesSelectedComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsFieldComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsToggleComponent
import fr.shiningcat.simplehiit.android.mobile.ui.settings.components.SettingsUsersComponent
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User

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
        HorizontalDivider(
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(vertical = dimensionResource(R.dimen.spacing_1)),
            thickness = Dp.Hairline,
            color = DividerDefaults.color,
        )
        TextButton(
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

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun SettingsNominalContentPreview(
    @PreviewParameter(SettingsNominalContentPreviewParameterProvider::class) viewState: SettingsViewState.Nominal,
) {
    val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
    val previewUiArrangement: UiArrangement =
        if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
            UiArrangement.HORIZONTAL
        } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
            if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                UiArrangement.HORIZONTAL
            } else {
                UiArrangement.VERTICAL // typically, a phone or tablet in portrait
            }
        }
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

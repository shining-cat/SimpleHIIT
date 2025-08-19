package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.common.ui.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants

@Composable
fun SettingsEditNumberCyclesDialog(
    saveNumber: (String) -> Unit,
    validateNumberCyclesInput: (String) -> Constants.InputError,
    numberOfCycles: String,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.number_of_cycle_setting_title),
            inputFieldValue = numberOfCycles,
            inputFieldPostfix = stringResource(id = R.string.number_of_exercises_per_cycle),
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.SMALL,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveNumber(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            keyboardType = KeyboardType.Number,
            validateInput = validateNumberCyclesInput,
            pickErrorMessage = { setNumberCyclesErrorMessage(it) },
        )
    }
}

private fun setNumberCyclesErrorMessage(error: Constants.InputError): Int =
    when (error) {
        Constants.InputError.NONE -> -1
        else -> R.string.invalid_input_error
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsEditNumberCyclesDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsEditNumberCyclesDialog(
            saveNumber = {},
            validateNumberCyclesInput = { _ -> Constants.InputError.NONE },
            numberOfCycles = "5",
            onCancel = {},
        )
    }
}

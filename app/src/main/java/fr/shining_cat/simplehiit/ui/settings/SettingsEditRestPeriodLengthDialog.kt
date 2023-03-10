package fr.shining_cat.simplehiit.ui.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.ui.components.InputDialog
import fr.shining_cat.simplehiit.ui.components.InputDialogTextFieldSize

@Composable
fun SettingsEditRestPeriodLengthDialog(
    saveRestPeriodLength: (String) -> Unit,
    validateRestPeriodLengthInput: (String) -> Constants.InputError,
    restPeriodLengthSeconds: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.rest_period_length_label),
            inputFieldValue = restPeriodLengthSeconds,
            inputFieldPostfix = stringResource(id = R.string.seconds),
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.SMALL,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveRestPeriodLength(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            keyboardType = KeyboardType.Number,
            validateInput = validateRestPeriodLengthInput,
            pickErrorMessage = { setInputPeriodLengthErrorMessage(it) }
        )
    }
}

private fun setInputPeriodLengthErrorMessage(error: Constants.InputError): Int {
    return when (error) {
        Constants.InputError.NONE -> -1
        Constants.InputError.VALUE_TOO_SMALL -> R.string.period_length_too_short_constraint
        else -> R.string.invalid_input_error
    }
}
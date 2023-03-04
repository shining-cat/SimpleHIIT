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
fun SettingsContentInputNumberCyclesDialog(
    saveNumber: (String) -> Unit,
    validateNumberCyclesInput: (String) -> Constants.InputError,
    numberOfCycles: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
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
            pickErrorMessage = { setNumberCyclesErrorMessage(it) }
        )
    }
}

private fun setNumberCyclesErrorMessage(error: Constants.InputError): Int{
    return when(error){
        Constants.InputError.NONE -> -1
        else -> R.string.invalid_input_error
    }
}
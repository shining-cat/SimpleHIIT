package fr.shining_cat.simplehiit.ui.home

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
fun HomeContentInputNumberCyclesDialog(
    saveInputNumberCycles: (String) -> Unit,
    validateInputNumberCycles: (String) -> Constants.InputError,
    numberOfCycles: Int,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.input_number_cycles_dialog_title),
            inputFieldValue = numberOfCycles.toString(),
            inputFieldPostfix = stringResource(id = R.string.input_number_cycles_dialog_postfix),
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.SMALL,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveInputNumberCycles(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            keyboardType = KeyboardType.Number,
            validateInput = validateInputNumberCycles,
            pickErrorMessage = { setErrorMessage(it) }
        )
    }
}

private fun setErrorMessage(error: Constants.InputError): Int{
    return when(error){
        Constants.InputError.NONE -> -1
        else -> R.string.invalid_input_error
    }
}

package fr.shining_cat.simplehiit.android.mobile.ui.home.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.android.mobile.common.components.InputDialog
import fr.shining_cat.simplehiit.android.mobile.common.components.InputDialogTextFieldSize
import fr.shining_cat.simplehiit.android.mobile.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.domain.common.Constants

@Composable
fun HomeInputNumberCyclesDialog(
    saveInputNumberCycles: (String) -> Unit,
    validateInputNumberCycles: (String) -> fr.shining_cat.simplehiit.domain.common.Constants.InputError,
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

private fun setErrorMessage(error: fr.shining_cat.simplehiit.domain.common.Constants.InputError): Int {
    return when (error) {
        fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE -> -1
        else -> R.string.invalid_input_error
    }
}

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.PIXEL_4,
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@Composable
private fun HomeInputNumberCyclesDialogPreview() {
    SimpleHiitTheme {
        HomeInputNumberCyclesDialog(
            saveInputNumberCycles = {},
            validateInputNumberCycles = { _ -> Constants.InputError.TOO_LONG },
            numberOfCycles = 3000,
            onCancel = {}
        )
    }
}

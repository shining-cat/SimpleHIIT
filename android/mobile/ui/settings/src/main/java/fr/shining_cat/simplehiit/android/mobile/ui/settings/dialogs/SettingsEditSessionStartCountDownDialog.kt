package fr.shining_cat.simplehiit.android.mobile.ui.settings.dialogs

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
fun SettingsEditSessionStartCountDownDialog(
    saveCountDownLength: (String) -> Unit,
    validateCountDownLengthInput: (String) -> Constants.InputError,
    countDownLengthSeconds: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.session_start_countdown_length_setting_label),
            inputFieldValue = countDownLengthSeconds,
            inputFieldPostfix = stringResource(id = R.string.seconds),
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.SMALL,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveCountDownLength(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            keyboardType = KeyboardType.Number,
            validateInput = validateCountDownLengthInput,
            pickErrorMessage = { setInputSessionCountDownLengthErrorMessage(it) }
        )
    }
}

private fun setInputSessionCountDownLengthErrorMessage(error: Constants.InputError): Int {
    return when (error) {
        Constants.InputError.NONE -> -1
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
private fun SettingsEditSessionStartCountDownDialogPreview() {
    SimpleHiitTheme {
        SettingsEditSessionStartCountDownDialog(
            saveCountDownLength = {},
            validateCountDownLengthInput = { _ -> Constants.InputError.NONE },
            countDownLengthSeconds = "5",
            onCancel = {}
        )
    }
}

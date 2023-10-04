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
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shining_cat.simplehiit.android.mobile.ui.settings.components.InputDialog
import fr.shining_cat.simplehiit.android.mobile.ui.settings.components.InputDialogTextFieldSize
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.Constants

@Composable
fun SettingsEditPeriodLengthDialog(
    dialogTitle: String,
    savePeriodLength: (String) -> Unit,
    validatePeriodLengthInput: (String) -> Constants.InputError,
    periodLengthSeconds: String,
    onCancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        InputDialog(
            dialogTitle = dialogTitle,
            inputFieldValue = periodLengthSeconds,
            inputFieldPostfix = stringResource(id = R.string.seconds),
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.SMALL,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { savePeriodLength(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            keyboardType = KeyboardType.Number,
            validateInput = validatePeriodLengthInput,
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
private fun SettingsEditPeriodLengthDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsEditPeriodLengthDialog(
            dialogTitle = "Some period length",
            savePeriodLength = {},
            validatePeriodLengthInput = { _ -> Constants.InputError.NONE },
            periodLengthSeconds = "15",
            onCancel = {}
        )
    }
}

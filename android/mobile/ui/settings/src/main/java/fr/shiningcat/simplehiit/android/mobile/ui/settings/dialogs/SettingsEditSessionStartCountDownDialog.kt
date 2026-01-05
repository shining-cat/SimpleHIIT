package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.InputError

@Composable
fun SettingsEditSessionStartCountDownDialog(
    saveCountDownLength: (String) -> Unit,
    validateCountDownLengthInput: (String) -> InputError,
    countDownLengthSeconds: String,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxWidth(),
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
            pickErrorMessage = { setInputSessionCountDownLengthErrorMessage(it) },
        )
    }
}

private fun setInputSessionCountDownLengthErrorMessage(error: InputError): Int =
    when (error) {
        InputError.NONE -> -1
        else -> R.string.invalid_input_error
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsEditSessionStartCountDownDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsEditSessionStartCountDownDialog(
            saveCountDownLength = {},
            validateCountDownLengthInput = { _ -> InputError.NONE },
            countDownLengthSeconds = "5",
            onCancel = {},
        )
    }
}

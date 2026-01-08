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
fun SettingsEditPeriodLengthDialog(
    dialogTitle: String,
    savePeriodLength: (String) -> Unit,
    validatePeriodLengthInput: (String) -> InputError?,
    periodLengthSeconds: String,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxWidth(),
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
            pickErrorMessage = ::setInputPeriodLengthErrorMessage,
        )
    }
}

private fun setInputPeriodLengthErrorMessage(error: InputError?): Int? =
    when (error) {
        null -> null
        InputError.VALUE_TOO_SMALL -> R.string.period_length_too_short_constraint
        else -> R.string.invalid_input_error
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsEditPeriodLengthDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsEditPeriodLengthDialog(
            dialogTitle = "Some period length",
            savePeriodLength = {},
            validatePeriodLengthInput = { _ -> null },
            periodLengthSeconds = "15",
            onCancel = {},
        )
    }
}

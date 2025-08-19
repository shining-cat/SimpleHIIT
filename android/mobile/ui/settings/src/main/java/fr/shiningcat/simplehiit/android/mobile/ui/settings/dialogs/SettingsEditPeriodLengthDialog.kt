package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Devices.DESKTOP
import androidx.compose.ui.tooling.preview.Devices.FOLDABLE
import androidx.compose.ui.tooling.preview.Devices.PHONE
import androidx.compose.ui.tooling.preview.Devices.TABLET
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants

@Composable
fun SettingsEditPeriodLengthDialog(
    dialogTitle: String,
    savePeriodLength: (String) -> Unit,
    validatePeriodLengthInput: (String) -> Constants.InputError,
    periodLengthSeconds: String,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
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
            pickErrorMessage = { setInputPeriodLengthErrorMessage(it) },
        )
    }
}

private fun setInputPeriodLengthErrorMessage(error: Constants.InputError): Int =
    when (error) {
        Constants.InputError.NONE -> -1
        Constants.InputError.VALUE_TOO_SMALL -> R.string.period_length_too_short_constraint
        else -> R.string.invalid_input_error
    }

// Previews
// we add each device preview manually here instead of using PreviewScreenSizes
// because we want no showSystemUi which overlaps the system status bar to the dialog preview
@PreviewLightDark
@PreviewFontScale
@Preview(name = "Phone", device = PHONE, showSystemUi = false)
@Preview(
    name = "Phone - Landscape",
    device = "spec:width=411dp,height=891dp,orientation=landscape,dpi=420",
    showSystemUi = false,
)
@Preview(name = "Unfolded Foldable", device = FOLDABLE, showSystemUi = false)
@Preview(name = "Tablet", device = TABLET, showSystemUi = false)
@Preview(name = "Desktop", device = DESKTOP, showSystemUi = false)
@Composable
private fun SettingsEditPeriodLengthDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsEditPeriodLengthDialog(
            dialogTitle = "Some period length",
            savePeriodLength = {},
            validatePeriodLengthInput = { _ -> Constants.InputError.NONE },
            periodLengthSeconds = "15",
            onCancel = {},
        )
    }
}

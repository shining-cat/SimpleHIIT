package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
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
fun SettingsAddUserDialog(
    saveUserName: (String) -> Unit,
    userName: String,
    validateUserNameInput: (String) -> Constants.InputError,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.create_user_dialog_title),
            inputFieldValue = userName,
            inputFieldPostfix = "",
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.LARGE,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveUserName(it) },
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            validateInput = validateUserNameInput,
            pickErrorMessage = { setUSerNameErrorMessage(it) },
        )
    }
}

private fun setUSerNameErrorMessage(error: Constants.InputError): Int =
    when (error) {
        Constants.InputError.TOO_LONG -> R.string.user_name_too_long_error
        Constants.InputError.VALUE_ALREADY_TAKEN -> R.string.user_name_taken_error
        else -> -1
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
private fun SettingsAddUserDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsAddUserDialog(
            saveUserName = {},
            userName = "The User's name",
            validateUserNameInput = { _ -> Constants.InputError.NONE },
            onCancel = {},
        )
    }
}

package fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import fr.shiningcat.simplehiit.android.tv.ui.common.components.DialogInput
import fr.shiningcat.simplehiit.android.tv.ui.common.components.InputDialogTextFieldSize
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
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
                .padding(dimensionResource(R.dimen.spacing_1))
                .fillMaxWidth(),
    ) {
        DialogInput(
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
        Constants.InputError.VALUE_EMPTY -> R.string.user_name_empty_error
        Constants.InputError.TOO_LONG -> R.string.user_name_too_long_error
        Constants.InputError.VALUE_ALREADY_TAKEN -> R.string.user_name_taken_error
        else -> -1
    }

// Previews
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showBackground = true,
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun SettingsAddUserDialogPreview() {
    SimpleHiitTvTheme {
        SettingsAddUserDialog(
            saveUserName = {},
            userName = "The User's name",
            validateUserNameInput = { _ -> Constants.InputError.NONE },
            onCancel = {},
        )
    }
}

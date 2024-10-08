package fr.shiningcat.simplehiit.android.tv.ui.settings.dialogs

import android.content.res.Configuration
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.InputDialog
import fr.shiningcat.simplehiit.android.tv.ui.settings.components.InputDialogTextFieldSize
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants

@Composable
fun SettingsEditUserDialog(
    saveUserName: (String) -> Unit,
    deleteUser: () -> Unit,
    validateUserNameInput: (String) -> Constants.InputError,
    userName: String,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(8.dp)
                .fillMaxWidth(),
    ) {
        InputDialog(
            dialogTitle = stringResource(id = R.string.edit_user_dialog_title),
            inputFieldValue = userName,
            inputFieldPostfix = "",
            inputFieldSingleLine = true,
            inputFieldSize = InputDialogTextFieldSize.LARGE,
            primaryButtonLabel = stringResource(id = R.string.save_settings_button_label),
            primaryAction = { saveUserName(it) },
            secondaryButtonLabel = stringResource(id = R.string.delete_button_label),
            secondaryAction = deleteUser,
            dismissButtonLabel = stringResource(id = R.string.cancel_button_label),
            dismissAction = onCancel,
            validateInput = validateUserNameInput,
            pickErrorMessage = { setUserNameErrorMessage(it) },
        )
    }
}

private fun setUserNameErrorMessage(error: Constants.InputError): Int =
    when (error) {
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
private fun SettingsEditUserDialogPreview() {
    SimpleHiitTvTheme {
        SettingsEditUserDialog(
            saveUserName = {},
            deleteUser = {},
            validateUserNameInput = { _ -> Constants.InputError.NONE },
            userName = "The user's name",
            onCancel = {},
        )
    }
}

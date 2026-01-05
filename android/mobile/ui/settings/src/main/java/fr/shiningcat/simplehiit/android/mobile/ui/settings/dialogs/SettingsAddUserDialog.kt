package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import fr.shiningcat.simplehiit.android.mobile.ui.common.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.models.InputError

@Composable
fun SettingsAddUserDialog(
    saveUserName: (String) -> Unit,
    userName: String,
    validateUserNameInput: (String) -> InputError,
    onCancel: () -> Unit,
) {
    Column(
        modifier =
            Modifier
                .padding(dimensionResource(R.dimen.spacing_1))
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

private fun setUSerNameErrorMessage(error: InputError): Int =
    when (error) {
        InputError.VALUE_EMPTY -> R.string.user_name_empty_error
        InputError.TOO_LONG -> R.string.user_name_too_long_error
        InputError.VALUE_ALREADY_TAKEN -> R.string.user_name_taken_error
        else -> -1
    }

// Previews
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun SettingsAddUserDialogPreview() {
    SimpleHiitMobileTheme {
        SettingsAddUserDialog(
            saveUserName = {},
            userName = "The User's name",
            validateUserNameInput = { _ -> InputError.NONE },
            onCancel = {},
        )
    }
}

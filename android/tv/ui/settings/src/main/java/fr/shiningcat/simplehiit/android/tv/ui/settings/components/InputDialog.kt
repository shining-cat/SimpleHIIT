package fr.shiningcat.simplehiit.android.tv.ui.settings.components

import android.content.res.Configuration
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants

enum class InputDialogTextFieldSize(
    val width: Dp,
) {
    SMALL(56.dp),
    MEDIUM(112.dp),
    LARGE(224.dp),
}

/**
 *
 */
@Composable
fun InputDialog(
    dialogTitle: String = "",
    inputFieldValue: String,
    inputFieldPostfix: String,
    inputFieldSingleLine: Boolean = true,
    inputFieldSize: InputDialogTextFieldSize = InputDialogTextFieldSize.MEDIUM,
    primaryButtonLabel: String,
    primaryAction: (String) -> Unit,
    secondaryButtonLabel: String = "",
    secondaryAction: () -> Unit = {},
    dismissButtonLabel: String = "",
    dismissAction: () -> Unit,
    keyboardType: KeyboardType = KeyboardOptions.Default.keyboardType,
    validateInput: (String) -> Constants.InputError = { Constants.InputError.NONE },
    pickErrorMessage: (Constants.InputError) -> Int = { -1 },
) {
    val input = rememberSaveable { mutableStateOf(inputFieldValue) }
    val isError =
        rememberSaveable { mutableStateOf(validateInput(inputFieldValue) != Constants.InputError.NONE) }
    val errorMessageStringRes =
        rememberSaveable { mutableStateOf(pickErrorMessage(validateInput(inputFieldValue))) }
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) { focusRequester.requestFocus() }

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            val dialogPadding = 8.dp
            val internalPadding = 8.dp
            Column(
                modifier =
                    Modifier
                        .padding(dialogPadding)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState()),
            ) {
                if (dialogTitle.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Left,
                        text = dialogTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                    )
                }
                Row(
                    Modifier
                        .padding(horizontal = internalPadding, vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    BasicTextField(
                        value = input.value,
                        singleLine = inputFieldSingleLine,
                        onValueChange = {
                            input.value = it
                            val validationResult = validateInput(it)
                            val errorStringRes = pickErrorMessage(validationResult)
                            isError.value =
                                validationResult != Constants.InputError.NONE // updating the error state Boolean
                            errorMessageStringRes.value =
                                errorStringRes // updating the eventual error message String resource pointer
                        },
                        keyboardOptions =
                            KeyboardOptions(
                                keyboardType = keyboardType,
                                imeAction = ImeAction.Done,
                            ),
                        keyboardActions =
                            KeyboardActions(onDone = {
                                if (!isError.value) primaryAction(input.value)
                            }),
                        modifier =
                            Modifier
                                .width(inputFieldSize.width)
                                .alignByBaseline()
                                .focusRequester(focusRequester),
                        decorationBox = {
                            InputDialogDecoration(
                                innerTextField = it,
                                isError = isError.value,
                                inputFieldSize = inputFieldSize,
                                errorMessageStringRes = errorMessageStringRes.value,
                            )
                        },
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = inputFieldPostfix,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }
                if (isError.value && errorMessageStringRes.value != -1) {
                    Text(
                        text = stringResource(id = errorMessageStringRes.value),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        textAlign = TextAlign.Center,
                        modifier =
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = internalPadding), // the error message needs all the room available so it won't follow the input row constraints
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.spacedBy(24.dp),
                ) {
                    if (secondaryButtonLabel.isNotBlank()) {
                        ButtonText(
                            modifier = Modifier.height(48.dp).weight(1f),
                            onClick = secondaryAction,
                            label = secondaryButtonLabel,
                        )
                    }
                    if (dismissButtonLabel.isNotBlank()) {
                        ButtonBordered(
                            modifier = Modifier.height(48.dp).weight(1f),
                            onClick = dismissAction,
                            label = dismissButtonLabel,
                        )
                    }
                    ButtonFilled(
                        modifier = Modifier.height(48.dp).weight(1f),
                        onClick = { if (!isError.value) primaryAction(input.value) },
                        label = primaryButtonLabel,
                    )
                }
            }
        }
    }
}

@Composable
fun InputDialogDecoration(
    innerTextField: @Composable () -> Unit,
    isError: Boolean,
    inputFieldSize: InputDialogTextFieldSize,
    errorMessageStringRes: Int,
) {
    Row(
        modifier =
            Modifier
                .border(
                    border =
                        BorderStroke(
                            width = 2.dp,
                            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.border,
                        ),
                    shape = MaterialTheme.shapes.small,
                ).padding(16.dp),
    ) {
        innerTextField()
        errorTrailingIcon(
            isError = isError,
            inputFieldSize = inputFieldSize,
            errorMessageStringRes = errorMessageStringRes,
        )
    }
}

@Composable
fun errorTrailingIcon(
    isError: Boolean,
    inputFieldSize: InputDialogTextFieldSize,
    errorMessageStringRes: Int,
): @Composable (() -> Unit)? {
    // small input field can not fit the trailing icon plus content
    return if (isError && inputFieldSize != InputDialogTextFieldSize.SMALL) {
        {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(id = errorMessageStringRes),
                tint = MaterialTheme.colorScheme.error,
            )
        }
    } else {
        null
    }
}

// Previews
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_NO,
)
@Preview(
    showSystemUi = true,
    device = Devices.TV_1080p,
    uiMode = Configuration.UI_MODE_NIGHT_YES,
)
@Composable
private fun InputDialogPreview(
    @PreviewParameter(InputDialogPreviewParameterProvider::class) inputDialogPreviewObject: InputDialogPreviewObject,
) {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            InputDialog(
                dialogTitle = "Duration of WORK periods",
                inputFieldValue = inputDialogPreviewObject.inputFieldValue,
                inputFieldPostfix = inputDialogPreviewObject.postfix,
                inputFieldSize = inputDialogPreviewObject.inputFieldSize,
                inputFieldSingleLine = inputDialogPreviewObject.singleLine,
                primaryButtonLabel = inputDialogPreviewObject.primaryButtonLabel,
                primaryAction = {},
                secondaryButtonLabel = inputDialogPreviewObject.secondaryButtonLabel,
                secondaryAction = {},
                dismissButtonLabel = inputDialogPreviewObject.dismissButtonLabel,
                dismissAction = {},
                keyboardType = KeyboardType.Number,
                validateInput = inputDialogPreviewObject.validateInput,
                pickErrorMessage = inputDialogPreviewObject.errorMessage,
            )
        }
    }
}

internal class InputDialogPreviewParameterProvider : PreviewParameterProvider<InputDialogPreviewObject> {
    override val values: Sequence<InputDialogPreviewObject>
        get() {
            val sequenceOf =
                sequenceOf(
                    InputDialogPreviewObject(
                        inputFieldValue = "30",
                        singleLine = true,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "",
                        dismissButtonLabel = "",
                        inputFieldSize = InputDialogTextFieldSize.SMALL,
                        validateInput = { Constants.InputError.NONE },
                        errorMessage = { -1 },
                    ),
                    InputDialogPreviewObject(
                        inputFieldValue = "Quatre-vingt",
                        singleLine = true,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.MEDIUM,
                        validateInput = { Constants.InputError.NONE },
                        errorMessage = { -1 },
                    ),
                    InputDialogPreviewObject(
                        inputFieldValue = "This is a very long input value so it takes a lot of place",
                        singleLine = true,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "Delete",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.LARGE,
                        validateInput = { Constants.InputError.NONE },
                        errorMessage = { -1 },
                    ),
                    InputDialogPreviewObject(
                        inputFieldValue = "This is a very long input value so it takes a lot of place",
                        singleLine = false,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "Delete",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.LARGE,
                        validateInput = { Constants.InputError.NONE },
                        errorMessage = { -1 },
                    ),
                    InputDialogPreviewObject(
                        inputFieldValue = "This is a very long input value so it takes a lot of place, and it could grow indefinitely depending only on the user's choice, so we need to be able to display this correctly on screen",
                        singleLine = false,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "Delete",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.LARGE,
                        validateInput = { Constants.InputError.WRONG_FORMAT },
                        errorMessage = { R.string.invalid_input_error },
                    ),
                    InputDialogPreviewObject(
                        inputFieldValue = "30",
                        singleLine = true,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "Delete",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.SMALL,
                        validateInput = { Constants.InputError.WRONG_FORMAT },
                        errorMessage = { R.string.invalid_input_error },
                    ),
                )
            return sequenceOf
        }
}

internal data class InputDialogPreviewObject(
    val singleLine: Boolean,
    val inputFieldValue: String,
    val postfix: String,
    val primaryButtonLabel: String,
    val secondaryButtonLabel: String,
    val dismissButtonLabel: String,
    val inputFieldSize: InputDialogTextFieldSize,
    val validateInput: (String) -> Constants.InputError,
    val errorMessage: (Constants.InputError) -> Int,
)

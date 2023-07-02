package fr.shining_cat.simplehiit.android.mobile.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonresources.R
import fr.shining_cat.simplehiit.domain.common.Constants

enum class InputDialogTextFieldSize(val width: Dp) {
    SMALL(56.dp), MEDIUM(112.dp), LARGE(224.dp)
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
    validateInput: (String) -> fr.shining_cat.simplehiit.domain.common.Constants.InputError = { fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE },
    pickErrorMessage: (fr.shining_cat.simplehiit.domain.common.Constants.InputError) -> Int = { -1 }
) {

    //TODO: auto-focus on input field when opening dialog

    //TODO: open keyboard when auto-focusing

    //TODO: set cursor position at end of inputFieldValue in input field when auto-focusing. Only affect this position once on dialog opening

    val input = rememberSaveable { mutableStateOf(inputFieldValue) }
    val isError = rememberSaveable() { mutableStateOf(validateInput(inputFieldValue) != fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE) }
    val errorMessageStringRes = rememberSaveable {mutableStateOf(pickErrorMessage(validateInput(inputFieldValue)))}

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            val dialogPadding = 8.dp
            val internalPadding = 8.dp
            Column(
                modifier = Modifier
                    .padding(dialogPadding)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState())
            ) {
                if (dialogTitle.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Left,
                        text = dialogTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    Modifier
                        .padding(horizontal = internalPadding, vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = input.value,
                        singleLine = inputFieldSingleLine,
                        onValueChange = {
                            input.value = it
                            val validationResult = validateInput(it)
                            val errorStringRes = pickErrorMessage(validationResult)
                            isError.value = validationResult != fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE // updating the error state Boolean
                            errorMessageStringRes.value = errorStringRes // updating the eventual error message String resource pointer
                        },
                        isError = isError.value,
                        trailingIcon = errorTrailingIcon(
                            isError.value,
                            inputFieldSize,
                            errorMessageStringRes.value
                        ),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            if (!isError.value) primaryAction(input.value)
                        }),
                        modifier = Modifier
                            //.weight(0.3F, false)
                            .width(inputFieldSize.width)
                            .alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = inputFieldPostfix,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                if (isError.value && errorMessageStringRes.value != -1) {
                    Text(
                        text = stringResource(id = errorMessageStringRes.value),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = internalPadding) //the error message needs all the room available so it won't follow the input row constraints
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    if (secondaryButtonLabel.isNotBlank()) {
                        TextButton(onClick = secondaryAction) {
                            Text(text = secondaryButtonLabel)
                        }
                    }
                    if (dismissButtonLabel.isNotBlank()) {
                        OutlinedButton(onClick = dismissAction) {
                            Text(text = dismissButtonLabel)
                        }
                    }
                    Button(onClick = { if (!isError.value) primaryAction(input.value) }) {
                        Text(text = primaryButtonLabel)
                    }
                }
            }
        }
    }
}

@Composable
fun errorTrailingIcon(
    isError: Boolean,
    inputFieldSize: InputDialogTextFieldSize,
    errorMessageStringRes: Int
): @Composable (() -> Unit)? {
    // small input field can not fit the trailing icon plus content
    return if (isError && inputFieldSize != InputDialogTextFieldSize.SMALL) {
        {
            Icon(
                imageVector = Icons.Filled.Info,
                contentDescription = stringResource(id = errorMessageStringRes),
                tint = MaterialTheme.colorScheme.error
            )
        }
    } else null
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
private fun ChoiceDialogPreview(
    @PreviewParameter(ChoiceDialogPreviewParameterProvider::class) inputDialogPreviewObject: InputDialogPreviewObject
) {
    SimpleHiitTheme {
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

internal class ChoiceDialogPreviewParameterProvider :
    PreviewParameterProvider<InputDialogPreviewObject> {
    override val values: Sequence<InputDialogPreviewObject>
        get() {
            val sequenceOf = sequenceOf(
                InputDialogPreviewObject(
                    inputFieldValue = "30",
                    singleLine = true,
                    postfix = "seconds",
                    primaryButtonLabel = "Save",
                    secondaryButtonLabel = "",
                    dismissButtonLabel = "",
                    inputFieldSize = InputDialogTextFieldSize.SMALL,
                    validateInput = { Constants.InputError.NONE },
                    errorMessage = { -1 }
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
                    errorMessage = { -1 }
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
                    errorMessage = { -1 }
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
                    errorMessage = { -1 }
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
                    errorMessage = { R.string.invalid_input_error }
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
                    errorMessage = { R.string.invalid_input_error }
                ),
            )
            return sequenceOf
        }
}

internal data class InputDialogPreviewObject(
    val singleLine: Boolean,
    val inputFieldValue:String,
    val postfix: String,
    val primaryButtonLabel: String,
    val secondaryButtonLabel: String,
    val dismissButtonLabel: String,
    val inputFieldSize: InputDialogTextFieldSize,
    val validateInput: (String) -> fr.shining_cat.simplehiit.domain.common.Constants.InputError,
    val errorMessage: (fr.shining_cat.simplehiit.domain.common.Constants.InputError) -> Int
)
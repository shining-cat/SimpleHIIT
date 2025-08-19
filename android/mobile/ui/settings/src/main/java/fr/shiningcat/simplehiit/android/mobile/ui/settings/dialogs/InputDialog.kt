package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Arrangement.Absolute.spacedBy
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.tooling.preview.PreviewScreenSizes
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.utils.ButtonLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.ButtonType
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.buttonsFitOnOneLine
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
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
    // TODO: auto-focus on input field when opening dialog

    // TODO: open keyboard when auto-focusing

    // TODO: set cursor position at end of inputFieldValue in input field when auto-focusing. Only affect this position once on dialog opening

    val input = rememberSaveable { mutableStateOf(inputFieldValue) }
    val isError =
        rememberSaveable { mutableStateOf(validateInput(inputFieldValue) != Constants.InputError.NONE) }
    val errorMessageStringRes =
        rememberSaveable { mutableIntStateOf(pickErrorMessage(validateInput(inputFieldValue))) }

    val primaryButton =
        ButtonLayoutInfo(
            label = primaryButtonLabel,
            style = MaterialTheme.typography.labelMedium,
            type = ButtonType.FILLED,
        )
    val secondaryButton =
        if (secondaryButtonLabel.isNotBlank()) {
            ButtonLayoutInfo(
                label = secondaryButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.OUTLINED,
            )
        } else {
            null
        }
    val dismissButton =
        if (dismissButtonLabel.isNotBlank()) {
            ButtonLayoutInfo(
                label = dismissButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.OUTLINED,
            )
        } else {
            null
        }
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
        ) {
            val dialogPadding = 8.dp
            // wrapping the whole dialog in a BoxWithConstraints enables retrieval of its actual width
            // at runtime and in previews to adapt the layout to fontscale and available space
            BoxWithConstraints {
                val density = LocalDensity.current
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding
                val effectiveDialogContentWidthPx =
                    with(density) { effectiveDialogContentWidthDp.toPx() }.toInt()

                Column(
                    modifier =
                        Modifier
                            .padding(dialogPadding)
                            .verticalScroll(rememberScrollState()),
                ) {
                    // TITLE
                    if (dialogTitle.isNotBlank()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = dialogTitle,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    // BODY
                    // Do the input field and the postfix fit in one line?
                    val inputFieldPostfixLayout =
                        TextLayoutInfo(
                            text = inputFieldPostfix,
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    val inputFieldWidthPx = with(density) { inputFieldSize.width.toPx() }.toInt()
                    val inputSpacing = 12.dp
                    val inputSpacingPx = with(density) { inputSpacing.toPx() }.toInt()
                    val dialogBodyPaddingDp = 8.dp
                    val dialogBodyPaddingPx = with(density) { dialogBodyPaddingDp.toPx() }.toInt()
                    // Calculate available width for postfix, considering everything
                    val inputFieldPostfixLayoutAvailableWidth =
                        effectiveDialogContentWidthPx - 2 * dialogBodyPaddingPx - inputFieldWidthPx - inputSpacingPx
                    val fieldAndPostfixFitOneLine =
                        fitsOnXLines(
                            textLayoutInfo = inputFieldPostfixLayout,
                            numberOfLines = 1,
                            availableWidthPx = inputFieldPostfixLayoutAvailableWidth,
                        )
                    //
                    Row(
                        Modifier
                            .padding(
                                start = dialogBodyPaddingDp,
                                end = dialogBodyPaddingDp,
                                top = 24.dp,
                                bottom = 8.dp,
                            ).align(Alignment.CenterHorizontally),
                        horizontalArrangement = Arrangement.spacedBy(inputSpacing),
                    ) {
                        OutlinedTextField(
                            value = input.value,
                            singleLine = inputFieldSingleLine,
                            onValueChange = {
                                input.value = it
                                val validationResult = validateInput(it)
                                val errorStringRes = pickErrorMessage(validationResult)
                                isError.value =
                                    validationResult != Constants.InputError.NONE // updating the error state Boolean
                                errorMessageStringRes.intValue =
                                    errorStringRes // updating the eventual error message String resource pointer
                            },
                            isError = isError.value,
                            trailingIcon =
                                errorTrailingIcon(
                                    isError = isError.value,
                                    inputFieldSize = inputFieldSize,
                                    errorMessageStringRes = errorMessageStringRes.intValue,
                                ),
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
                                    .then(
                                        if (fieldAndPostfixFitOneLine) {
                                            Modifier
                                                .width(inputFieldSize.width)
                                                .alignByBaseline()
                                        } else {
                                            Modifier.fillMaxWidth()
                                        },
                                    ),
                        )
                        if (fieldAndPostfixFitOneLine) {
                            Text(
                                modifier = Modifier.alignByBaseline(),
                                text = inputFieldPostfixLayout.text,
                                style = inputFieldPostfixLayout.style,
                            )
                        }
                    }
                    if (!fieldAndPostfixFitOneLine) {
                        Text(
                            modifier =
                                Modifier
                                    .padding(horizontal = dialogBodyPaddingDp)
                                    .fillMaxWidth(),
                            text = inputFieldPostfixLayout.text,
                            style = inputFieldPostfixLayout.style,
                            textAlign = TextAlign.End,
                        )
                    }
                    if (isError.value && errorMessageStringRes.intValue != -1) {
                        Text(
                            text = stringResource(id = errorMessageStringRes.intValue),
                            textAlign = TextAlign.Center,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error,
                            // the error message needs all the room available so it won't follow the input row constraints
                            modifier =
                                Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = dialogBodyPaddingDp),
                        )
                    }
                    val buttons = listOfNotNull(primaryButton, secondaryButton, dismissButton)
                    val buttonsSpacingDp = 12.dp
                    val buttonsFitOnOneLine =
                        buttonsFitOnOneLine(
                            buttons = buttons,
                            availableWidthPx = effectiveDialogContentWidthPx,
                            horizontalSpacingDp = buttonsSpacingDp,
                        )
                    if (buttonsFitOnOneLine) {
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 0.dp, vertical = 24.dp),
                            horizontalArrangement =
                                Arrangement.spacedBy(
                                    space = buttonsSpacingDp,
                                    alignment = Alignment.End,
                                ),
                        ) {
                            if (secondaryButtonLabel.isNotBlank()) {
                                TextButton(onClick = secondaryAction) {
                                    Text(
                                        text = secondaryButtonLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                    )
                                }
                            }
                            if (dismissButtonLabel.isNotBlank()) {
                                OutlinedButton(onClick = dismissAction) {
                                    Text(
                                        text = dismissButtonLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                    )
                                }
                            }
                            Button(onClick = { if (!isError.value) primaryAction(input.value) }) {
                                Text(
                                    text = primaryButtonLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                )
                            }
                        }
                    } else {
                        Column(
                            Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 0.dp, vertical = 24.dp),
                            verticalArrangement = spacedBy(buttonsSpacingDp),
                            horizontalAlignment = Alignment.End,
                        ) {
                            Button(onClick = { if (!isError.value) primaryAction(input.value) }) {
                                Text(
                                    text = primaryButtonLabel,
                                    style = MaterialTheme.typography.labelMedium,
                                    maxLines = 1,
                                )
                            }
                            if (secondaryButtonLabel.isNotBlank()) {
                                TextButton(onClick = secondaryAction) {
                                    Text(
                                        text = secondaryButtonLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                    )
                                }
                            }
                            if (dismissButtonLabel.isNotBlank()) {
                                OutlinedButton(onClick = dismissAction) {
                                    Text(
                                        text = dismissButtonLabel,
                                        style = MaterialTheme.typography.labelMedium,
                                        maxLines = 1,
                                    )
                                }
                            }
                        }
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
    errorMessageStringRes: Int,
): @Composable (() -> Unit)? {
    // small input field can not fit the trailing icon plus content // todo: change this, we want the icon for all field sizes
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
@PreviewLightDark
@PreviewFontScale
@PreviewScreenSizes
@Composable
private fun InputDialogPreviewPhone(
    @PreviewParameter(InputDialogPreviewParameterProvider::class) inputDialogPreviewObject: InputDialogPreviewObject,
) {
    SimpleHiitMobileTheme {
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

internal class InputDialogPreviewParameterProvider : PreviewParameterProvider<InputDialogPreviewObject> {
    override val values: Sequence<InputDialogPreviewObject>
        get() {
            return sequenceOf(
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
                    postfix = "seconds or whatever ...",
                    primaryButtonLabel = "Save",
                    secondaryButtonLabel = "Delete long",
                    dismissButtonLabel = "Cancel even longer",
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
                    inputFieldValue =
                        "This is a very long input value so it takes a lot of place," +
                            " and it could grow indefinitely depending only on the user's choice," +
                            " so we need to be able to display this correctly on screen",
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

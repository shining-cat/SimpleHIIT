package fr.shiningcat.simplehiit.android.mobile.ui.settings.dialogs

import androidx.compose.foundation.layout.Arrangement
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import fr.shiningcat.simplehiit.android.common.ui.previews.PreviewMobileScreensNoUI
import fr.shiningcat.simplehiit.android.common.ui.utils.AdaptiveDialogButtonsLayout
import fr.shiningcat.simplehiit.android.common.ui.utils.ButtonType
import fr.shiningcat.simplehiit.android.common.ui.utils.DialogButtonConfig
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonresources.R
import fr.shiningcat.simplehiit.domain.common.Constants

enum class InputDialogTextFieldSize(
    val charCount: Int,
) {
    SMALL(2),
    MEDIUM(10),
    LARGE(24),
}

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
        rememberSaveable { mutableIntStateOf(pickErrorMessage(validateInput(inputFieldValue))) }

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium,
        ) {
            val dialogPadding = 8.dp
            BoxWithConstraints {
                val density = LocalDensity.current
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding

                Column(
                    modifier =
                        Modifier
                            .padding(dialogPadding)
                            .verticalScroll(rememberScrollState()),
                ) {
                    if (dialogTitle.isNotBlank()) {
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            text = dialogTitle,
                            style = MaterialTheme.typography.headlineSmall,
                        )
                    }
                    InputDialogBodyContent(
                        inputValue = input.value,
                        onInputValueChange = {
                            input.value = it
                            val validationResult = validateInput(it)
                            val errorStringRes = pickErrorMessage(validationResult)
                            isError.value =
                                validationResult != Constants.InputError.NONE
                            errorMessageStringRes.intValue =
                                errorStringRes
                        },
                        isError = isError.value,
                        errorMessageResId = errorMessageStringRes.intValue,
                        onKeyboardDoneAction = {
                            if (!isError.value) primaryAction(input.value)
                        },
                        inputFieldSingleLine = inputFieldSingleLine,
                        inputFieldSize = inputFieldSize,
                        keyboardType = keyboardType,
                        errorTrailingIconComposable = null,
                        /*   errorTrailingIcon(
                               isError = isError.value,
                               errorMessageStringRes = errorMessageStringRes.intValue,
                           ),*/
                        inputFieldPostfixText = inputFieldPostfix,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
                        density = density,
                    )
                    InputDialogButtonsLayout(
                        primaryButtonLabel = primaryButtonLabel,
                        onPrimaryClick = { primaryAction(input.value) },
                        secondaryButtonLabel = secondaryButtonLabel,
                        onSecondaryClick = secondaryAction,
                        dismissButtonLabel = dismissButtonLabel,
                        onDismissClick = dismissAction,
                        isError = isError.value,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
                        density = density,
                    )
                }
            }
        }
    }
}

@Composable
private fun InputDialogBodyContent(
    inputValue: String,
    onInputValueChange: (String) -> Unit,
    isError: Boolean,
    errorMessageResId: Int,
    onKeyboardDoneAction: () -> Unit,
    inputFieldSingleLine: Boolean,
    inputFieldSize: InputDialogTextFieldSize,
    keyboardType: KeyboardType,
    errorTrailingIconComposable: @Composable (() -> Unit)?,
    inputFieldPostfixText: String,
    effectiveDialogContentWidthDp: Dp,
    density: Density,
) {
    val inputSpacing = 12.dp
    val dialogHorizontalPadding = 8.dp

    val inputFieldPostfixStyle = MaterialTheme.typography.bodyMedium
    val inputFieldPostfixLayout =
        TextLayoutInfo(
            text = inputFieldPostfixText,
            style = inputFieldPostfixStyle,
        )
    val textMeasurer = rememberTextMeasurer()
    val textFieldTextStyle = MaterialTheme.typography.bodyLarge // Style for measurement
    val sampleString = "M".repeat(inputFieldSize.charCount)
    val measuredTextOnlyWidthPx =
        textMeasurer.measure(text = sampleString, style = textFieldTextStyle).size.width
    val measuredTextOnlyWidthDp = with(density) { measuredTextOnlyWidthPx.toDp() }
    val textFieldInternalHorizontalPaddingDp = 24.dp // Assumed 12.dp on each side
    val inputFieldWidthDp = measuredTextOnlyWidthDp + textFieldInternalHorizontalPaddingDp
    val inputFieldWidthPx = with(density) { inputFieldWidthDp.toPx() }.toInt()
    val inputSpacingPx = with(density) { inputSpacing.toPx() }.toInt()
    val dialogHorizontalPaddingPx = with(density) { dialogHorizontalPadding.toPx() }.toInt()
    val effectiveDialogContentWidthForBodyPx =
        with(density) { effectiveDialogContentWidthDp.toPx() }.toInt()

    val inputFieldPostfixLayoutAvailableWidth =
        (effectiveDialogContentWidthForBodyPx - 2 * dialogHorizontalPaddingPx) - inputFieldWidthPx - inputSpacingPx

    val fieldAndPostfixFitOneLine =
        fitsOnXLines(
            textLayoutInfo = inputFieldPostfixLayout,
            numberOfLines = 1,
            availableWidthPx = inputFieldPostfixLayoutAvailableWidth,
        )

    Column(Modifier.fillMaxWidth()) {
        Row(
            Modifier
                .padding(
                    start = dialogHorizontalPadding,
                    end = dialogHorizontalPadding,
                    top = 24.dp,
                    bottom = 8.dp,
                ).align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(inputSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = inputValue,
                singleLine = inputFieldSingleLine,
                onValueChange = onInputValueChange,
                isError = isError,
                trailingIcon = errorTrailingIconComposable,
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions = KeyboardActions(onDone = { onKeyboardDoneAction() }),
                modifier =
                    Modifier
                        .then(
                            if (fieldAndPostfixFitOneLine) {
                                Modifier
                                    .width(inputFieldWidthDp)
                                    .alignByBaseline()
                            } else {
                                Modifier.fillMaxWidth()
                            },
                        ),
            )
            if (fieldAndPostfixFitOneLine && inputFieldPostfixText.isNotBlank()) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = inputFieldPostfixLayout.text,
                    style = inputFieldPostfixLayout.style,
                )
            }
        }
        if (!fieldAndPostfixFitOneLine && inputFieldPostfixText.isNotBlank()) {
            Text(
                modifier =
                    Modifier
                        .padding(horizontal = dialogHorizontalPadding)
                        .fillMaxWidth(),
                text = inputFieldPostfixLayout.text,
                style = inputFieldPostfixLayout.style,
                textAlign = TextAlign.End,
            )
        }
        if (isError && errorMessageResId != -1) {
            Text(
                text = stringResource(id = errorMessageResId),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier =
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = dialogHorizontalPadding)
                        .padding(top = 4.dp),
            )
        }
    }
}

@Composable
private fun InputDialogButtonsLayout(
    primaryButtonLabel: String,
    onPrimaryClick: () -> Unit,
    secondaryButtonLabel: String,
    onSecondaryClick: () -> Unit,
    dismissButtonLabel: String,
    onDismissClick: () -> Unit,
    isError: Boolean,
    effectiveDialogContentWidthDp: Dp,
    density: Density,
) {
    val primaryButtonInfo =
        DialogButtonConfig(
            label = primaryButtonLabel,
            style = MaterialTheme.typography.labelMedium,
            type = ButtonType.FILLED,
            enabled = !isError,
            onClick = onPrimaryClick,
        )
    val secondaryButtonInfo =
        if (secondaryButtonLabel.isNotBlank()) {
            DialogButtonConfig(
                label = secondaryButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.OUTLINED,
                onClick = onSecondaryClick,
            )
        } else {
            null
        }
    val dismissButtonInfo =
        if (dismissButtonLabel.isNotBlank()) {
            DialogButtonConfig(
                label = dismissButtonLabel,
                style = MaterialTheme.typography.labelMedium,
                type = ButtonType.OUTLINED,
                onClick = onDismissClick,
            )
        } else {
            null
        }

    val buttons = listOfNotNull(primaryButtonInfo, secondaryButtonInfo, dismissButtonInfo)
    val buttonsSpacingDp = 12.dp

    AdaptiveDialogButtonsLayout(
        buttons = buttons,
        modifier = Modifier.padding(horizontal = 0.dp, vertical = 24.dp),
        dialogContentWidthDp = effectiveDialogContentWidthDp,
        horizontalSpacingDp = buttonsSpacingDp,
        verticalSpacingDp = buttonsSpacingDp,
    )
}

// Previews
// we add each device preview manually here instead of using PreviewScreenSizes
// because we want no showSystemUi which overlaps the system status bar to the dialog preview
@PreviewLightDark
@PreviewFontScale
@PreviewMobileScreensNoUI
@Composable
private fun InputDialogPreview(
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
                            " and it could grow indefinitely depending only on the user\'s choice," +
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

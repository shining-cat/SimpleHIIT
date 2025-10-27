package fr.shiningcat.simplehiit.android.tv.ui.settings.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
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
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.times
import androidx.compose.ui.window.Dialog
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.common.ui.utils.TextLayoutInfo
import fr.shiningcat.simplehiit.android.common.ui.utils.adaptDpToFontScale
import fr.shiningcat.simplehiit.android.common.ui.utils.fitsOnXLines
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonBordered
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonFilled
import fr.shiningcat.simplehiit.android.tv.ui.common.components.ButtonText
import fr.shiningcat.simplehiit.android.tv.ui.common.previews.PreviewTvScreensNoUi
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.android.tv.ui.settings.R
import fr.shiningcat.simplehiit.domain.common.Constants
import kotlinx.coroutines.delay
import fr.shiningcat.simplehiit.commonresources.R as CommonResourcesR

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
    val dialogPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

    val input =
        rememberSaveable(stateSaver = TextFieldValue.Saver) {
            mutableStateOf(
                TextFieldValue(
                    text = inputFieldValue,
                    selection = TextRange(inputFieldValue.length),
                ),
            )
        }
    val isError =
        rememberSaveable { mutableStateOf(validateInput(inputFieldValue) != Constants.InputError.NONE) }
    val errorMessageStringRes =
        rememberSaveable { mutableIntStateOf(pickErrorMessage(validateInput(inputFieldValue))) }
    val focusRequester = remember { FocusRequester() }
    val keyboardController = LocalSoftwareKeyboardController.current

    LaunchedEffect(Unit) {
        delay(300) // Delay to ensure dialog is fully composed and rendered
        try {
            focusRequester.requestFocus()
            keyboardController?.show()
        } catch (e: Exception) {
            // Focus request may fail if composable is not yet ready
        }
    }

    Dialog(onDismissRequest = dismissAction) {
        Surface(
            shape = MaterialTheme.shapes.medium,
        ) {
            BoxWithConstraints {
                val density = LocalDensity.current
                val dialogAvailableWidthDp = this.maxWidth
                val effectiveDialogContentWidthDp = dialogAvailableWidthDp - 2 * dialogPadding

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
                    InputDialogBodyContent(
                        inputValue = input.value,
                        onInputValueChange = {
                            input.value = it
                            val validationResult = validateInput(it.text)
                            val errorStringRes = pickErrorMessage(validationResult)
                            isError.value =
                                validationResult != Constants.InputError.NONE
                            errorMessageStringRes.intValue =
                                errorStringRes
                        },
                        isError = isError.value,
                        errorMessageResId = errorMessageStringRes.intValue,
                        onKeyboardDoneAction = {
                            if (!isError.value) primaryAction(input.value.text)
                        },
                        inputFieldSingleLine = inputFieldSingleLine,
                        inputFieldSize = inputFieldSize,
                        keyboardType = keyboardType,
                        inputFieldPostfixText = inputFieldPostfix,
                        effectiveDialogContentWidthDp = effectiveDialogContentWidthDp,
                        density = density,
                        focusRequester = focusRequester,
                    )
                    Row(
                        Modifier
                            .fillMaxWidth()
                            .padding(
                                horizontal = 0.dp,
                                vertical = dimensionResource(CommonResourcesR.dimen.spacing_3),
                            ),
                        horizontalArrangement =
                            Arrangement.spacedBy(
                                dimensionResource(
                                    CommonResourcesR.dimen.spacing_3,
                                ),
                            ),
                    ) {
                        if (secondaryButtonLabel.isNotBlank()) {
                            ButtonText(
                                modifier =
                                    Modifier
                                        .height(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                                        .weight(1f),
                                fillWidth = true,
                                fillHeight = true,
                                onClick = secondaryAction,
                                label = secondaryButtonLabel,
                            )
                        }
                        if (dismissButtonLabel.isNotBlank()) {
                            ButtonBordered(
                                modifier =
                                    Modifier
                                        .height(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                                        .weight(1f),
                                fillWidth = true,
                                fillHeight = true,
                                onClick = dismissAction,
                                label = dismissButtonLabel,
                            )
                        }
                        ButtonFilled(
                            modifier =
                                Modifier
                                    .height(adaptDpToFontScale(dimensionResource(R.dimen.button_height)))
                                    .weight(1f),
                            fillHeight = true,
                            fillWidth = true,
                            onClick = { if (isError.value.not()) primaryAction(input.value.text) },
                            label = primaryButtonLabel,
                            enabled = isError.value.not(),
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun InputDialogBodyContent(
    inputValue: TextFieldValue,
    onInputValueChange: (TextFieldValue) -> Unit,
    isError: Boolean,
    errorMessageResId: Int,
    onKeyboardDoneAction: () -> Unit,
    inputFieldSingleLine: Boolean,
    inputFieldSize: InputDialogTextFieldSize,
    keyboardType: KeyboardType,
    inputFieldPostfixText: String,
    effectiveDialogContentWidthDp: Dp,
    density: Density,
    focusRequester: FocusRequester,
) {
    val inputSpacing = dimensionResource(CommonResourcesR.dimen.spacing_15)
    val dialogHorizontalPadding = dimensionResource(CommonResourcesR.dimen.spacing_1)

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
    val textFieldInternalHorizontalPaddingDp =
        dimensionResource(CommonResourcesR.dimen.spacing_3) // Assumed 12.dp on each side
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
                    top = dimensionResource(CommonResourcesR.dimen.spacing_3),
                    bottom = dimensionResource(CommonResourcesR.dimen.spacing_1),
                ).align(Alignment.CenterHorizontally),
            horizontalArrangement = Arrangement.spacedBy(inputSpacing),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            BasicTextField(
                value = inputValue,
                singleLine = inputFieldSingleLine,
                onValueChange = onInputValueChange,
                textStyle = TextStyle(color = MaterialTheme.colorScheme.onSurface),
                keyboardOptions =
                    KeyboardOptions(
                        keyboardType = keyboardType,
                        imeAction = ImeAction.Done,
                    ),
                keyboardActions =
                    KeyboardActions(
                        onDone = {
                            if (isError.not()) onKeyboardDoneAction()
                        },
                    ),
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
                        ).focusRequester(focusRequester),
                decorationBox = {
                    InputDialogDecoration(
                        innerTextField = it,
                        isError = isError,
                        errorMessageStringRes = errorMessageResId,
                    )
                },
            )
            if (fieldAndPostfixFitOneLine && inputFieldPostfixText.isNotBlank()) {
                Text(
                    modifier = Modifier.alignByBaseline(),
                    text = inputFieldPostfixLayout.text,
                    style = inputFieldPostfixLayout.style,
                )
            }
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
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            textAlign = TextAlign.Center,
            // the error message needs all the room available so it won't follow the input row constraints
            modifier =
                Modifier
                    .fillMaxWidth()
                    .padding(horizontal = dialogHorizontalPadding)
                    .padding(top = dimensionResource(CommonResourcesR.dimen.spacing_05)),
        )
    }
}

@Composable
fun InputDialogDecoration(
    innerTextField: @Composable () -> Unit,
    isError: Boolean,
    errorMessageStringRes: Int,
) {
    Row(
        modifier =
            Modifier
                .border(
                    border =
                        BorderStroke(
                            width = dimensionResource(CommonResourcesR.dimen.stroke_025),
                            color = if (isError) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.border,
                        ),
                    shape = MaterialTheme.shapes.small,
                ).padding(dimensionResource(CommonResourcesR.dimen.spacing_2)),
    ) {
        innerTextField()
        errorTrailingIcon(
            isError = isError,
            errorMessageStringRes = errorMessageStringRes,
        )
    }
}

@Composable
fun errorTrailingIcon(
    isError: Boolean,
    errorMessageStringRes: Int,
): @Composable (() -> Unit)? =
    if (isError) {
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

// Previews
@PreviewTvScreensNoUi
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
                        inputFieldValue =
                            "This is a very long input value so it takes a lot of place," +
                                " and it could grow indefinitely depending only on the user's choice, " +
                                "so we need to be able to display this correctly on screen",
                        singleLine = false,
                        postfix = "seconds",
                        primaryButtonLabel = "Save",
                        secondaryButtonLabel = "Delete",
                        dismissButtonLabel = "Cancel",
                        inputFieldSize = InputDialogTextFieldSize.LARGE,
                        validateInput = { Constants.InputError.WRONG_FORMAT },
                        errorMessage = { CommonResourcesR.string.invalid_input_error },
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
                        errorMessage = { CommonResourcesR.string.invalid_input_error },
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

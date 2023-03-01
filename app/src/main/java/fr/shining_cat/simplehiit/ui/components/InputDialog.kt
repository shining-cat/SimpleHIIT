package fr.shining_cat.simplehiit.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.shining_cat.simplehiit.R
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputDialog(
    dialogTitle: String = "",
    inputFieldValue: String,
    inputFieldPostfix: String,
    buttonSaveLabel: String,
    onSave: (String) -> Unit,
    onCancel: () -> Unit,
    keyboardType: KeyboardType = KeyboardOptions.Default.keyboardType,
    validate: (String) -> Boolean = { true },
    errorMessage: String = stringResource(id = R.string.error),
    displayErrorBelow:Boolean = false
) {

    val input = rememberSaveable { mutableStateOf(inputFieldValue) }
    val isError = rememberSaveable() { mutableStateOf(false) }

    Dialog(onDismissRequest = onCancel) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {
                if (dialogTitle.isNotBlank()) {
                    Text(
                        textAlign = TextAlign.Left,
                        modifier = Modifier.padding(horizontal = 0.dp, vertical = 4.dp),
                        text = dialogTitle,
                        style = MaterialTheme.typography.headlineSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
                Row(
                    Modifier
                        .padding(horizontal = 64.dp, vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = input.value,
                        singleLine = true,
                        onValueChange = {
                            input.value = it
                            isError.value = !validate(input.value)
                        },
                        isError = isError.value,
                        trailingIcon = {
                            if (isError.value)
                                Icon(
                                    Icons.Filled.Info,
                                    errorMessage,
                                    tint = MaterialTheme.colorScheme.error
                                )
                        },
                        supportingText = {ErrorText(isError.value, displayErrorBelow, errorMessage) },
                        keyboardOptions = KeyboardOptions(
                            keyboardType = keyboardType,
                            imeAction = ImeAction.Done
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            if (!isError.value) onSave(input.value)
                        }),
                        modifier = Modifier
                            .weight(0.3F, false)
                            .alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        modifier = Modifier.alignByBaseline(),
                        text = inputFieldPostfix,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 24.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedButton(onClick = { onCancel() }) {
                        Text(text = stringResource(id = R.string.cancel_button_label))
                    }
                    Button(onClick = { if(!isError.value) onSave(input.value) }) {
                        Text(text = buttonSaveLabel)
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorText(isError: Boolean, displayErrorBelow: Boolean, errorMessage: String) {
    if (isError && displayErrorBelow) {
        Text(
            text = errorMessage,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.fillMaxWidth()
        )
    }
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
private fun ChoiceDialogPreview() {
    SimpleHiitTheme {
        InputDialog(
            dialogTitle = "Duration of WORK periods",
            inputFieldValue = "30",
            inputFieldPostfix = "seconds",
            buttonSaveLabel = "Save",
            onSave = {},
            onCancel = {},
            KeyboardType.Number,
            { true }
        )
    }
}

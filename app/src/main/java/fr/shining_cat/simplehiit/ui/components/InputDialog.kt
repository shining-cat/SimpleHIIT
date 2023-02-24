package fr.shining_cat.simplehiit.ui.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
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
    onEditInput: (String) -> Unit,
    onSave: () -> Unit,
    onCancel: () -> Unit
) {

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
                        .padding(horizontal = 24.dp, vertical = 24.dp)
                        .align(Alignment.CenterHorizontally),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    OutlinedTextField(
                        value = inputFieldValue,
                        onValueChange = { onEditInput(it) },
                        isError = false,
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                        keyboardActions = KeyboardActions(onDone = { onSave() }),
                        modifier = Modifier.weight(0.3F, false).alignByBaseline()
                    )
                    Spacer(modifier = Modifier.width(4.dp))
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
                    Button(onClick = { onSave() }) {
                        Text(text = buttonSaveLabel)
                    }
                }
            }
        }
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
            onEditInput = {},
            onSave = {},
            onCancel = {},
        )
    }
}

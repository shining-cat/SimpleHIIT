package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@Composable
fun ButtonError(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String,
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors =
            ButtonDefaults.colors(
                containerColor = MaterialTheme.colorScheme.error,
                contentColor = MaterialTheme.colorScheme.onError,
                focusedContainerColor = MaterialTheme.colorScheme.surface,
                focusedContentColor = MaterialTheme.colorScheme.error,
                pressedContainerColor = MaterialTheme.colorScheme.error,
                pressedContentColor = MaterialTheme.colorScheme.onError,
            ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
    ) {
        Text(text = label)
    }
}

// Previews
@PreviewFontScale
@PreviewLightDark
@Composable
private fun ButtonErrorPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                ButtonError(
                    modifier = Modifier.height(48.dp),
                    label = "Erreur!!",
                )
            }
        }
    }
}

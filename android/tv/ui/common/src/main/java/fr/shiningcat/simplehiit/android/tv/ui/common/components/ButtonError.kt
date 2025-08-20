package fr.shiningcat.simplehiit.android.tv.ui.common.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
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
    fillWidth: Boolean = false,
    fillHeight: Boolean = false,
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
        // Set to none, inner content will handle padding
        contentPadding = PaddingValues(),
    ) {
        Row(
            modifier =
                Modifier
                    .run { if (fillWidth) fillMaxWidth() else this }
                    .run { if (fillHeight) fillMaxHeight() else this }
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = label,
                textAlign = TextAlign.Center,
            )
        }
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
                modifier = Modifier.padding(16.dp),
            ) {
                ButtonError(
                    label = "Short Error",
                )
                ButtonError(
                    modifier = Modifier.width(200.dp),
                    fillWidth = true,
                    label = "Fixed Width Error",
                )
                ButtonError(
                    modifier = Modifier.width(200.dp),
                    fillWidth = true,
                    label = "Fill Width Error",
                )
                ButtonError(
                    modifier = Modifier.height(100.dp),
                    fillHeight = true,
                    label = "Fill Height Error",
                )
                ButtonError(
                    modifier =
                        Modifier
                            .height(48.dp)
                            .width(132.dp),
                    fillWidth = true,
                    fillHeight = true,
                    label = "Erreur!!",
                )
            }
        }
    }
}

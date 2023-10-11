package fr.shiningcat.simplehiit.android.tv.ui.common.components

import android.content.res.Configuration
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.Text
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
fun ButtonError(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {},
    label: String
) {
    Button(
        modifier = modifier,
        onClick = { onClick() },
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.error,
            contentColor = MaterialTheme.colorScheme.onError,
            focusedContainerColor = MaterialTheme.colorScheme.surface,
            focusedContentColor = MaterialTheme.colorScheme.error,
            pressedContainerColor = MaterialTheme.colorScheme.error,
            pressedContentColor = MaterialTheme.colorScheme.onError
        ),
        shape = ButtonDefaults.shape(shape = MaterialTheme.shapes.small),
        contentPadding = PaddingValues(12.dp)
    ) {
        Text(text = label)
    }
}

// Previews
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_NO
)
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES
)
@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun ButtonErrorPreview() {
    SimpleHiitTvTheme {
        Surface(shape = MaterialTheme.shapes.extraSmall) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                ButtonError(
                    modifier = Modifier.height(48.dp),
                    label = "Erreur!!"
                )
            }
        }
    }
}

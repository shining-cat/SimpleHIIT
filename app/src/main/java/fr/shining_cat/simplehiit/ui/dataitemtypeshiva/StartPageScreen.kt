package fr.shining_cat.simplehiit.ui.dataitemtypeshiva

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme
import androidx.compose.material3.ExperimentalMaterial3Api

@Composable
fun StartPageScreen(modifier: Modifier = Modifier, viewModel: StartPageViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun StartPageScreen(
    items: List<String>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier) {
        var nameDataItemTypeShiva by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier.fillMaxWidth().padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameDataItemTypeShiva,
                onValueChange = { nameDataItemTypeShiva = it }
            )

            Button(modifier = Modifier.width(96.dp), onClick = { onSave(nameDataItemTypeShiva) }) {
                Text("Save")
            }
        }
        items.forEach {
            Text("Saved item: $it")
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
private fun DefaultPreview() {
    SimpleHiitTheme {
        StartPageScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

@Preview(showBackground = true, widthDp = 480)
@Composable
private fun PortraitPreview() {
    SimpleHiitTheme {
        StartPageScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
    }
}

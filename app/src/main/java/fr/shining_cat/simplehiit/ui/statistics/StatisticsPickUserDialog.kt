package fr.shining_cat.simplehiit.ui.statistics

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import fr.shining_cat.simplehiit.domain.models.User

@Composable
fun StatisticsPickUserDialog(
    users: List<User>,
    selectUser: (User) -> Unit,
    dismissButtonLabel: String = "",
    dismissAction: () -> Unit
) {
    Dialog(onDismissRequest = dismissAction) {
        Surface(
            color = MaterialTheme.colorScheme.surface,
            shape = MaterialTheme.shapes.medium
        ) {
            Column(
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
            ) {


                TextButton(onClick = dismissAction) {
                    Text(text = dismissButtonLabel)
                }
            }

        }
    }
}
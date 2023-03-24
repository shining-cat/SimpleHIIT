package fr.shining_cat.simplehiit.ui.session

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionWorkNominalContent(screenViewState: SessionViewState.WorkNominal) {
    Row {
        Text(text = "countdown:")
        Text(text = screenViewState.countDown.toString())
    }
    Spacer(modifier = Modifier.height(32.dp))
    Row {
        Text(text = "exerciseRemainingTime:")
        Text(text = screenViewState.exerciseRemainingTime.toString())
    }
    Spacer(modifier = Modifier.height(32.dp))
    Row {
        Text(text = "sessionRemainingTime:")
        Text(text = screenViewState.sessionRemainingTime.toString())
    }

}
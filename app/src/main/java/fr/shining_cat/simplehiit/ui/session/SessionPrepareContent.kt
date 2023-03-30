package fr.shining_cat.simplehiit.ui.session

import androidx.compose.foundation.layout.Row
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SessionPrepareContent(screenViewState: SessionViewState.InitialCountDownSession) {
    Row {
        Text(text = "countdown:")
        Text(text = screenViewState.countDown.toString())
    }
    CircularProgressIndicator(progress = screenViewState.countDown.progress)
}
package fr.shining_cat.simplehiit.ui.session

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SessionPrepareContent(viewState: SessionViewState.InitialCountDownSession) {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        CountDownComponent(size = 64.dp, countDown = viewState.countDown)
    }

}
package fr.shining_cat.simplehiit.ui.session

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SessionScreen(viewModel: SessionViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

}


// Previews


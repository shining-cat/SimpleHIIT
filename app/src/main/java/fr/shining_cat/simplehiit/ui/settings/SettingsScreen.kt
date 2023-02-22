package fr.shining_cat.simplehiit.ui.settings

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SettingsScreen(viewModel: SettingsViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

}

// Previews


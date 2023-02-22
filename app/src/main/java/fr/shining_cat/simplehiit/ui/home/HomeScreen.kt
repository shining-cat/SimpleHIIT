package fr.shining_cat.simplehiit.ui.home

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel
import fr.shining_cat.simplehiit.utils.HiitLogger

@Composable
fun HomeScreen(
    viewModel: HomeViewModel = hiltViewModel()
) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    viewModel.logD("HomeScreen", "INIT")
}

// Previews


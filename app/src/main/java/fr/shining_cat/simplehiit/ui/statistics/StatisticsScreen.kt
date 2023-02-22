package fr.shining_cat.simplehiit.ui.statistics

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun StatisticsScreen(viewModel: StatisticsViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

}

// Previews


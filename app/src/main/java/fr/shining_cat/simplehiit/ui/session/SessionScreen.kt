package fr.shining_cat.simplehiit.ui.session

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun SessionScreen(modifier: Modifier = Modifier, viewModel: SessionViewModel = hiltViewModel()) {
    val lifecycle = LocalLifecycleOwner.current.lifecycle

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun SessionScreen(

) {


}

// Previews


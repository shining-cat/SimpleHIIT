package fr.shining_cat.simplehiit

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalView
import dagger.hilt.android.AndroidEntryPoint
import fr.shining_cat.simplehiit.ui.SimpleHiitNavigation
import fr.shining_cat.simplehiit.ui.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.utils.HiitLogger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var hiitLog: HiitLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        hiitLog.d("MainActivity", "onCreate!!")
        setContent {
            SimpleHiitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleHiitNavigation()
                }
            }
        }
    }
}
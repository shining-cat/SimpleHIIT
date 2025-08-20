package fr.shiningcat.simplehiit.android.mobile.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.adaptive.currentWindowAdaptiveInfo
import androidx.compose.ui.Modifier
import androidx.window.core.layout.WindowHeightSizeClass
import androidx.window.core.layout.WindowWidthSizeClass
import dagger.hilt.android.AndroidEntryPoint
import fr.shiningcat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var hiitLogger: HiitLogger

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
        )
        hiitLogger.d("MainActivity", "onCreate!!")
        setContent {
            val windowSizeClass = currentWindowAdaptiveInfo().windowSizeClass
            val uiArrangement: UiArrangement =
                if (windowSizeClass.windowWidthSizeClass == WindowWidthSizeClass.EXPANDED) { // typically, a tablet or bigger in landscape
                    UiArrangement.HORIZONTAL
                } else { // WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
                    if (windowSizeClass.windowHeightSizeClass == WindowHeightSizeClass.COMPACT) { // typically, a phone in landscape
                        UiArrangement.HORIZONTAL
                    } else {
                        UiArrangement.VERTICAL // typically, a phone or tablet in portrait
                    }
                }

            // the composition tree below knows nothing about window size classes, we only pass a UiArrangement to help screens decide how to build their layout
            SimpleHiitMobileTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background,
                ) {
                    SimpleHiitNavigation(
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger,
                    )
                }
            }
        }
    }
}

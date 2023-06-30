package fr.shining_cat.simplehiit.mobile

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.ui.Modifier
import dagger.hilt.android.AndroidEntryPoint
import fr.shining_cat.simplehiit.android.mobile.ui.common.UiArrangement
import fr.shining_cat.simplehiit.android.mobile.ui.common.theme.SimpleHiitTheme
import fr.shining_cat.simplehiit.commonutils.HiitLogger
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var hiitLogger: HiitLogger

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED
        )
        hiitLogger.d("MainActivity", "onCreate!!")
        setContent {
            val windowSizeClass = calculateWindowSizeClass(this)
            val uiArrangement: UiArrangement =
                if (windowSizeClass.widthSizeClass == WindowWidthSizeClass.Expanded) { //typically, a tablet or bigger in landscape
                    UiArrangement.HORIZONTAL
                } else { //WindowWidthSizeClass.Medium, WindowWidthSizeClass.Compact :
                    if (windowSizeClass.heightSizeClass == WindowHeightSizeClass.Compact) { //typically, a phone in landscape
                        UiArrangement.HORIZONTAL
                    } else {
                        UiArrangement.VERTICAL//typically, a phone or tablet in portrait
                    }
                }

            //the composition tree below knows nothing about window size classes, we only pass a UiArrangement to help screens decide how to build their layout
            SimpleHiitTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SimpleHiitNavigation(
                        uiArrangement = uiArrangement,
                        hiitLogger = hiitLogger
                    )
                }
            }
        }
    }
}
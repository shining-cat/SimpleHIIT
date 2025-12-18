package fr.shiningcat.simplehiit.android.mobile.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.AndroidEntryPoint
import fr.shiningcat.simplehiit.android.mobile.ui.common.helpers.currentUiArrangement
import fr.shiningcat.simplehiit.android.mobile.ui.common.theme.SimpleHiitMobileTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var hiitLogger: HiitLogger

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
            WindowManager.LayoutParams.FLAG_HARDWARE_ACCELERATED,
        )
        hiitLogger.d("MainActivity", "onCreate!!")
        setContent {
            val appTheme by viewModel.appTheme.collectAsStateWithLifecycle()
            val systemInDarkTheme = isSystemInDarkTheme()

            val useDarkTheme =
                when (appTheme) {
                    AppTheme.LIGHT -> false
                    AppTheme.DARK -> true
                    AppTheme.FOLLOW_SYSTEM -> systemInDarkTheme
                }

            val uiArrangement = currentUiArrangement()

            // the composition tree below knows nothing about window size classes, we only pass a UiArrangement to help screens decide how to build their layout
            SimpleHiitMobileTheme(darkTheme = useDarkTheme) {
                // we need to wrap the whole app in a fillMaxSize Surface to avoid white system bars in dark mode + edge-to-edge
                // insets have to be handled at the level below
                Surface(
                    modifier =
                        Modifier
                            .fillMaxSize(),
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

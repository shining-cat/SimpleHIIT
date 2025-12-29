package fr.shiningcat.simplehiit.android.tv.app

import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Surface
import androidx.tv.material3.SurfaceDefaults
import dagger.hilt.android.AndroidEntryPoint
import fr.shiningcat.simplehiit.android.tv.ui.common.theme.SimpleHiitTvTheme
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import javax.inject.Inject

/**
 * Main activity for SimpleHIIT TV.
 * Currently supports both Hilt and Koin during migration.
 *
 * TODO: Remove @AndroidEntryPoint and @Inject after full migration.
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    @Inject
    lateinit var hiitLoggerHilt: HiitLogger

    // Koin injection
    private val hiitLogger: HiitLogger by inject()
    private val viewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
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

            SimpleHiitTvTheme(darkTheme = useDarkTheme) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    colors =
                        SurfaceDefaults.colors(
                            containerColor = MaterialTheme.colorScheme.background,
                            contentColor = MaterialTheme.colorScheme.onBackground,
                        ),
                ) {
                    SimpleHiitNavigation(hiitLogger = hiitLogger)
                }
            }
        }
    }
}

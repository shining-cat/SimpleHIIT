package fr.shiningcat.simplehiit.testutils

import android.app.Application
import android.content.Context
import androidx.test.runner.AndroidJUnitRunner
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin

/**
 * Custom test runner for Koin-based instrumented tests.
 *
 * This runner initializes Koin before tests run and cleans up after.
 * Unlike Hilt which requires HiltTestApplication, Koin can use the regular application
 * and load test-specific modules as needed.
 *
 * Usage in module's build.gradle.kts:
 * ```
 * android {
 *     defaultConfig {
 *         testInstrumentationRunner = "fr.shiningcat.simplehiit.testutils.KoinTestRunner"
 *     }
 * }
 * ```
 *
 * In test classes, use KoinTest and declare test modules:
 * ```
 * @RunWith(AndroidJUnit4::class)
 * class MyInstrumentedTest : KoinTest {
 *     @Before
 *     fun setup() {
 *         loadKoinModules(testModule)
 *     }
 * }
 * ```
 */
class KoinTestRunner : AndroidJUnitRunner() {
    override fun newApplication(
        cl: ClassLoader?,
        className: String?,
        context: Context?,
    ): Application = super.newApplication(cl, KoinTestApplication::class.java.name, context)
}

/**
 * Test application that sets up Koin for instrumented tests.
 * Provides minimal Koin initialization - tests can load additional modules as needed.
 */
class KoinTestApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@KoinTestApplication)
            // Modules are loaded by individual tests as needed
        }
    }

    override fun onTerminate() {
        stopKoin()
        super.onTerminate()
    }
}

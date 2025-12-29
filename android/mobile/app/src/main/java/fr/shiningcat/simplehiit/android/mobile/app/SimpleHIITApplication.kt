package fr.shiningcat.simplehiit.android.mobile.app

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import fr.shiningcat.simplehiit.android.mobile.app.di.allKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class for SimpleHIIT Mobile.
 * Initializes both Hilt and Koin during migration period.
 *
 * TODO: Remove @HiltAndroidApp and Hilt initialization after full migration.
 */
@HiltAndroidApp
class SimpleHIITApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR) // Only log errors to avoid noise
            androidContext(this@SimpleHIITApplication)
            modules(allKoinModules)
        }
    }
}

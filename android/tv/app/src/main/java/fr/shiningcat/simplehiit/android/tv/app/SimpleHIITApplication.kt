package fr.shiningcat.simplehiit.android.tv.app

import android.app.Application
import fr.shiningcat.simplehiit.android.tv.app.di.allKoinModules
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Application class for SimpleHIIT TV.
 */
class SimpleHIITApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        // Initialize Koin
        startKoin {
            androidLogger(Level.ERROR) // Only log errors to avoid noise
            androidContext(this@SimpleHIITApplication)

            // Prevent duplicate definitions: if false, Koin crashes when a definition is declared twice
            // This catches configuration errors immediately rather than silently using the last one.
            allowOverride(false)

            modules(allKoinModules)
        }
    }
}

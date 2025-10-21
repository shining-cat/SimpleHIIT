package fr.shiningcat.simplehiit.commonutils

/**
 * Provides the current Android SDK version.
 * This abstraction allows for easier testing by avoiding direct dependencies on Build.VERSION.SDK_INT.
 */
interface AndroidVersionProvider {
    /**
     * @return The current Android SDK version (API level)
     */
    fun getSdkVersion(): Int
}

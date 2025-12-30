package fr.shiningcat.simplehiit.commonutils

import android.os.Build

/**
 * Production implementation of AndroidVersionProvider.
 * Returns the actual Android SDK version from the system.
 */
class AndroidVersionProviderImpl : AndroidVersionProvider {
    override fun getSdkVersion(): Int = Build.VERSION.SDK_INT
}

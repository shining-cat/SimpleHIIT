package fr.shiningcat.simplehiit.commonutils

import android.os.Build
import javax.inject.Inject

/**
 * Production implementation of AndroidVersionProvider.
 * Returns the actual Android SDK version from the system.
 */
class AndroidVersionProviderImpl
    @Inject
    constructor() : AndroidVersionProvider {
        override fun getSdkVersion(): Int = Build.VERSION.SDK_INT
    }

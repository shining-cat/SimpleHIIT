/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

import android.os.Build
import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromCoverage

/**
 * Production implementation of AndroidVersionProvider.
 * Returns the actual Android SDK version from the system.
 *
 * Excluded from coverage: Trivial wrapper with no business logic to test.
 */
@ExcludeFromCoverage
class AndroidVersionProviderImpl : AndroidVersionProvider {
    override fun getSdkVersion(): Int = Build.VERSION.SDK_INT
}

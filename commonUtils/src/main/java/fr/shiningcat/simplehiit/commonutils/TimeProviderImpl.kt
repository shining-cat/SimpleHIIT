/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.commonutils

import fr.shiningcat.simplehiit.commonutils.annotations.ExcludeFromCoverage

/**
 * Production implementation of TimeProvider.
 * Returns the current system time in milliseconds.
 *
 * Excluded from coverage: Trivial wrapper around System.currentTimeMillis() that exists
 * solely to enable testing of code that depends on current time.
 * Cannot be meaningfully tested as System classes cannot be mocked.
 *
 * See: https://github.com/mockk/mockk/issues/98
 */
@ExcludeFromCoverage
class TimeProviderImpl : TimeProvider {
    override fun getCurrentTimeMillis(): Long = System.currentTimeMillis()
}

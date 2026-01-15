/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.app

import android.content.Context
import fr.shiningcat.simplehiit.android.tv.app.di.allKoinModules
import org.junit.Test
import org.koin.core.annotation.KoinExperimentalAPI
import org.koin.test.verify.verify

/**
 * Verifies that all Koin module definitions are valid and can be resolved.
 * This test catches DI configuration errors that would otherwise only appear at runtime.
 *
 * Benefits:
 * - Catches missing dependencies
 * - Catches circular dependencies
 * - Catches type mismatches
 * - Runs in milliseconds (no emulator needed)
 * - Provides compile-time-like safety for Koin
 */
class TvKoinConfigurationTest {
    @OptIn(KoinExperimentalAPI::class)
    @Test
    fun `verify TV Koin modules configuration is valid`() {
        // Verify each module individually
        allKoinModules.forEach { module ->
            module.verify(
                extraTypes =
                    listOf(
                        Context::class,
                    ),
            )
        }
    }
}

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.local.localemanager

import fr.shiningcat.simplehiit.domain.common.models.AppLanguage

/**
 * Interface for managing application locale settings.
 * Implementation should handle platform-specific locale APIs.
 */
interface LocaleManager {
    /**
     * Sets the application language.
     * @param language The language to set
     * @return true if successful, false otherwise
     */
    fun setAppLanguage(language: AppLanguage): Boolean

    /**
     * Gets the currently configured application language.
     * @return The current app language, or SYSTEM_DEFAULT if not set or on error
     */
    fun getCurrentLanguage(): AppLanguage
}

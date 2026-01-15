/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.datainterfaces

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import kotlinx.coroutines.flow.Flow

interface LanguageRepository {
    suspend fun setAppLanguage(language: AppLanguage): Output<Int>

    fun getCurrentLanguage(): Flow<AppLanguage>
}

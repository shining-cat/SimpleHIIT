/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import kotlinx.coroutines.flow.Flow

class GetCurrentAppLanguageUseCase(
    private val languageRepository: LanguageRepository,
) {
    fun execute(): Flow<AppLanguage> = languageRepository.getCurrentLanguage()
}

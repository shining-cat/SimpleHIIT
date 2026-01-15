/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage

class SetAppLanguageUseCase(
    private val languageRepository: LanguageRepository,
    private val logger: HiitLogger,
) {
    suspend fun execute(language: AppLanguage): Output<Int> = languageRepository.setAppLanguage(language)
}

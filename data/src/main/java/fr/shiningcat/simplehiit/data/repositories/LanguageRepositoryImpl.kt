/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.LanguageRepository
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageRepositoryImpl(
    private val localeManager: LocaleManager,
    private val hiitLogger: HiitLogger,
) : LanguageRepository {
    private val _currentLanguage = MutableStateFlow(localeManager.getCurrentLanguage())

    override suspend fun setAppLanguage(language: AppLanguage): Output<Int> {
        val success = localeManager.setAppLanguage(language)
        return if (success) {
            hiitLogger.d("LanguageRepositoryImpl", "setAppLanguage::Language set to: $language")
            // Update the StateFlow to emit the new language value
            _currentLanguage.value = language
            Output.Success(result = 1)
        } else {
            hiitLogger.e("LanguageRepositoryImpl", "setAppLanguage::Failed to set language to $language")
            Output.Error(
                errorCode = DomainError.LANGUAGE_SET_FAILED,
                exception = Exception("LocaleManager returned false"),
            )
        }
    }

    override fun getCurrentLanguage(): Flow<AppLanguage> {
        // Refresh the current language from the system in case it was changed outside the app
        val freshLanguage = localeManager.getCurrentLanguage()
        hiitLogger.d("LanguageRepositoryImpl", "getCurrentLanguage:: fresh=$freshLanguage, cached=${_currentLanguage.value}")
        // Update the cached value if it differs from the system value
        if (_currentLanguage.value != freshLanguage) {
            hiitLogger.d("LanguageRepositoryImpl", "getCurrentLanguage:: updating cached value to $freshLanguage")
            _currentLanguage.value = freshLanguage
        }
        return _currentLanguage.asStateFlow()
    }
}

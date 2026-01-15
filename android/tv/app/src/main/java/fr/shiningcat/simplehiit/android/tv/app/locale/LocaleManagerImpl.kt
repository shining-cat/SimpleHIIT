/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.app.locale

import android.annotation.SuppressLint
import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProvider
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager as DataLocaleManager

/**
 * TV implementation of LocaleManager.
 * Handles platform-specific locale APIs with proper fallbacks.
 * Uses system LocaleManager on Android 13+, AppCompatDelegate on older versions.
 */
@SuppressLint("NewApi")
class LocaleManagerImpl(
    private val context: Context,
    private val androidVersionProvider: AndroidVersionProvider,
    private val hiitLogger: HiitLogger,
) : DataLocaleManager {
    override fun setAppLanguage(language: AppLanguage): Boolean =
        runCatching {
            if (androidVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+): Use system LocaleManager
                val localeManager = context.getSystemService(LocaleManager::class.java)
                val localeList =
                    if (language.languageTag != null) {
                        LocaleList.forLanguageTags(language.languageTag)
                    } else {
                        LocaleList.getEmptyLocaleList()
                    }
                localeManager.applicationLocales = localeList
                hiitLogger.d("LocaleManagerImpl", "Language set to: $language (via LocaleManager)")
            } else {
                // Android 12 and below: Use AppCompatDelegate
                val localeList =
                    language.languageTag?.let {
                        LocaleListCompat.forLanguageTags(it)
                    } ?: LocaleListCompat.getEmptyLocaleList()
                AppCompatDelegate.setApplicationLocales(localeList)
                hiitLogger.d("LocaleManagerImpl", "Language set to: $language (via AppCompatDelegate)")
            }
        }.onFailure { exception ->
            hiitLogger.e("LocaleManagerImpl", "Failed to set language to $language", exception)
        }.isSuccess

    override fun getCurrentLanguage(): AppLanguage =
        runCatching {
            if (androidVersionProvider.getSdkVersion() >= Build.VERSION_CODES.TIRAMISU) {
                // Android 13+ (API 33+): Use system LocaleManager
                val localeManager = context.getSystemService(LocaleManager::class.java)
                val currentLocales = localeManager.applicationLocales
                parseLocaleList(currentLocales.isEmpty, currentLocales[0]?.toLanguageTag())
            } else {
                // Android 12 and below: Use AppCompatDelegate
                val currentLocales = AppCompatDelegate.getApplicationLocales()
                parseLocaleList(currentLocales.isEmpty, currentLocales[0]?.toLanguageTag())
            }
        }.getOrElse { exception ->
            hiitLogger.e("LocaleManagerImpl", "Failed getting current language", exception)
            AppLanguage.SYSTEM_DEFAULT
        }

    private fun parseLocaleList(
        isEmpty: Boolean,
        languageTag: String?,
    ): AppLanguage {
        if (isEmpty || languageTag == null) {
            return AppLanguage.SYSTEM_DEFAULT
        }
        // Language tags can be "en", "en-US", "fr", "fr-FR", etc.
        // We check if the tag starts with the language code
        return when {
            languageTag.startsWith("en", ignoreCase = true) -> {
                AppLanguage.ENGLISH
            }
            languageTag.startsWith("fr", ignoreCase = true) -> {
                AppLanguage.FRENCH
            }
            languageTag.startsWith("sv", ignoreCase = true) -> {
                AppLanguage.SWEDISH
            }
            else -> {
                hiitLogger.d("LocaleManagerImpl", "Unknown language tag: $languageTag, returning SYSTEM_DEFAULT")
                AppLanguage.SYSTEM_DEFAULT
            }
        }
    }
}

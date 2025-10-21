package fr.shiningcat.simplehiit.android.mobile.app.locale

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import dagger.hilt.android.qualifiers.ApplicationContext
import fr.shiningcat.simplehiit.commonutils.HiitLogger
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import javax.inject.Inject
import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager as DataLocaleManager

/**
 * Mobile implementation of LocaleManager.
 * Handles platform-specific locale APIs with proper fallbacks.
 * Uses system LocaleManager on Android 13+, AppCompatDelegate on older versions.
 */
class LocaleManagerImpl
    @Inject
    constructor(
        @ApplicationContext private val context: Context,
        private val hiitLogger: HiitLogger,
    ) : DataLocaleManager {
        override fun setAppLanguage(language: AppLanguage): Boolean =
            try {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
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
                    hiitLogger.d(
                        "LocaleManagerImpl",
                        "Language set to: $language (via AppCompatDelegate)",
                    )
                }
                true
            } catch (exception: Exception) {
                hiitLogger.e("LocaleManagerImpl", "Failed to set language to $language", exception)
                false
            }

        override fun getCurrentLanguage(): AppLanguage =
            try {
                val result =
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                        // Android 13+ (API 33+): Use system LocaleManager
                        val localeManager = context.getSystemService(LocaleManager::class.java)
                        val currentLocales = localeManager.applicationLocales
                        hiitLogger.d(
                            "LocaleManagerImpl",
                            "Getting current language - isEmpty: ${currentLocales.isEmpty}, size: ${currentLocales.size()}, first: ${currentLocales[0]?.toLanguageTag()}",
                        )
                        parseLocaleList(currentLocales.isEmpty, currentLocales[0]?.toLanguageTag())
                    } else {
                        // Android 12 and below: Use AppCompatDelegate
                        val currentLocales = AppCompatDelegate.getApplicationLocales()
                        hiitLogger.d(
                            "LocaleManagerImpl",
                            "Getting current language - isEmpty: ${currentLocales.isEmpty}, size: ${currentLocales.size()}, first: ${currentLocales[0]?.toLanguageTag()}",
                        )
                        parseLocaleList(currentLocales.isEmpty, currentLocales[0]?.toLanguageTag())
                    }
                hiitLogger.d("LocaleManagerImpl", "getCurrentLanguage returning: $result")
                result
            } catch (exception: Exception) {
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
                languageTag.startsWith("en", ignoreCase = true) -> AppLanguage.ENGLISH
                languageTag.startsWith("fr", ignoreCase = true) -> AppLanguage.FRENCH
                languageTag.startsWith("sv", ignoreCase = true) -> AppLanguage.SWEDISH
                else -> {
                    hiitLogger.d(
                        "LocaleManagerImpl",
                        "Unknown language tag: $languageTag, returning SYSTEM_DEFAULT",
                    )
                    AppLanguage.SYSTEM_DEFAULT
                }
            }
        }
    }

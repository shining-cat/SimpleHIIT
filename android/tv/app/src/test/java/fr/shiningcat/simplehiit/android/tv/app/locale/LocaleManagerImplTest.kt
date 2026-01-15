/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.app.locale

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import fr.shiningcat.simplehiit.commonutils.AndroidVersionProvider
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import io.mockk.slot
import io.mockk.unmockkStatic
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.Locale
import java.util.stream.Stream

internal class LocaleManagerImplTest : AbstractMockkTest() {
    private val mockContext = mockk<Context>()
    private val mockAndroidVersionProvider = mockk<AndroidVersionProvider>()
    private val mockLocaleManager = mockk<LocaleManager>(relaxed = true)
    private val mockLocaleList = mockk<LocaleList>()
    private val mockLocaleListCompat = mockk<LocaleListCompat>()

    @BeforeEach
    override fun setupBeforeEach() {
        super.setupBeforeEach()
        mockkStatic(LocaleList::class)
        mockkStatic(LocaleListCompat::class)
        mockkStatic(AppCompatDelegate::class)
    }

    @AfterEach
    fun tearDown() {
        unmockkStatic(LocaleList::class)
        unmockkStatic(LocaleListCompat::class)
        unmockkStatic(AppCompatDelegate::class)
    }

    // ////////////
    // SET APP LANGUAGE - Android 13+
    @ParameterizedTest(name = "setAppLanguage sets language {0} using LocaleManager on Android 13+")
    @MethodSource("appLanguageWithTagsProvider")
    fun `setAppLanguage sets language using LocaleManager on Android 13 and above`(
        language: AppLanguage,
        expectedTag: String?,
    ) {
        // Set Android version to 33 (Android 13)
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } returns mockLocaleManager
        if (expectedTag != null) {
            every { LocaleList.forLanguageTags(expectedTag) } returns mockLocaleList
        } else {
            every { LocaleList.getEmptyLocaleList() } returns mockLocaleList
        }
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)
        val localeListSlot = slot<LocaleList>()
        every { mockLocaleManager.applicationLocales = capture(localeListSlot) } answers {}

        // When
        val result = localeManagerImpl.setAppLanguage(language)

        // Then
        assertTrue(result)
        verify(exactly = 1) { mockContext.getSystemService(LocaleManager::class.java) }
        verify(exactly = 1) { mockLocaleManager.applicationLocales = any() }
        assertEquals(mockLocaleList, localeListSlot.captured)
        verify(exactly = 1) {
            mockHiitLogger.d(
                "LocaleManagerImpl",
                "Language set to: $language (via LocaleManager)",
            )
        }
    }

    @Test
    fun `setAppLanguage handles exception on Android 13 and above`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } throws RuntimeException("Test exception")
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.setAppLanguage(AppLanguage.ENGLISH)

        // Then
        assertFalse(result)
        verify(exactly = 1) {
            mockHiitLogger.e(
                "LocaleManagerImpl",
                "Failed to set language to ${AppLanguage.ENGLISH}",
                any<Exception>(),
            )
        }
    }

    // ////////////
    // SET APP LANGUAGE - Android 12 and below
    @ParameterizedTest(name = "setAppLanguage sets language {0} using AppCompatDelegate on Android 12 and below")
    @MethodSource("appLanguageWithTagsProvider")
    fun `setAppLanguage sets language using AppCompatDelegate on Android 12 and below`(
        language: AppLanguage,
        expectedTag: String?,
    ) {
        // Set Android version to 32 (Android 12)
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.S
        if (expectedTag != null) {
            every { LocaleListCompat.forLanguageTags(expectedTag) } returns mockLocaleListCompat
        } else {
            every { LocaleListCompat.getEmptyLocaleList() } returns mockLocaleListCompat
        }
        every { AppCompatDelegate.setApplicationLocales(any()) } answers {}
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)
        val localeListSlot = slot<LocaleListCompat>()
        every { AppCompatDelegate.setApplicationLocales(capture(localeListSlot)) } answers {}

        // When
        val result = localeManagerImpl.setAppLanguage(language)

        // Then
        assertTrue(result)
        verify(exactly = 1) { AppCompatDelegate.setApplicationLocales(any()) }
        assertEquals(mockLocaleListCompat, localeListSlot.captured)
        verify(exactly = 1) {
            mockHiitLogger.d(
                "LocaleManagerImpl",
                "Language set to: $language (via AppCompatDelegate)",
            )
        }
    }

    @Test
    fun `setAppLanguage handles exception on Android 12 and below`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.S
        every { LocaleListCompat.forLanguageTags(any()) } throws RuntimeException("Test exception")
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.setAppLanguage(AppLanguage.FRENCH)

        // Then
        assertFalse(result)
        verify(exactly = 1) {
            mockHiitLogger.e(
                "LocaleManagerImpl",
                "Failed to set language to ${AppLanguage.FRENCH}",
                any<Exception>(),
            )
        }
    }

    // ////////////
    // GET CURRENT LANGUAGE - Android 13+
    @ParameterizedTest(name = "getCurrentLanguage returns {0} from LocaleManager on Android 13+")
    @MethodSource("languageTagToAppLanguageProvider")
    fun `getCurrentLanguage returns correct language from LocaleManager on Android 13 and above`(
        languageTag: String,
        expectedLanguage: AppLanguage,
    ) {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } returns mockLocaleManager
        every { mockLocaleManager.applicationLocales } returns mockLocaleList
        every { mockLocaleList.isEmpty } returns false
        every { mockLocaleList.size() } returns 1
        val mockLocale = mockk<Locale>()
        every { mockLocale.toLanguageTag() } returns languageTag
        every { mockLocaleList[0] } returns mockLocale
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(expectedLanguage, result)
        verify(exactly = 1) { mockContext.getSystemService(LocaleManager::class.java) }
        verify(exactly = 1) { mockLocaleManager.applicationLocales }
    }

    @Test
    fun `getCurrentLanguage returns SYSTEM_DEFAULT when locale list is empty on Android 13+`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } returns mockLocaleManager
        every { mockLocaleManager.applicationLocales } returns mockLocaleList
        every { mockLocaleList.isEmpty } returns true
        every { mockLocaleList.size() } returns 0
        every { mockLocaleList[0] } returns null
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(AppLanguage.SYSTEM_DEFAULT, result)
    }

    @Test
    fun `getCurrentLanguage handles exception on Android 13 and above`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } throws RuntimeException("Test exception")
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(AppLanguage.SYSTEM_DEFAULT, result)
        verify(exactly = 1) {
            mockHiitLogger.e(
                "LocaleManagerImpl",
                "Failed getting current language",
                any<Exception>(),
            )
        }
    }

    // ////////////
    // GET CURRENT LANGUAGE - Android 12 and below
    @ParameterizedTest(name = "getCurrentLanguage returns {0} from AppCompatDelegate on Android 12 and below")
    @MethodSource("languageTagToAppLanguageProvider")
    fun `getCurrentLanguage returns correct language from AppCompatDelegate on Android 12 and below`(
        languageTag: String,
        expectedLanguage: AppLanguage,
    ) {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.S
        every { AppCompatDelegate.getApplicationLocales() } returns mockLocaleListCompat
        every { mockLocaleListCompat.isEmpty } returns false
        every { mockLocaleListCompat.size() } returns 1
        val mockLocale = mockk<Locale>()
        every { mockLocale.toLanguageTag() } returns languageTag
        every { mockLocaleListCompat[0] } returns mockLocale
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(expectedLanguage, result)
        verify(exactly = 1) { AppCompatDelegate.getApplicationLocales() }
    }

    @Test
    fun `getCurrentLanguage returns SYSTEM_DEFAULT when locale list is empty on Android 12 and below`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.S
        every { AppCompatDelegate.getApplicationLocales() } returns mockLocaleListCompat
        every { mockLocaleListCompat.isEmpty } returns true
        every { mockLocaleListCompat.size() } returns 0
        every { mockLocaleListCompat[0] } returns null
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(AppLanguage.SYSTEM_DEFAULT, result)
    }

    @Test
    fun `getCurrentLanguage handles exception on Android 12 and below`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.S
        every { AppCompatDelegate.getApplicationLocales() } throws RuntimeException("Test exception")
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(AppLanguage.SYSTEM_DEFAULT, result)
        verify(exactly = 1) {
            mockHiitLogger.e(
                "LocaleManagerImpl",
                "Failed getting current language",
                any<Exception>(),
            )
        }
    }

    @Test
    fun `getCurrentLanguage returns SYSTEM_DEFAULT for unknown language tag`() {
        // Given
        every { mockAndroidVersionProvider.getSdkVersion() } returns Build.VERSION_CODES.TIRAMISU
        every { mockContext.getSystemService(LocaleManager::class.java) } returns mockLocaleManager
        every { mockLocaleManager.applicationLocales } returns mockLocaleList
        every { mockLocaleList.isEmpty } returns false
        every { mockLocaleList.size() } returns 1
        val mockLocale = mockk<Locale>()
        every { mockLocale.toLanguageTag() } returns "de-DE" // German, not supported
        every { mockLocaleList[0] } returns mockLocale
        val localeManagerImpl = LocaleManagerImpl(mockContext, mockAndroidVersionProvider, mockHiitLogger)

        // When
        val result = localeManagerImpl.getCurrentLanguage()

        // Then
        assertEquals(AppLanguage.SYSTEM_DEFAULT, result)
        verify(exactly = 1) {
            mockHiitLogger.d(
                "LocaleManagerImpl",
                "Unknown language tag: de-DE, returning SYSTEM_DEFAULT",
            )
        }
    }

    companion object {
        @JvmStatic
        fun appLanguageWithTagsProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of(AppLanguage.SYSTEM_DEFAULT, null),
                Arguments.of(AppLanguage.ENGLISH, "en"),
                Arguments.of(AppLanguage.FRENCH, "fr"),
                Arguments.of(AppLanguage.SWEDISH, "sv"),
            )

        @JvmStatic
        fun languageTagToAppLanguageProvider(): Stream<Arguments> =
            Stream.of(
                Arguments.of("en", AppLanguage.ENGLISH),
                Arguments.of("en-US", AppLanguage.ENGLISH),
                Arguments.of("en-GB", AppLanguage.ENGLISH),
                Arguments.of("fr", AppLanguage.FRENCH),
                Arguments.of("fr-FR", AppLanguage.FRENCH),
                Arguments.of("fr-CA", AppLanguage.FRENCH),
                Arguments.of("sv", AppLanguage.SWEDISH),
                Arguments.of("sv-SE", AppLanguage.SWEDISH),
            )
    }
}

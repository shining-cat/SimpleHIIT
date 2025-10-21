package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.localemanager.LocaleManager
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.EnumSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class LanguageRepositoryImplTest : AbstractMockkTest() {
    private val mockLocaleManager = mockk<LocaleManager>()

    // ////////////
    // INITIALIZATION
    @ParameterizedTest(name = "repository initializes with current language {0}")
    @EnumSource(AppLanguage::class)
    fun `repository initializes StateFlow with current language from LocaleManager`(language: AppLanguage) =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns language
            //
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )
            //
            // StateFlow is initialized during construction
            verify(exactly = 1) { mockLocaleManager.getCurrentLanguage() }
            // Verify the initial value
            val result = repository.getCurrentLanguage().first()
            assertEquals(language, result)
        }

    // ////////////
    // SET APP LANGUAGE
    @ParameterizedTest(name = "setAppLanguage calls LocaleManager with correct parameter and returns success for {0}")
    @EnumSource(AppLanguage::class)
    fun `setAppLanguage calls LocaleManager with correct parameter and returns success when it succeeds`(language: AppLanguage) =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns AppLanguage.SYSTEM_DEFAULT
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )
            val languageSlot = slot<AppLanguage>()
            every { mockLocaleManager.setAppLanguage(capture(languageSlot)) } returns true
            //
            val result = repository.setAppLanguage(language)
            //
            // Verify the correct parameter was passed to LocaleManager
            assertEquals(language, languageSlot.captured)
            verify(exactly = 1) { mockLocaleManager.setAppLanguage(any()) }
            // Verify success output
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(1, result.result)
        }

    @Test
    fun `setAppLanguage calls LocaleManager and returns error when it fails`() =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns AppLanguage.ENGLISH
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )
            val languageSlot = slot<AppLanguage>()
            every { mockLocaleManager.setAppLanguage(capture(languageSlot)) } returns false
            //
            val result = repository.setAppLanguage(AppLanguage.FRENCH)
            //
            // Verify the correct parameter was passed to LocaleManager
            assertEquals(AppLanguage.FRENCH, languageSlot.captured)
            verify(exactly = 1) { mockLocaleManager.setAppLanguage(any()) }
            verify(exactly = 1) {
                mockHiitLogger.e(
                    "LanguageRepositoryImpl",
                    "setAppLanguage::Failed to set language to ${AppLanguage.FRENCH}",
                )
            }
            // Verify error output
            assertTrue(result is Output.Error)
            result as Output.Error
            assertEquals(Constants.Errors.LANGUAGE_SET_FAILED, result.errorCode)
        }

    // ////////////
    // GET CURRENT LANGUAGE
    @ParameterizedTest(name = "getCurrentLanguage returns {0}")
    @EnumSource(AppLanguage::class)
    fun `getCurrentLanguage returns language from LocaleManager on first call`(language: AppLanguage) =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns language
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )
            //
            val result = repository.getCurrentLanguage().first()
            //
            // Called once during initialization, once during getCurrentLanguage
            verify(atLeast = 1) { mockLocaleManager.getCurrentLanguage() }
            assertEquals(language, result)
        }

    @Test
    fun `getCurrentLanguage refreshes from system and updates StateFlow when system language changes`() =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns AppLanguage.ENGLISH
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )
            // Verify initial language
            val initialLanguage = repository.getCurrentLanguage().first()
            assertEquals(AppLanguage.ENGLISH, initialLanguage)

            // Simulate system language change
            every { mockLocaleManager.getCurrentLanguage() } returns AppLanguage.FRENCH

            // Call getCurrentLanguage again - should refresh from system
            val updatedLanguage = repository.getCurrentLanguage().first()
            //
            assertEquals(AppLanguage.FRENCH, updatedLanguage)
            verify(atLeast = 2) { mockLocaleManager.getCurrentLanguage() }
        }

    @Test
    fun `getCurrentLanguage does not update StateFlow when system language hasn't changed`() =
        runTest {
            every { mockLocaleManager.getCurrentLanguage() } returns AppLanguage.SWEDISH
            val repository =
                LanguageRepositoryImpl(
                    localeManager = mockLocaleManager,
                    hiitLogger = mockHiitLogger,
                )

            // First call
            val firstResult = repository.getCurrentLanguage().first()
            assertEquals(AppLanguage.SWEDISH, firstResult)

            // Second call - system language hasn't changed
            val secondResult = repository.getCurrentLanguage().first()
            assertEquals(AppLanguage.SWEDISH, secondResult)

            // Should have called LocaleManager multiple times to check for changes
            verify(atLeast = 2) { mockLocaleManager.getCurrentLanguage() }
        }
}

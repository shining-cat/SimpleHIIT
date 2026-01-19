/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppLanguage
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SettingsPresenter app-level settings.
 * Includes language, theme, and reset operations.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterAppSettingsTest : SettingsPresenterTestBase() {
    @Test
    fun `editLanguage emits PickLanguage dialog with current language`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editLanguage()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.PickLanguage)
            assertEquals(AppLanguage.ENGLISH, (dialogState as SettingsDialog.PickLanguage).currentLanguage)

            collectorJob.cancel()
        }

    @Test
    fun `setLanguage calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.setAppLanguage(any()) } returns Output.Success(1)

            testedPresenter.setLanguage(AppLanguage.FRENCH)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setAppLanguage(AppLanguage.FRENCH) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editTheme emits PickTheme dialog with current theme`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editTheme()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.PickTheme)
            assertEquals(AppTheme.FOLLOW_SYSTEM, (dialogState as SettingsDialog.PickTheme).currentTheme)

            collectorJob.cancel()
        }

    @Test
    fun `setTheme calls interactor dismisses dialog and emits restart trigger`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.setAppTheme(any()) } just runs

            // Collect restartTrigger emissions
            val emissions = mutableListOf<Unit>()
            val collectorJob =
                launch {
                    testedPresenter.restartTrigger.take(1).toList(emissions)
                }

            testedPresenter.setTheme(AppTheme.DARK)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setAppTheme(AppTheme.DARK) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify restart trigger was emitted
            assertEquals(1, emissions.size)

            collectorJob.cancel()
        }

    @Test
    fun `resetAllSettings emits ConfirmResetAllSettings dialog`() =
        runTest(testDispatcher) {
            testedPresenter.resetAllSettings()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.ConfirmResetAllSettings, dialogState)
        }

    @Test
    fun `resetAllSettingsConfirmation calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.resetAllSettings() } returns Unit

            testedPresenter.resetAllSettingsConfirmation()
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.resetAllSettings() }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }
}

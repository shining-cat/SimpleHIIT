/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests for success paths in SettingsPresenter setter methods.
 * Covers the branches where validation passes and interactor is called successfully.
 * Ensures complete branch coverage for all setter methods.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterSuccessPathsTest : SettingsPresenterTestBase() {
    @Test
    fun `setWorkPeriodLength with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val validInput = "30"

            // Mock validation to return null (success)
            every {
                mockSettingsInteractor.validatePeriodLength(validInput, any())
            } returns null
            coEvery { mockSettingsInteractor.setWorkPeriodLength(any()) } returns Unit

            testedPresenter.setWorkPeriodLength(validInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should call interactor with converted value
            coVerify { mockSettingsInteractor.setWorkPeriodLength(30000L) }

            // Should dismiss dialog
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setRestPeriodLength with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val validInput = "15"

            // Mock validation to return null (success)
            every {
                mockSettingsInteractor.validatePeriodLength(validInput, any())
            } returns null
            coEvery { mockSettingsInteractor.setRestPeriodLength(any()) } returns Unit

            testedPresenter.setRestPeriodLength(validInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should call interactor with converted value
            coVerify { mockSettingsInteractor.setRestPeriodLength(15000L) }

            // Should dismiss dialog
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setNumberOfWorkPeriods with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val validInput = "8"

            // Mock validation to return null (success)
            every {
                mockSettingsInteractor.validateNumberOfWorkPeriods(validInput)
            } returns null
            coEvery { mockSettingsInteractor.setNumberOfWorkPeriods(any()) } returns Unit

            testedPresenter.setNumberOfWorkPeriods(validInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should call interactor with converted value
            coVerify { mockSettingsInteractor.setNumberOfWorkPeriods(8) }

            // Should dismiss dialog
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setSessionStartCountDown with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val validInput = "10"

            // Mock validation to return null (success)
            every {
                mockSettingsInteractor.validateInputSessionStartCountdown(validInput)
            } returns null
            coEvery { mockSettingsInteractor.setSessionStartCountDown(any()) } returns Unit

            testedPresenter.setSessionStartCountDown(validInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should call interactor with converted value
            coVerify { mockSettingsInteractor.setSessionStartCountDown(10000L) }

            // Should dismiss dialog
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setPeriodStartCountDown with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val validInput = "5"

            // Mock validation to return null (success)
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = validInput,
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            } returns null
            coEvery { mockSettingsInteractor.setPeriodStartCountDown(any()) } returns Unit

            testedPresenter.setPeriodStartCountDown(validInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should call interactor with converted value
            coVerify { mockSettingsInteractor.setPeriodStartCountDown(5000L) }

            // Should dismiss dialog
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    private suspend fun setupNominalStateAndCollect(scope: kotlinx.coroutines.CoroutineScope) {
        // Mock the mapper to return Nominal state
        every { mockMapper.map(any()) } returns testNominalViewState()

        // Emit the settings to trigger state update
        generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
        testDispatcher.scheduler.advanceUntilIdle()

        // Collect the screenViewState flow to ensure it updates
        // StateFlow with WhileSubscribed needs active collection
        val collectorJob =
            scope.launch {
                testedPresenter.screenViewState.first()
            }
        testDispatcher.scheduler.advanceUntilIdle()
        collectorJob.cancel()
    }
}

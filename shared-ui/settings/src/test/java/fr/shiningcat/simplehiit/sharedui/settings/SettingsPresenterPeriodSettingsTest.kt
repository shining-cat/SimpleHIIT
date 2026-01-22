/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.InputError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SettingsPresenter period settings.
 * Includes work period, rest period, number of periods, beep sound, and countdowns.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterPeriodSettingsTest : SettingsPresenterTestBase() {
    // Work period tests
    @Test
    fun `editWorkPeriodLength with Nominal state emits dialog with current value`() =
        runTest(testDispatcher) {
            val nominalState = testNominalViewState()
            every { mockMapper.map(any()) } returns nominalState
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editWorkPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditWorkPeriodLength)
            assertEquals("20", (dialogState as SettingsDialog.EditWorkPeriodLength).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setWorkPeriodLength with valid input calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns null
            coEvery { mockSettingsInteractor.setWorkPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setWorkPeriodLength("30")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setWorkPeriodLength(30000L) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `setWorkPeriodLength with invalid input does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns InputError.WRONG_FORMAT
            coEvery { mockSettingsInteractor.setWorkPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.setWorkPeriodLength("invalid")
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setWorkPeriodLength(any()) }

            collectorJob.cancel()
        }

    @Test
    fun `validatePeriodLengthInput with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength("25", 5L) } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("25")

            assertEquals(null, result)
            coVerify { mockSettingsInteractor.validatePeriodLength("25", 5L) }

            collectorJob.cancel()
        }

    // Rest period tests
    @Test
    fun `editRestPeriodLength with Nominal state emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editRestPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditRestPeriodLength)
            assertEquals("10", (dialogState as SettingsDialog.EditRestPeriodLength).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setRestPeriodLength with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns null
            coEvery { mockSettingsInteractor.setRestPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setRestPeriodLength("15")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setRestPeriodLength(15000L) }
        }

    @Test
    fun `setRestPeriodLength with invalid input does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validatePeriodLength(any(), any()) } returns InputError.WRONG_FORMAT
            coEvery { mockSettingsInteractor.setRestPeriodLength(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.setRestPeriodLength("invalid")
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setRestPeriodLength(any()) }

            collectorJob.cancel()
        }

    // Number of work periods tests
    @Test
    fun `editNumberOfWorkPeriods emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editNumberOfWorkPeriods()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditNumberCycles)
            assertEquals("8", (dialogState as SettingsDialog.EditNumberCycles).numberOfCycles)

            collectorJob.cancel()
        }

    @Test
    fun `setNumberOfWorkPeriods with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateNumberOfWorkPeriods(any()) } returns null
            coEvery { mockSettingsInteractor.setNumberOfWorkPeriods(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setNumberOfWorkPeriods("12")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setNumberOfWorkPeriods(12) }
        }

    @Test
    fun `setNumberOfWorkPeriods with invalid input does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateNumberOfWorkPeriods(any()) } returns InputError.VALUE_TOO_SMALL
            coEvery { mockSettingsInteractor.setNumberOfWorkPeriods(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setNumberOfWorkPeriods("0")
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setNumberOfWorkPeriods(any()) }
        }

    @Test
    fun `validateNumberOfWorkPeriods delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockSettingsInteractor.validateNumberOfWorkPeriods("10") } returns null

            val result = testedPresenter.validateNumberOfWorkPeriods("10")

            assertEquals(null, result)
        }

    // Beep sound test
    @Test
    fun `toggleBeepSound with Nominal state calls interactor with toggled value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            coEvery { mockSettingsInteractor.setBeepSound(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.toggleBeepSound()
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setBeepSound(false) } // Was true, now false

            collectorJob.cancel()
        }

    // Session start countdown tests
    @Test
    fun `editSessionStartCountDown emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editSessionStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditSessionStartCountDown)
            assertEquals("10", (dialogState as SettingsDialog.EditSessionStartCountDown).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setSessionStartCountDown with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateInputSessionStartCountdown(any()) } returns null
            coEvery { mockSettingsInteractor.setSessionStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setSessionStartCountDown("12")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setSessionStartCountDown(12000L) }
        }

    @Test
    fun `setSessionStartCountDown with invalid input does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every { mockSettingsInteractor.validateInputSessionStartCountdown(any()) } returns InputError.VALUE_TOO_SMALL
            coEvery { mockSettingsInteractor.setSessionStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setSessionStartCountDown("-1")
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setSessionStartCountDown(any()) }
        }

    // Period start countdown tests
    @Test
    fun `editPeriodStartCountDown emits dialog with current value`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.editPeriodStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditPeriodStartCountDown)
            assertEquals("5", (dialogState as SettingsDialog.EditPeriodStartCountDown).valueSeconds)

            collectorJob.cancel()
        }

    @Test
    fun `setPeriodStartCountDown with valid input calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(any(), any(), any())
            } returns null
            coEvery { mockSettingsInteractor.setPeriodStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.setPeriodStartCountDown("8")
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.setPeriodStartCountDown(8000L) }
        }

    @Test
    fun `setPeriodStartCountDown with invalid input does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(any(), any(), any())
            } returns InputError.VALUE_TOO_BIG
            coEvery { mockSettingsInteractor.setPeriodStartCountDown(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            testedPresenter.setPeriodStartCountDown("100")
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setPeriodStartCountDown(any()) }

            collectorJob.cancel()
        }

    @Test
    fun `validateInputPeriodStartCountdown with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown("6", 20L, 10L)
            } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("6")

            assertEquals(null, result)
        }
}

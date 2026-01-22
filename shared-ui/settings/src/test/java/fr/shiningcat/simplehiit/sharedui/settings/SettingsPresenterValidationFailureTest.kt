/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.InputError
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Validation failure tests for SettingsPresenter.
 * Tests the else branches in setter methods when validation fails.
 * This improves branch coverage by testing error paths.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterValidationFailureTest : SettingsPresenterTestBase() {
    @Test
    fun `setWorkPeriodLength with invalid input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "-5"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validatePeriodLength(invalidInput, any())
            } returns InputError.VALUE_TOO_SMALL

            testedPresenter.setWorkPeriodLength(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setWorkPeriodLength(any()) }
        }

    @Test
    fun `setRestPeriodLength with invalid input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "0"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validatePeriodLength(invalidInput, any())
            } returns InputError.VALUE_TOO_SMALL

            testedPresenter.setRestPeriodLength(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setRestPeriodLength(any()) }
        }

    @Test
    fun `setNumberOfWorkPeriods with invalid input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "abc"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateNumberOfWorkPeriods(invalidInput)
            } returns InputError.WRONG_FORMAT

            testedPresenter.setNumberOfWorkPeriods(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setNumberOfWorkPeriods(any()) }
        }

    @Test
    fun `setSessionStartCountDown with invalid input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "999"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateInputSessionStartCountdown(invalidInput)
            } returns InputError.VALUE_TOO_BIG

            testedPresenter.setSessionStartCountDown(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setSessionStartCountDown(any()) }
        }

    @Test
    fun `setPeriodStartCountDown with invalid input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "-1"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = invalidInput,
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            } returns InputError.VALUE_TOO_SMALL

            testedPresenter.setPeriodStartCountDown(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setPeriodStartCountDown(any()) }
        }

    @Test
    fun `setWorkPeriodLength with empty input logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = ""

            // Mock validation to return error
            every {
                mockSettingsInteractor.validatePeriodLength(invalidInput, any())
            } returns InputError.VALUE_EMPTY

            testedPresenter.setWorkPeriodLength(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setWorkPeriodLength(any()) }
        }

    @Test
    fun `setRestPeriodLength with value too large logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "999999"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validatePeriodLength(invalidInput, any())
            } returns InputError.VALUE_TOO_BIG

            testedPresenter.setRestPeriodLength(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setRestPeriodLength(any()) }
        }

    @Test
    fun `setNumberOfWorkPeriods with value too small logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "0"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateNumberOfWorkPeriods(invalidInput)
            } returns InputError.VALUE_TOO_SMALL

            testedPresenter.setNumberOfWorkPeriods(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setNumberOfWorkPeriods(any()) }
        }

    @Test
    fun `setSessionStartCountDown with empty field logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = ""

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateInputSessionStartCountdown(invalidInput)
            } returns InputError.VALUE_EMPTY

            testedPresenter.setSessionStartCountDown(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setSessionStartCountDown(any()) }
        }

    @Test
    fun `setPeriodStartCountDown with not a number logs error and does not call interactor`() =
        runTest(testDispatcher) {
            setupNominalStateAndCollect(this)
            val invalidInput = "xyz"

            // Mock validation to return error
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = invalidInput,
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            } returns InputError.WRONG_FORMAT

            testedPresenter.setPeriodStartCountDown(invalidInput)
            testDispatcher.scheduler.advanceUntilIdle()

            // Should not call interactor when validation fails
            coVerify(exactly = 0) { mockSettingsInteractor.setPeriodStartCountDown(any()) }
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

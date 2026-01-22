/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.domain.common.models.User
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * Tests for successful validation paths in SettingsPresenter.
 * These tests cover the branches where validation passes (returns null).
 * Ensures both validation success and failure branches are covered.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterValidationSuccessTest : SettingsPresenterTestBase() {
    @Test
    fun `validatePeriodLengthInput with valid input returns null`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validatePeriodLength(any(), any())
            } returns null // Valid input
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("30")

            assertNull(result)
            collectorJob.cancel()
        }

    @Test
    fun `validatePeriodLengthInput with invalid input returns error`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validatePeriodLength(any(), any())
            } returns InputError.VALUE_TOO_SMALL // Invalid input
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("0")

            assertEquals(InputError.VALUE_TOO_SMALL, result)
            collectorJob.cancel()
        }

    @Test
    fun `validateNumberOfWorkPeriods with valid input returns null`() =
        runTest(testDispatcher) {
            every {
                mockSettingsInteractor.validateNumberOfWorkPeriods("5")
            } returns null // Valid input

            val result = testedPresenter.validateNumberOfWorkPeriods("5")

            assertNull(result)
        }

    @Test
    fun `validateNumberOfWorkPeriods with invalid input returns error`() =
        runTest(testDispatcher) {
            every {
                mockSettingsInteractor.validateNumberOfWorkPeriods("abc")
            } returns InputError.WRONG_FORMAT // Invalid input

            val result = testedPresenter.validateNumberOfWorkPeriods("abc")

            assertEquals(InputError.WRONG_FORMAT, result)
        }

    @Test
    fun `validateInputSessionStartCountdown with valid input returns null`() =
        runTest(testDispatcher) {
            every {
                mockSettingsInteractor.validateInputSessionStartCountdown("10")
            } returns null // Valid input

            val result = testedPresenter.validateInputSessionStartCountdown("10")

            assertNull(result)
        }

    @Test
    fun `validateInputSessionStartCountdown with invalid input returns error`() =
        runTest(testDispatcher) {
            every {
                mockSettingsInteractor.validateInputSessionStartCountdown("999")
            } returns InputError.VALUE_TOO_BIG // Invalid input

            val result = testedPresenter.validateInputSessionStartCountdown("999")

            assertEquals(InputError.VALUE_TOO_BIG, result)
        }

    @Test
    fun `validateInputPeriodStartCountdown with valid input returns null`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = "5",
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            } returns null // Valid input
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("5")

            assertNull(result)
            collectorJob.cancel()
        }

    @Test
    fun `validateInputPeriodStartCountdown with invalid input returns error`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = "100",
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            } returns InputError.VALUE_TOO_BIG // Invalid input
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("100")

            assertEquals(InputError.VALUE_TOO_BIG, result)
            collectorJob.cancel()
        }

    @Test
    fun `validateInputUserNameString with valid user returns null`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            val testUser = User(id = 3L, name = "New User", selected = false)
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputUserName(
                    user = testUser,
                    existingUsers = any(),
                )
            } returns null // Valid user
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(testUser)

            assertNull(result)
            collectorJob.cancel()
        }

    @Test
    fun `validateInputUserNameString with duplicate name returns error`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            val duplicateUser = User(id = 3L, name = "User 1", selected = false) // Same name as testUser1
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputUserName(
                    user = duplicateUser,
                    existingUsers = any(),
                )
            } returns InputError.VALUE_ALREADY_TAKEN // Duplicate name
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(duplicateUser)

            assertEquals(InputError.VALUE_ALREADY_TAKEN, result)
            collectorJob.cancel()
        }

    @Test
    fun `validateInputUserNameString with empty name returns error`() =
        runTest(testDispatcher) {
            // Setup Nominal state
            val emptyNameUser = User(id = 0L, name = "", selected = false)
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputUserName(
                    user = emptyNameUser,
                    existingUsers = any(),
                )
            } returns InputError.VALUE_EMPTY // Empty name
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            // Collect to ensure state is updated
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.first()
                }
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(emptyNameUser)

            assertEquals(InputError.VALUE_EMPTY, result)
            collectorJob.cancel()
        }
}

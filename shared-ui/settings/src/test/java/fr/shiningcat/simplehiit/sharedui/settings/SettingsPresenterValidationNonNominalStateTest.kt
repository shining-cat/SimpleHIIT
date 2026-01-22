/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import io.mockk.coEvery
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * Tests validation methods when state is NOT Nominal (Loading/Error).
 * This covers the defensive programming branches that return null when state data is unavailable.
 * These branches prevent NullPointerExceptions and ensure validation fails gracefully.
 *
 * Branch coverage: Tests the "state is not Nominal" branch in validation methods.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterValidationNonNominalStateTest : SettingsPresenterTestBase() {
    @Test
    fun `validatePeriodLengthInput when state is Loading returns null without calling interactor`() =
        runTest(testDispatcher) {
            // State is Loading (no data emitted yet)
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading

            // Don't emit anything - state remains Loading
            testDispatcher.scheduler.advanceUntilIdle()

            // Call validation
            val result = testedPresenter.validatePeriodLengthInput("30")

            // Should return null (no error) without calling interactor
            assertNull(result)
            verify(exactly = 0) { mockSettingsInteractor.validatePeriodLength(any(), any()) }
        }

    @Test
    fun `validatePeriodLengthInput when state is Error returns null without calling interactor`() =
        runTest(testDispatcher) {
            // Set up Error state
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error(errorCode = "123")
            generalSettingsFlow.emit(Output.Error(errorCode = DomainError.DATABASE_FETCH_FAILED, exception = Exception("Test error")))
            testDispatcher.scheduler.advanceUntilIdle()

            // Call validation
            val result = testedPresenter.validatePeriodLengthInput("30")

            // Should return null without calling interactor
            assertNull(result)
            verify(exactly = 0) { mockSettingsInteractor.validatePeriodLength(any(), any()) }
        }

    @Test
    fun `validateInputPeriodStartCountdown when state is Loading returns null without calling interactor`() =
        runTest(testDispatcher) {
            // State is Loading
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            testDispatcher.scheduler.advanceUntilIdle()

            // Call validation
            val result = testedPresenter.validateInputPeriodStartCountdown("5")

            // Should return null without calling interactor
            assertNull(result)
            verify(exactly = 0) {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = any(),
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            }
        }

    @Test
    fun `validateInputPeriodStartCountdown when state is Error returns null without calling interactor`() =
        runTest(testDispatcher) {
            // Set up Error state
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error(errorCode = "456")
            generalSettingsFlow.emit(Output.Error(errorCode = DomainError.DATABASE_FETCH_FAILED, exception = Exception("Test error")))
            testDispatcher.scheduler.advanceUntilIdle()

            // Call validation
            val result = testedPresenter.validateInputPeriodStartCountdown("5")

            // Should return null without calling interactor
            assertNull(result)
            verify(exactly = 0) {
                mockSettingsInteractor.validateInputPeriodStartCountdown(
                    input = any(),
                    workPeriodLengthSeconds = any(),
                    restPeriodLengthSeconds = any(),
                )
            }
        }

    @Test
    fun `validateInputUserNameString when state is Loading returns null without calling interactor`() =
        runTest(testDispatcher) {
            // State is Loading
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            testDispatcher.scheduler.advanceUntilIdle()

            val testUser = User(id = 1L, name = "TestUser", selected = true)

            // Call validation
            val result = testedPresenter.validateInputUserNameString(testUser)

            // Should return null without calling interactor
            assertNull(result)
            verify(exactly = 0) {
                mockSettingsInteractor.validateInputUserName(
                    user = any(),
                    existingUsers = any(),
                )
            }
        }

    @Test
    fun `validateInputUserNameString when state is Error returns null without calling interactor`() =
        runTest(testDispatcher) {
            // Set up Error state
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error(errorCode = "789")
            generalSettingsFlow.emit(Output.Error(errorCode = DomainError.DATABASE_FETCH_FAILED, exception = Exception("Test error")))
            testDispatcher.scheduler.advanceUntilIdle()

            val testUser = User(id = 1L, name = "TestUser", selected = true)

            // Call validation
            val result = testedPresenter.validateInputUserNameString(testUser)

            // Should return null without calling interactor
            assertNull(result)
            verify(exactly = 0) {
                mockSettingsInteractor.validateInputUserName(
                    user = any(),
                    existingUsers = any(),
                )
            }
        }
}

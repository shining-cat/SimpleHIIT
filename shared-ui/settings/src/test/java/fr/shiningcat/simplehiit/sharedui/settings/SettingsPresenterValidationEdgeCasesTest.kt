/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test

/**
 * Edge case tests for SettingsPresenter validation methods.
 * Tests defensive programming branches that return null when state is not Nominal.
 * These cover the "we don't really expect to land in here" branches to improve coverage.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterValidationEdgeCasesTest : SettingsPresenterTestBase() {
    @Test
    fun `validatePeriodLengthInput when state is Loading returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Loading state
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("30")

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }

    @Test
    fun `validatePeriodLengthInput when state is Error returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Error state
            every { mockMapper.map(any()) } returns SettingsViewState.Error(DomainError.DATABASE_FETCH_FAILED.code)
            generalSettingsFlow.emit(Output.Error(DomainError.DATABASE_FETCH_FAILED, Exception("Test")))
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("30")

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }

    @Test
    fun `validateInputPeriodStartCountdown when state is Loading returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Loading state
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("5")

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }

    @Test
    fun `validateInputPeriodStartCountdown when state is Error returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Error state
            every { mockMapper.map(any()) } returns SettingsViewState.Error(DomainError.DATABASE_FETCH_FAILED.code)
            generalSettingsFlow.emit(Output.Error(DomainError.DATABASE_FETCH_FAILED, Exception("Test")))
            testDispatcher.scheduler.advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("5")

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }

    @Test
    fun `validateInputUserNameString when state is Loading returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Loading state
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.emit(Output.Success(testGeneralSettings()))
            testDispatcher.scheduler.advanceUntilIdle()

            val testUser = User(id = 1L, name = "Test User", selected = true)
            val result = testedPresenter.validateInputUserNameString(testUser)

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }

    @Test
    fun `validateInputUserNameString when state is Error returns null`() =
        runTest(testDispatcher) {
            // Mock mapper to return Error state
            every { mockMapper.map(any()) } returns SettingsViewState.Error(DomainError.DATABASE_FETCH_FAILED.code)
            generalSettingsFlow.emit(Output.Error(DomainError.DATABASE_FETCH_FAILED, Exception("Test")))
            testDispatcher.scheduler.advanceUntilIdle()

            val testUser = User(id = 1L, name = "Test User", selected = true)
            val result = testedPresenter.validateInputUserNameString(testUser)

            // Should return null when state is not Nominal (defensive programming)
            assertNull(result)
        }
}

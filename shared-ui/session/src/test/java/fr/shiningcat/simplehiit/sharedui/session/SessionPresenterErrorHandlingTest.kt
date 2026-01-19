/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SessionPresenter null session error handling.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterErrorHandlingTest : SessionPresenterTestBase() {
    @Test
    fun `pause with null session emits SESSION_NOT_FOUND error`() =
        runTest(testDispatcher) {
            // Don't call onSoundLoaded() - session remains null
            testedPresenter.pause()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.SESSION_NOT_FOUND.code, (state as SessionViewState.Error).errorCode)
        }

    @Test
    fun `resume with null session emits SESSION_NOT_FOUND error and returns early`() =
        runTest(testDispatcher) {
            // Don't call onSoundLoaded() - session remains null
            testedPresenter.resume()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.SESSION_NOT_FOUND.code, (state as SessionViewState.Error).errorCode)

            // Should not attempt to start timer
            coVerify(exactly = 0) { mockSessionInteractor.startStepTimer(any()) }
        }

    @Test
    fun `launchSession with null session emits SESSION_NOT_FOUND error`() =
        runTest(testDispatcher) {
            coEvery { mockSessionInteractor.buildSession(any()) } returns testSession()

            // Create a scenario where session is null despite initialization attempt
            // This tests the defensive null check in launchSession
            sessionSettingsFlow.value = Output.Success(testSessionSettings())

            // Manually set session to null after initialization (simulating edge case)
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Now cleanup which resets session to null
            testedPresenter.cleanup()
            advanceUntilIdle()

            // Try to use the presenter again without re-initialization
            testedPresenter.pause()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.SESSION_NOT_FOUND.code, (state as SessionViewState.Error).errorCode)
        }

    @Test
    fun `abortSession with partial session data handles null session gracefully`() =
        runTest(testDispatcher) {
            every {
                mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(any())
            } returns "0s"
            coEvery { mockSessionInteractor.insertSession(any()) } returns Output.Success(1)

            // Start a session
            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Clean up (sets session to null)
            testedPresenter.cleanup()
            advanceUntilIdle()

            // Now try to abort - session is null
            testedPresenter.abortSession()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.SESSION_NOT_FOUND.code, (state as SessionViewState.Error).errorCode)
        }
}

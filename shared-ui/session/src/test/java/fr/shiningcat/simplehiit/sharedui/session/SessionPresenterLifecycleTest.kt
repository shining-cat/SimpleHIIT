/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import io.mockk.coEvery
import io.mockk.coVerify
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
 * Tests for SessionPresenter lifecycle methods including resetAndStart() and cleanup().
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterLifecycleTest : SessionPresenterTestBase() {
    @Test
    fun `resetAndStart clears state and re-initializes session`() =
        runTest(testDispatcher) {
            val initialSettings = testSessionSettings()
            sessionSettingsFlow.value = Output.Success(initialSettings)
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "10s",
                    sessionRemainingPercentage = 0.5f,
                    countDown = null,
                )

            // Start initial session
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Simulate some progress
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 80000L)
            advanceUntilIdle()

            // Pause to set dialog state
            testedPresenter.pause()
            advanceUntilIdle()

            // Verify pause dialog is showing
            val dialogBeforeReset = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.Pause, dialogBeforeReset)

            // Reset the timer state for re-initialization
            timerStateFlow.value = StepTimerState()

            // Call resetAndStart
            testedPresenter.resetAndStart()
            advanceUntilIdle()

            // Verify state was reset to Loading
            val screenStates = mutableListOf<SessionViewState>()
            val job =
                launch {
                    testedPresenter.screenViewState.take(2).toList(screenStates)
                }
            advanceUntilIdle()
            job.cancel()

            // Should have emitted Loading state during reset
            assertTrue(
                screenStates.any { it is SessionViewState.Loading },
                "Expected Loading state after resetAndStart",
            )

            // Verify dialog was reset to None
            val dialogAfterReset = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.None, dialogAfterReset)

            // Verify timer state was reset
            coVerify { mockSessionInteractor.resetTimerState() }

            // Verify session was re-initialized
            coVerify(atLeast = 2) { mockSessionInteractor.buildSession(any()) }
            coVerify(atLeast = 2) { mockSessionInteractor.startStepTimer(any()) }
        }

    @Test
    fun `cleanup cancels all jobs and resets internal state`() =
        runTest(testDispatcher) {
            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "10s",
                    sessionRemainingPercentage = 0.5f,
                    countDown = null,
                )

            // Start session
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Verify session started
            coVerify { mockSessionInteractor.startStepTimer(100000L) }

            // Simulate some progress
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 80000L)
            advanceUntilIdle()

            // Call cleanup
            testedPresenter.cleanup()
            advanceUntilIdle()

            // Verify timer state was reset
            coVerify { mockSessionInteractor.resetTimerState() }

            // After cleanup, attempting to pause should emit SESSION_NOT_FOUND error
            // because session was reset to null
            testedPresenter.pause()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.SESSION_NOT_FOUND.code, (state as SessionViewState.Error).errorCode)
        }
}

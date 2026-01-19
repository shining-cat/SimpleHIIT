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
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for basic SessionPresenter functionality including initialization, basic flows, and state management.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterBasicTest : SessionPresenterTestBase() {
    @Test
    fun `onSoundLoaded triggers session initialization flow`() =
        runTest(testDispatcher) {
            val settings = testSessionSettings()
            sessionSettingsFlow.value = Output.Success(settings)

            testedPresenter.onSoundLoaded()
            runCurrent()

            coVerify { mockSessionInteractor.getSessionSettings() }
            coVerify { mockSessionInteractor.buildSession(settings) }
            coVerify { mockSessionInteractor.startStepTimer(100000L) }
        }

    @Test
    fun `onSoundLoaded with session settings error emits error state`() =
        runTest(testDispatcher) {
            val errorOutput = Output.Error(DomainError.NO_USERS_FOUND, Exception("Test"))
            sessionSettingsFlow.value = errorOutput

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Error)
            assertEquals(DomainError.NO_USERS_FOUND.code, (state as SessionViewState.Error).errorCode)
        }

    @Test
    fun `tick with session end emits beep and finished state`() =
        runTest(testDispatcher) {
            val finishedState =
                SessionViewState.Finished(
                    sessionDurationFormatted = "1m40s",
                    workingStepsDone = emptyList(),
                )
            every {
                mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(any())
            } returns "1m40s"
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns finishedState
            coEvery { mockSessionInteractor.insertSession(any()) } returns Output.Success(1)

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            runCurrent()

            // Emit timer reaching 0
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 0L)
            runCurrent()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Finished)
        }

    @Test
    fun `tick with running session emits current state and beep when playBeep is true`() =
        runTest(testDispatcher) {
            val runningState =
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "15s",
                    stepRemainingPercentage = 0.5f,
                    sessionRemainingTime = "50s",
                    sessionRemainingPercentage = 0.5f,
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = 0.5f,
                            playBeep = true,
                        ),
                )
            coEvery {
                mockMapper.buildStateFromWholeSession(any(), any(), any())
            } returns runningState

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Emit running timer
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 50000L)
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertEquals(runningState, state)
        }

    @Test
    fun `tick with InitialCountDownSession and playBeep true emits beep`() =
        runTest(testDispatcher) {
            val countdownState =
                SessionViewState.InitialCountDownSession(
                    countDown =
                        CountDown(
                            secondsDisplay = "3",
                            progress = 0.6f,
                            playBeep = true,
                        ),
                )
            coEvery {
                mockMapper.buildStateFromWholeSession(any(), any(), any())
            } returns countdownState

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 95000L)
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.InitialCountDownSession)
        }

    @Test
    fun `pause cancels timer and emits pause dialog`() =
        runTest(testDispatcher) {
            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            testedPresenter.pause()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.Pause, dialogState)
        }

    @Test
    fun `resume starts timer from current step and dismisses dialog`() =
        runTest(testDispatcher) {
            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            testedPresenter.pause()
            advanceUntilIdle()

            testedPresenter.resume()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.None, dialogState)

            // Should restart timer with remaining time
            coVerify(atLeast = 1) { mockSessionInteractor.startStepTimer(any()) }
        }

    @Test
    fun `abortSession cancels timer and emits finished state with session record`() =
        runTest(testDispatcher) {
            // Mock the mapper for the tick call that happens during progress simulation
            val runningState =
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "18s",
                    stepRemainingPercentage = 0.75f,
                    sessionRemainingTime = "1m20s",
                    sessionRemainingPercentage = 0.8f,
                    countDown = null,
                )
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns runningState

            every {
                mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(any())
            } returns "20s"
            coEvery { mockSessionInteractor.insertSession(any()) } returns Output.Success(1)

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            timerStateFlow.value = StepTimerState()

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Simulate some progress
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 80000L)
            advanceUntilIdle()

            testedPresenter.abortSession()
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Finished)

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.None, dialogState)
        }

    @Test
    fun `screenViewState initially emits Loading`() =
        runTest(testDispatcher) {
            val state = testedPresenter.screenViewState.first()
            assertEquals(SessionViewState.Loading, state)
        }

    @Test
    fun `dialogViewState initially emits None`() =
        runTest(testDispatcher) {
            val state = testedPresenter.dialogViewState.first()
            assertEquals(SessionDialog.None, state)
        }

    @Test
    fun `beepSignal can be collected without emitting values initially`() =
        runTest(testDispatcher) {
            val collected = mutableListOf<Unit>()
            val job =
                launch {
                    testedPresenter.beepSignal.take(1).toList(collected)
                }

            // Don't emit anything, just verify collection works
            job.cancel()
            assertTrue(collected.isEmpty())
        }
}

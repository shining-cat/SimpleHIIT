/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SessionPresenter step transition logic including index updates and beep sounds.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterStepTransitionTest : SessionPresenterTestBase() {
    @Test
    fun `tick increments step index when timer reaches step boundary`() =
        runTest(testDispatcher) {
            // Create session with specific step boundaries we can test
            val testSessionWithBoundaries =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 25000L, // Step ends at 25s remaining
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.WorkStep(
                                durationMs = 20000L,
                                remainingSessionDurationMsAfterMe = 5000L, // Step ends at 5s remaining
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.RestStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L, // Final step
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 30000L,
                    beepSoundCountDownActive = true,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns testSessionWithBoundaries

            // Mock mapper to return different states for each step
            val prepareState =
                SessionViewState.InitialCountDownSession(
                    countDown = CountDown("5", 1.0f, false),
                )
            val workState =
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "20s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "25s",
                    sessionRemainingPercentage = 0.83f,
                    countDown = null,
                )
            coEvery { mockMapper.buildStateFromWholeSession(any(), 0, any()) } returns prepareState
            coEvery { mockMapper.buildStateFromWholeSession(any(), 1, any()) } returns workState

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Emit timer at 26s (still in PrepareStep - step 0)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 26000L)
            advanceUntilIdle()

            // Should still be in prepare step (step 0)
            coVerify { mockMapper.buildStateFromWholeSession(any(), 0, any()) }

            // Emit timer at 25s (boundary - should transition to WorkStep - step 1)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 25000L)
            advanceUntilIdle()

            // Should now be in work step (step 1)
            coVerify { mockMapper.buildStateFromWholeSession(any(), 1, any()) }
        }

    @Test
    fun `tick plays beep sound on step transition when beepSoundCountDownActive is true`() =
        runTest(testDispatcher) {
            val testSessionWithBeep =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 5000L,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 10000L,
                    beepSoundCountDownActive = true, // Beep enabled
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns testSessionWithBeep
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "5s",
                    sessionRemainingPercentage = 0.5f,
                    countDown = null,
                )

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            runCurrent()

            // Collect beep signals
            val beeps = mutableListOf<Unit>()
            val beepJob =
                launch {
                    testedPresenter.beepSignal.take(1).toList(beeps)
                }

            // Emit timer crossing step boundary (5s = end of PrepareStep)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 5000L)
            runCurrent()

            // Should have emitted beep for step transition
            assertTrue(beeps.isNotEmpty(), "Expected beep signal on step transition")

            beepJob.cancel()
        }

    @Test
    fun `tick does not play beep on step transition when beepSoundCountDownActive is false`() =
        runTest(testDispatcher) {
            val testSessionWithoutBeep =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 5000L,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 10000L,
                    beepSoundCountDownActive = false, // Beep disabled
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns testSessionWithoutBeep
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "5s",
                    sessionRemainingPercentage = 0.5f,
                    countDown = null,
                )

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            runCurrent()

            // Start collecting beep signals (won't block since we use runCurrent)
            val beeps = mutableListOf<Unit>()

            // Emit timer crossing step boundary (5s = end of PrepareStep)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 5000L)
            runCurrent()

            // Try to consume any beeps that might have been emitted
            val tryReceiveResult = testedPresenter.beepSignal.replayCache
            beeps.addAll(tryReceiveResult)

            // Should NOT have emitted beep since beepSoundCountDownActive is false
            assertTrue(beeps.isEmpty(), "Expected no beep signal when beepSoundCountDownActive is false")
        }
}

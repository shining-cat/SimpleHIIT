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
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SessionPresenter session end edge cases including duration calculation and session recording.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterSessionEndTest : SessionPresenterTestBase() {
    @Test
    fun `emitSessionEndState decrements index when last step is RestStep`() =
        runTest(testDispatcher) {
            // Session with 2 complete cycles + ending on a RestStep
            val sessionEndingWithRest =
                Session(
                    steps =
                        listOf(
                            // First complete cycle
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 15000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.RestStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 10000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            // Second cycle - ending on RestStep
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 5000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.RestStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 20000L,
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns sessionEndingWithRest
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.WORK,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "10s",
                    sessionRemainingPercentage = 1.0f,
                    countDown = null,
                )
            // Expected: 2 WorkSteps * 5000ms + 1 RestStep * 5000ms = 15000ms (second RestStep excluded)
            every { mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(15000L) } returns "15s"
            coEvery { mockSessionInteractor.insertSession(any()) } returns Output.Success(1)

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            runCurrent()

            // Complete first cycle
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 17000L)
            runCurrent()
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 10000L) // Complete first WorkStep + RestStep
            runCurrent()

            // Progress through second WorkStep
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 7000L)
            runCurrent()

            // Complete second WorkStep and enter final RestStep
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 5000L)
            runCurrent()

            // End the session during final RestStep
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 0L)
            runCurrent()

            // Verify session was inserted with 2 WorkSteps + 1 RestStep (final RestStep excluded)
            coVerify {
                mockSessionInteractor.insertSession(
                    match { sessionRecord ->
                        sessionRecord.durationMs == 15000L // 2 work + 1 rest, excluding final rest
                    },
                )
            }
        }

    @Test
    fun `emitSessionEndState calculates zero duration when no rest steps completed`() =
        runTest(testDispatcher) {
            val sessionOnlyWork =
                Session(
                    steps =
                        listOf(
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 5000L,
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns sessionOnlyWork
            every { mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(0L) } returns "0s"
            coEvery { mockSessionInteractor.insertSession(any()) } returns Output.Success(1)

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Abort immediately (no rest steps done)
            testedPresenter.abortSession()
            advanceUntilIdle()

            // Session should have 0 duration since no complete work+rest cycles
            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Finished)
            assertEquals("0s", (state as SessionViewState.Finished).sessionDurationFormatted)

            // Should NOT insert session with 0 duration
            coVerify(exactly = 0) { mockSessionInteractor.insertSession(any()) }
        }

    @Test
    fun `emitSessionEndState calculates zero duration when no work steps completed`() =
        runTest(testDispatcher) {
            val sessionOnlyRest =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 0L,
                                countDownLengthMs = 3000L,
                            ),
                        ),
                    durationMs = 5000L,
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns sessionOnlyRest
            every { mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(0L) } returns "0s"

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Abort immediately (no work steps done)
            testedPresenter.abortSession()
            advanceUntilIdle()

            // Session should have 0 duration since no work steps
            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Finished)
            assertEquals("0s", (state as SessionViewState.Finished).sessionDurationFormatted)

            // Should NOT insert session with 0 duration
            coVerify(exactly = 0) { mockSessionInteractor.insertSession(any()) }
        }

    @Test
    fun `emitSessionEndState does not insert session record when actualSessionLength is zero`() =
        runTest(testDispatcher) {
            val emptySession =
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
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns emptySession
            every { mockSessionInteractor.formatLongDurationMsAsSmallestHhMmSsString(0L) } returns "0s"

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

            // Abort before any steps complete
            testedPresenter.abortSession()
            advanceUntilIdle()

            // Should NOT call insertSession when actualSessionLength is 0
            coVerify(exactly = 0) { mockSessionInteractor.insertSession(any()) }

            // But should still emit Finished state
            val state = testedPresenter.screenViewState.first()
            assertTrue(state is SessionViewState.Finished)
        }
}

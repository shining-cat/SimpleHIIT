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
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test

/**
 * Tests for SessionPresenter pause/resume edge cases including WorkStep index handling.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterPauseResumeTest : SessionPresenterTestBase() {
    @Test
    fun `pause during WorkStep decrements index to restart from preceding step`() =
        runTest(testDispatcher) {
            val sessionWithMultipleSteps =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 15000L,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 10000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.RestStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 5000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
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
                    durationMs = 20000L,
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns sessionWithMultipleSteps
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

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            runCurrent()

            // Progress to PrepareStep completion
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 16000L)
            runCurrent()

            // Transition to first WorkStep (index 1)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 15000L)
            runCurrent()

            // Verify we're in WorkStep (index 1)
            coVerify { mockMapper.buildStateFromWholeSession(any(), 1, any()) }

            // Pause during WorkStep
            testedPresenter.pause()
            runCurrent()

            // Resume should restart from decremented index (back to PrepareStep - index 0)
            // Verify startStepTimer is called with time for index 0: PrepareStep(5000) + remaining(15000) = 20000
            testedPresenter.resume()
            runCurrent()

            coVerify { mockSessionInteractor.startStepTimer(20000L) }
        }

    @Test
    fun `pause during RestStep does not decrement index`() =
        runTest(testDispatcher) {
            val sessionWithMultipleSteps =
                Session(
                    steps =
                        listOf(
                            SessionStep.PrepareStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 15000L,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.WorkStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 10000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
                                countDownLengthMs = 3000L,
                            ),
                            SessionStep.RestStep(
                                durationMs = 5000L,
                                remainingSessionDurationMsAfterMe = 5000L,
                                exercise = Exercise.LungesBasic,
                                side = ExerciseSide.NONE,
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
                    durationMs = 20000L,
                    beepSoundCountDownActive = false,
                    users = listOf(testUser),
                )

            coEvery { mockSessionInteractor.buildSession(any()) } returns sessionWithMultipleSteps
            coEvery { mockMapper.buildStateFromWholeSession(any(), any(), any()) } returns
                SessionViewState.RunningNominal(
                    periodType = RunningSessionStepType.REST,
                    displayedExercise = Exercise.LungesBasic,
                    side = ExerciseSide.NONE,
                    stepRemainingTime = "5s",
                    stepRemainingPercentage = 1.0f,
                    sessionRemainingTime = "10s",
                    sessionRemainingPercentage = 0.5f,
                    countDown = null,
                )

            sessionSettingsFlow.value = Output.Success(testSessionSettings())
            testedPresenter.onSoundLoaded()
            runCurrent()

            // Progress through PrepareStep and WorkStep
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 11000L)
            runCurrent()

            // Transition to RestStep (index 2)
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 10000L)
            runCurrent()

            // Verify we're in RestStep (index 2)
            coVerify { mockMapper.buildStateFromWholeSession(any(), 2, any()) }

            // Pause during RestStep
            testedPresenter.pause()
            runCurrent()

            // Resume should restart from same index (RestStep - index 2)
            // Verify startStepTimer is called with time for index 2: RestStep(5000) + remaining(5000) = 10000
            testedPresenter.resume()
            runCurrent()

            coVerify { mockSessionInteractor.startStepTimer(10000L) }
        }
}

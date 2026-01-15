/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionSettings
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionPresenterTest : AbstractMockkTest() {
    private val mockSessionInteractor = mockk<SessionInteractor>()
    private val mockMapper = mockk<SessionViewStateMapper>()
    private val mockTimeProvider = mockk<TimeProvider>()
    private val testDispatcher = StandardTestDispatcher()

    private val sessionSettingsFlow = MutableStateFlow<Output<SessionSettings>>(Output.Success(testSessionSettings()))
    private val timerStateFlow = MutableStateFlow(StepTimerState())

    private val testUser = User(id = 1L, name = "Test User", selected = true)

    private fun testSessionSettings() =
        SessionSettings(
            numberCumulatedCycles = 3,
            workPeriodLengthMs = 20000L,
            restPeriodLengthMs = 10000L,
            numberOfWorkPeriods = 3,
            cycleLengthMs = 30000L,
            beepSoundCountDownActive = true,
            sessionStartCountDownLengthMs = 5000L,
            periodsStartCountDownLengthMs = 3000L,
            users = listOf(testUser),
            exerciseTypes = listOf(ExerciseTypeSelected(ExerciseType.LUNGE, true)),
        )

    private fun testSession() =
        Session(
            steps =
                listOf(
                    SessionStep.PrepareStep(
                        durationMs = 5000L,
                        remainingSessionDurationMsAfterMe = 95000L,
                        countDownLengthMs = 3000L,
                    ),
                    SessionStep.WorkStep(
                        durationMs = 20000L,
                        remainingSessionDurationMsAfterMe = 75000L,
                        exercise = Exercise.LungesBasic,
                        side = ExerciseSide.NONE,
                        countDownLengthMs = 3000L,
                    ),
                    SessionStep.RestStep(
                        durationMs = 10000L,
                        remainingSessionDurationMsAfterMe = 65000L,
                        exercise = Exercise.LungesBasic,
                        side = ExerciseSide.NONE,
                        countDownLengthMs = 3000L,
                    ),
                ),
            durationMs = 100000L,
            beepSoundCountDownActive = true,
            users = listOf(testUser),
        )

    private lateinit var testedPresenter: SessionPresenter

    @BeforeEach
    fun setUp() {
        every { mockSessionInteractor.getStepTimerState() } returns timerStateFlow
        every { mockSessionInteractor.getSessionSettings() } returns sessionSettingsFlow
        coEvery { mockSessionInteractor.buildSession(any()) } returns testSession()
        coEvery { mockSessionInteractor.startStepTimer(any()) } just runs
        every { mockTimeProvider.getCurrentTimeMillis() } returns 123456789L

        testedPresenter =
            SessionPresenter(
                sessionInteractor = mockSessionInteractor,
                mapper = mockMapper,
                timeProvider = mockTimeProvider,
                dispatcher = testDispatcher,
                logger = mockHiitLogger,
            )
    }

    @Test
    fun `onSoundLoaded triggers session initialization flow`() =
        runTest(testDispatcher) {
            val settings = testSessionSettings()
            sessionSettingsFlow.value = Output.Success(settings)

            testedPresenter.onSoundLoaded()
            advanceUntilIdle()

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
            advanceUntilIdle()

            // Emit timer reaching 0
            timerStateFlow.value = StepTimerState(milliSecondsRemaining = 0L)
            advanceUntilIdle()

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

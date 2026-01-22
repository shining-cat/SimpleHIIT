/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.session

import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
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
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.runs
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach

/**
 * Base class for SessionPresenter tests providing common setup, mocks, and helper methods.
 */
@OptIn(ExperimentalCoroutinesApi::class)
abstract class SessionPresenterTestBase : AbstractMockkTest() {
    protected val mockSessionInteractor = mockk<SessionInteractor>()
    protected val mockMapper = mockk<SessionViewStateMapper>()
    protected val mockTimeProvider = mockk<TimeProvider>()
    protected val testDispatcher = StandardTestDispatcher()

    protected val sessionSettingsFlow = MutableStateFlow<Output<SessionSettings>>(Output.Success(testSessionSettings()))
    protected val timerStateFlow = MutableStateFlow(StepTimerState())

    protected val testUser = User(id = 1L, name = "Test User", selected = true)

    protected lateinit var testedPresenter: SessionPresenter

    protected fun testSessionSettings() =
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

    protected fun testSession() =
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

    @BeforeEach
    fun setUp() {
        every { mockSessionInteractor.getStepTimerState() } returns timerStateFlow
        every { mockSessionInteractor.getSessionSettings() } returns sessionSettingsFlow
        coEvery { mockSessionInteractor.buildSession(any()) } returns testSession()
        coEvery { mockSessionInteractor.startStepTimer(any()) } just runs
        every { mockSessionInteractor.resetTimerState() } just runs
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

    @AfterEach
    fun tearDown() {
        // Cancel all presenter coroutines to prevent hanging tests
        testedPresenter.cleanup()
    }
}

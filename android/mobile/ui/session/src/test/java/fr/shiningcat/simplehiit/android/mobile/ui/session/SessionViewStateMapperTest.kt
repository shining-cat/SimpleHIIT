package fr.shiningcat.simplehiit.android.mobile.ui.session

import fr.shiningcat.simplehiit.android.mobile.ui.session.SessionViewState.InitialCountDownSession
import fr.shiningcat.simplehiit.domain.common.models.AsymmetricalExerciseSideOrder
import fr.shiningcat.simplehiit.domain.common.models.DurationStringFormatter
import fr.shiningcat.simplehiit.domain.common.models.Exercise
import fr.shiningcat.simplehiit.domain.common.models.ExerciseSide
import fr.shiningcat.simplehiit.domain.common.models.Session
import fr.shiningcat.simplehiit.domain.common.models.SessionStep
import fr.shiningcat.simplehiit.domain.common.models.StepTimerState
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SessionViewStateMapperTest : AbstractMockkTest() {
    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val durationStringFormatter = DurationStringFormatter()

    @BeforeEach
    fun setUpMock() {
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                any(),
                any(),
            )
        } returns MOCK_DURATION_STRING
    }

    @ParameterizedTest(name = "{index} -> correctly maps {0} to {1}")
    @MethodSource("mapToViewStateArguments")
    fun `mapper produces expected viewStates`(
        sessionMapperTestParameter: SessionMapperTestParameter,
        expectedViewStateOutput: SessionViewState,
    ) = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testedMapper =
            SessionViewStateMapper(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
                defaultDispatcher = testDispatcher,
                hiitLogger = mockHiitLogger,
            )
        //
        val result =
            testedMapper.buildStateFromWholeSession(
                session = sessionMapperTestParameter.session,
                currentSessionStepIndex = sessionMapperTestParameter.currentSessionStepIndex,
                currentStepTimerState = sessionMapperTestParameter.currentStepTimerState,
                durationStringFormatter = durationStringFormatter,
            )
        //
        assertEquals(expectedViewStateOutput, result)
    }

    // //////////////////////
    private companion object {
        private const val MOCK_DURATION_STRING = "This is a test duration string"
        private val testSession =
            Session(
                steps =
                    listOf(
                        SessionStep.PrepareStep(
                            durationMs = 5000L,
                            remainingSessionDurationMsAfterMe = 765000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LungesSideToCurtsy,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 730000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LungesSideToCurtsy,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 680000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LungesSideToCurtsy,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 645000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LungesSideToCurtsy,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 595000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LyingSupermanTwist,
                            side = ExerciseSide.NONE,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 560000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LyingSupermanTwist,
                            side = ExerciseSide.NONE,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 510000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.PlankMountainClimber,
                            side = ExerciseSide.NONE,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 475000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.PlankMountainClimber,
                            side = ExerciseSide.NONE,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 425000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.CrabKicks,
                            side = ExerciseSide.NONE,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 390000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.CrabKicks,
                            side = ExerciseSide.NONE,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 340000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LungesBackKick,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 305000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LungesBackKick,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 255000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LungesBackKick,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 220000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LungesBackKick,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 170000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LyingSideLegLift,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 135000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LyingSideLegLift,
                            side = AsymmetricalExerciseSideOrder.FIRST.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 85000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.RestStep(
                            exercise = Exercise.LyingSideLegLift,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 35000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 50000L,
                            countDownLengthMs = 5000L,
                        ),
                        SessionStep.WorkStep(
                            exercise = Exercise.LyingSideLegLift,
                            side = AsymmetricalExerciseSideOrder.SECOND.side,
                            durationMs = 50000L,
                            durationFormatted = MOCK_DURATION_STRING,
                            remainingSessionDurationMsAfterMe = 0L,
                            countDownLengthMs = 5000L,
                        ),
                    ),
                durationMs = 800000L,
                beepSoundCountDownActive = true,
                users = listOf(User(name = "user test 1"), User(name = "user test 2")),
            )

        @JvmStatic
        fun mapToViewStateArguments(): Stream<Arguments> =
            Stream.of(
                // normal ongoing prepare step
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 0,
                        currentStepTimerState =
                            StepTimerState(
                                milliSecondsRemaining = 769000L,
                                totalMilliSeconds = 800000L,
                            ),
                    ),
                    InitialCountDownSession(
                        countDown =
                            CountDown(
                                secondsDisplay = "4",
                                // 4/5 to float
                                progress = .8f,
                                playBeep = true,
                            ),
                    ),
                ),
                // normal ongoing work step before countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 4,
                        currentStepTimerState =
                            StepTimerState(
                                milliSecondsRemaining = 605000L,
                                totalMilliSeconds = 800000L,
                            ),
                    ),
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.WORK,
                        displayedExercise = Exercise.LungesSideToCurtsy,
                        side = AsymmetricalExerciseSideOrder.SECOND.side,
                        // verify formatter is called with 10000L,
                        stepRemainingTime = MOCK_DURATION_STRING,
                        stepRemainingPercentage = .2f,
                        // verify formatter is called with (595000L + 10000L)
                        sessionRemainingTime = MOCK_DURATION_STRING,
                        // 605000L / 800000L = 0.75625
                        sessionRemainingPercentage = .75625f,
                        countDown = null,
                    ),
                ),
                // normal ongoing work step during countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 4,
                        currentStepTimerState =
                            StepTimerState(
                                milliSecondsRemaining = 596000L,
                                totalMilliSeconds = 800000L,
                            ),
                    ),
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.WORK,
                        displayedExercise = Exercise.LungesSideToCurtsy,
                        side = AsymmetricalExerciseSideOrder.SECOND.side,
                        // verify formatter is called with 1000L,
                        stepRemainingTime = MOCK_DURATION_STRING,
                        stepRemainingPercentage = .02f,
                        // verify formatter is called with (595000L + 1000L)
                        sessionRemainingTime = MOCK_DURATION_STRING,
                        // 596000L / 800000L
                        sessionRemainingPercentage = .745f,
                        countDown =
                            CountDown(
                                secondsDisplay = "1",
                                // 1/5 to float
                                progress = .2f,
                                playBeep = true,
                            ),
                    ),
                ),
                // normal ongoing rest step before countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 7,
                        currentStepTimerState =
                            StepTimerState(
                                milliSecondsRemaining = 482000L,
                                totalMilliSeconds = 800000L,
                            ),
                    ),
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.REST,
                        displayedExercise = Exercise.PlankMountainClimber,
                        side = ExerciseSide.NONE,
                        // verify formatter is called with 7000L,
                        stepRemainingTime = MOCK_DURATION_STRING,
                        stepRemainingPercentage = .2f,
                        // verify formatter is called with (475000L + 7000L)
                        sessionRemainingTime = MOCK_DURATION_STRING,
                        // 482000L / 800000L
                        sessionRemainingPercentage = .6025f,
                        countDown = null,
                    ),
                ),
                // normal ongoing rest step during countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 7,
                        currentStepTimerState =
                            StepTimerState(
                                milliSecondsRemaining = 480000L,
                                totalMilliSeconds = 800000L,
                            ),
                    ),
                    SessionViewState.RunningNominal(
                        periodType = RunningSessionStepType.REST,
                        displayedExercise = Exercise.PlankMountainClimber,
                        side = ExerciseSide.NONE,
                        // verify formatter is called with 5000L,
                        stepRemainingTime = MOCK_DURATION_STRING,
                        stepRemainingPercentage = .14285715f,
                        // verify formatter is called with (475000L + 5000L)
                        sessionRemainingTime = MOCK_DURATION_STRING,
                        // 480000L / 800000L
                        sessionRemainingPercentage = .6f,
                        countDown =
                            CountDown(
                                secondsDisplay = "5",
                                // 5/5 to float
                                progress = 1f,
                                playBeep = true,
                            ),
                    ),
                ),
            )
    }

    internal data class SessionMapperTestParameter(
        val session: Session,
        val currentSessionStepIndex: Int,
        val currentStepTimerState: StepTimerState,
    )
}

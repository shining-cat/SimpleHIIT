package fr.shining_cat.simplehiit.ui.session

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.*
import fr.shining_cat.simplehiit.domain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.ui.session.SessionViewState.InitialCountDownSession
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
internal class SessionMapperTest : AbstractMockkTest() {

    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val durationStringFormatter = DurationStringFormatter(
        hoursMinutesSeconds = "",
        hoursMinutesNoSeconds = "",
        hoursNoMinutesNoSeconds = "",
        minutesSeconds = "",
        minutesNoSeconds = "",
        seconds = "",
    )

    @BeforeEach
    fun setUpMock() {
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                any(),
                any()
            )
        } returns mockDurationString

    }

    @ParameterizedTest(name = "{index} -> correctly maps {0} to {1}")
    @MethodSource("mapToViewStateArguments")
    fun `mapper produces expected viewStates`(
        sessionMapperTestParameter: SessionMapperTestParameter,
        expectedViewStateOutput: SessionViewState
    ) = runTest {
        val testDispatcher = StandardTestDispatcher(testScheduler)
        val testedMapper = SessionMapper(
            formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            defaultDispatcher = testDispatcher,
            hiitLogger = mockHiitLogger
        )
        //
        val result = testedMapper.buildState(
            session = sessionMapperTestParameter.session,
            currentSessionStepIndex = sessionMapperTestParameter.currentSessionStepIndex,
            currentStepTimerState = sessionMapperTestParameter.currentStepTimerState,
            durationStringFormatter = durationStringFormatter
        )
        //
        assertEquals(expectedViewStateOutput, result)
    }

    ////////////////////////
    private companion object {

        private const val mockDurationString = "This is a test duration string"
        private val testSession = Session(
            steps = listOf(
                SessionStep.PrepareStep(
                    durationMs = 5000L,
                    remainingSessionDurationMsAfterMe = 765000L,
                    countDownLengthMs = 5000L,
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 730000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 680000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 645000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LungesSideToCurtsy,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 595000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LyingSupermanTwist,
                    side = ExerciseSide.NONE,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 560000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LyingSupermanTwist,
                    side = ExerciseSide.NONE,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 510000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.PlankMountainClimber,
                    side = ExerciseSide.NONE,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 475000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.PlankMountainClimber,
                    side = ExerciseSide.NONE,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 425000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.CrabKicks,
                    side = ExerciseSide.NONE,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 390000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.CrabKicks,
                    side = ExerciseSide.NONE,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 340000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LungesBackKick,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 305000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LungesBackKick,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 255000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LungesBackKick,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 220000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LungesBackKick,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 170000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LyingSideLegLift,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 135000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LyingSideLegLift,
                    side = AsymmetricalExerciseSideOrder.FIRST.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 85000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.RestStep(
                    exercise = Exercise.LyingSideLegLift,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 35000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 50000L,
                    countDownLengthMs = 5000L
                ),
                SessionStep.WorkStep(
                    exercise = Exercise.LyingSideLegLift,
                    side = AsymmetricalExerciseSideOrder.SECOND.side,
                    durationMs = 50000L,
                    durationFormatted = mockDurationString,
                    remainingSessionDurationMsAfterMe = 0L,
                    countDownLengthMs = 5000L
                )
            ),
            durationMs = 800000L,
            durationFormatted = mockDurationString,
            beepSoundCountDownActive = true
        )

        @JvmStatic
        fun mapToViewStateArguments() =
            Stream.of(
                //countdown length shorter than prepare step length
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 0,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 9,
                            totalSeconds = 10
                        )
                    ),
                    SessionViewState.Error(errorCode="0501 - The countdown length is shorter than the prepare step?")
                ),
                //normal ongoing prepare step
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 0,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 4,
                            totalSeconds = 5
                        )
                    ),
                    InitialCountDownSession(
                        countDown = CountDown(
                            secondsDisplay = "4",
                            progress = .8f, // 4/5 to float
                            playBeep = true
                        )
                    )
                ),
                //normal ongoing work step before countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 4,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 10,
                            totalSeconds = 50
                        )
                    ),
                    SessionViewState.WorkNominal(
                        currentExercise = Exercise.LungesSideToCurtsy,
                        side = AsymmetricalExerciseSideOrder.SECOND.side,
                        exerciseRemainingTime = mockDurationString, // verify formatter is called with 10000L,
                        exerciseRemainingPercentage = .2f,
                        sessionRemainingTime = mockDurationString, // verify formatter is called with (595000L + 10000L)
                        sessionRemainingPercentage = .75625f, // (595000L + 10000L) / 800000L = 0.75625
                        countDown = null
                    )
                ),
                //normal ongoing work step during countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 4,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 1,
                            totalSeconds = 50
                        )
                    ),
                    SessionViewState.WorkNominal(
                        currentExercise = Exercise.LungesSideToCurtsy,
                        side = AsymmetricalExerciseSideOrder.SECOND.side,
                        exerciseRemainingTime = mockDurationString, // verify formatter is called with 1000L,
                        exerciseRemainingPercentage = .02f,
                        sessionRemainingTime = mockDurationString, // verify formatter is called with (595000L + 1000L)
                        sessionRemainingPercentage = .745f, // (595000L + 1000L) / 800000L =
                        countDown = CountDown(
                            secondsDisplay = "1",
                            progress = .2f, // 1/5 to float
                            playBeep = true
                        )
                    )
                ),
                //normal ongoing rest step before countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 7,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 7,
                            totalSeconds = 35
                        )
                    ),
                    SessionViewState.RestNominal(
                        nextExercise = Exercise.PlankMountainClimber,
                        side = ExerciseSide.NONE,
                        restRemainingTime = mockDurationString, // verify formatter is called with 7000L,
                        restRemainingPercentage = .2f,
                        sessionRemainingTime = mockDurationString, // verify formatter is called with (475000L + 7000L)
                        sessionRemainingPercentage = .6025f, // (475000L + 7000L) / 800000L = 0.75625
                        countDown = null
                    )
                ),
                //normal ongoing rest step during countdown
                Arguments.of(
                    SessionMapperTestParameter(
                        session = testSession,
                        currentSessionStepIndex = 7,
                        currentStepTimerState = StepTimerState(
                            secondsRemaining = 5,
                            totalSeconds = 50
                        )
                    ),
                    SessionViewState.RestNominal(
                        nextExercise = Exercise.PlankMountainClimber,
                        side = ExerciseSide.NONE,
                        restRemainingTime = mockDurationString, // verify formatter is called with 5000L,
                        restRemainingPercentage = .1f,
                        sessionRemainingTime = mockDurationString, // verify formatter is called with (475000L + 5000L)
                        sessionRemainingPercentage = .6f, // (475000L + 5000L) / 800000L =
                        countDown = CountDown(
                            secondsDisplay = "5",
                            progress = 1f, // 5/5 to float
                            playBeep = true
                        )
                    )
                )

            )

    }

    internal data class SessionMapperTestParameter(
        val session: Session,
        val currentSessionStepIndex: Int,
        val currentStepTimerState: StepTimerState
    )

}
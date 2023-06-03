package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.commondomain.models.AsymmetricalExerciseSideOrder
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.Exercise
import fr.shining_cat.simplehiit.commondomain.models.ExerciseSide
import fr.shining_cat.simplehiit.commondomain.models.ExerciseType
import fr.shining_cat.simplehiit.commondomain.models.ExerciseTypeSelected
import fr.shining_cat.simplehiit.commondomain.models.Session
import fr.shining_cat.simplehiit.commondomain.models.SessionSettings
import fr.shining_cat.simplehiit.commondomain.models.SessionStep
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class BuildSessionUseCaseTest : AbstractMockkTest() {

    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val mockComposeExercisesListForSessionUseCase =
        mockk<ComposeExercisesListForSessionUseCase>()
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

    @ParameterizedTest(name = "{index} -> provided with {0} and settings should return {5}")
    @MethodSource("sessionArguments")
    fun `building session`(
        exercisesList: List<Exercise>,
        sessionSettings: SessionSettings,
        expectedSessionOutput: Session
    ) = runTest {
        val testedUseCase =
            BuildSessionUseCase(
                formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
                composeExercisesListForSessionUseCase = mockComposeExercisesListForSessionUseCase,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger
            )
        coEvery {
            mockComposeExercisesListForSessionUseCase.execute(
                any(),
                any(),
                any()
            )
        } returns exercisesList
        val result = testedUseCase.execute(
            sessionSettings,
            durationStringFormatter = durationStringFormatter
        )
        //
        coVerify(exactly = 1) {
            mockComposeExercisesListForSessionUseCase.execute(
                sessionSettings.numberOfWorkPeriods,
                sessionSettings.numberCumulatedCycles,
                any()
            )
        }
        val expectedNumberOfCalls = exercisesList.size.times(2)
        coVerify(exactly = expectedNumberOfCalls) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                any(),
                any()
            )
        }
        val expectedStepsNumber = if (sessionSettings.sessionStartCountDownLengthMs > 0L) {
            exercisesList.size.times(2) + 1
        } else {
            exercisesList.size.times(2)
        }
        assertEquals(expectedStepsNumber, result.steps.size)
        assertEquals(expectedSessionOutput, result)
    }

    private companion object {
        private const val mockDurationString = "This is a test duration string"
        private val userTest1 = User(name = "user 1")
        private val userTest2 = User(name = "user 2")

        @JvmStatic
        fun sessionArguments() =
            Stream.of(
                Arguments.of(
                    listOf<Exercise>(),
                    SessionSettings(
                        numberCumulatedCycles = 0,
                        workPeriodLengthMs = 0L,
                        restPeriodLengthMs = 0L,
                        numberOfWorkPeriods = 0,
                        cycleLengthMs = 0L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 0L,
                        users = listOf(userTest1),
                        exerciseTypes = listOf(),//this input is not used as we mock the secondary usecase which relies on it
                    ),
                    Session(emptyList(), 0L, true, listOf(userTest1))
                ),
                Arguments.of(
                    listOf(Exercise.LyingSupermanTwist),
                    SessionSettings(
                        numberCumulatedCycles = 3,
                        workPeriodLengthMs = 20000L,
                        restPeriodLengthMs = 10000L,
                        numberOfWorkPeriods = 3,
                        cycleLengthMs = 240000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 123L,
                        periodsStartCountDownLengthMs = 234L,
                        users = listOf(
                            userTest1,
                            userTest2
                        ),
                        exerciseTypes = listOf(ExerciseTypeSelected(ExerciseType.LUNGE, true), ExerciseTypeSelected(ExerciseType.CAT, false)),//this input is not used as we mock the secondary usecase which relies on it
                    ),
                    Session(
                        steps = listOf(
                            SessionStep.PrepareStep(
                                durationMs = 123L,
                                remainingSessionDurationMsAfterMe = 30000L,
                                countDownLengthMs = 123L,
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LyingSupermanTwist,
                                side = ExerciseSide.NONE,
                                durationMs = 10000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 20000L,
                                countDownLengthMs = 234L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LyingSupermanTwist,
                                side = ExerciseSide.NONE,
                                durationMs = 20000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 0L,
                                countDownLengthMs = 234L
                            )
                        ),
                        durationMs = 720123L,
                        beepSoundCountDownActive = false,
                        users = listOf(
                            userTest1,
                            userTest2
                        )
                    )
                ),
                Arguments.of(
                    listOf(
                        Exercise.LungesSideToCurtsy,
                        Exercise.LungesSideToCurtsy
                    ),
                    SessionSettings(
                        numberCumulatedCycles = 2,
                        workPeriodLengthMs = 10000L,
                        restPeriodLengthMs = 5000L,
                        numberOfWorkPeriods = 0,//this input is not used as we mock the secondary usecase which relies on it
                        cycleLengthMs = 400000L,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthMs = 345L,
                        periodsStartCountDownLengthMs = 456L,
                        users = listOf(userTest2),
                        exerciseTypes = listOf(ExerciseTypeSelected(ExerciseType.LUNGE, true), ExerciseTypeSelected(ExerciseType.CAT, false)),//this input is not used as we mock the secondary usecase which relies on it
                    ),
                    Session(
                        steps = listOf(
                            SessionStep.PrepareStep(
                                durationMs = 345L,
                                remainingSessionDurationMsAfterMe = 30000L,
                                countDownLengthMs = 345L,
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 5000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 25000L,
                                countDownLengthMs = 456L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 10000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 15000L,
                                countDownLengthMs = 456L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 5000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 10000L,
                                countDownLengthMs = 456L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 10000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 0L,
                                countDownLengthMs = 456L
                            )
                        ),
                        durationMs = 800345L,
                        beepSoundCountDownActive = true,
                        users = listOf(userTest2)
                    )
                ),
                Arguments.of(
                    listOf(
                        Exercise.LungesSideToCurtsy,
                        Exercise.LungesSideToCurtsy,
                        Exercise.LyingSupermanTwist,
                        Exercise.PlankMountainClimber,
                        Exercise.CrabKicks,
                        Exercise.LungesBackKick,
                        Exercise.LungesBackKick,
                        Exercise.LyingSideLegLift,
                        Exercise.LyingSideLegLift
                    ),
                    SessionSettings(
                        numberCumulatedCycles = 5,
                        workPeriodLengthMs = 50000L,
                        restPeriodLengthMs = 35000L,
                        numberOfWorkPeriods = 0,//this input is not used as we mock the secondary usecase which relies on it
                        cycleLengthMs = 680000L,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthMs = 0L,
                        periodsStartCountDownLengthMs = 567L,
                        users = listOf(
                            userTest2,
                            userTest1
                        ),
                        exerciseTypes = listOf(ExerciseTypeSelected(ExerciseType.LUNGE, true), ExerciseTypeSelected(ExerciseType.CAT, false)),//this input is not used as we mock the secondary usecase which relies on it
                    ),
                    Session(
                        steps = listOf(
                            SessionStep.RestStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 730000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 680000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 645000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesSideToCurtsy,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 595000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LyingSupermanTwist,
                                side = ExerciseSide.NONE,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 560000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LyingSupermanTwist,
                                side = ExerciseSide.NONE,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 510000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.PlankMountainClimber,
                                side = ExerciseSide.NONE,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 475000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.PlankMountainClimber,
                                side = ExerciseSide.NONE,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 425000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.CrabKicks,
                                side = ExerciseSide.NONE,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 390000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.CrabKicks,
                                side = ExerciseSide.NONE,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 340000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LungesBackKick,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 305000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesBackKick,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 255000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LungesBackKick,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 220000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LungesBackKick,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 170000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LyingSideLegLift,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 135000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LyingSideLegLift,
                                side = AsymmetricalExerciseSideOrder.FIRST.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 85000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.RestStep(
                                exercise = Exercise.LyingSideLegLift,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 35000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 50000L,
                                countDownLengthMs = 567L
                            ),
                            SessionStep.WorkStep(
                                exercise = Exercise.LyingSideLegLift,
                                side = AsymmetricalExerciseSideOrder.SECOND.side,
                                durationMs = 50000L,
                                durationFormatted = mockDurationString,
                                remainingSessionDurationMsAfterMe = 0L,
                                countDownLengthMs = 567L
                            )
                        ),
                        durationMs = 3400000L,
                        beepSoundCountDownActive = false,
                        users = listOf(
                            userTest2,
                            userTest1
                        )
                    )
                )
            )
    }
}
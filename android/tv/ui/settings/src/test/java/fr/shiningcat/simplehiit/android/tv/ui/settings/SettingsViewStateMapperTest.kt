package fr.shiningcat.simplehiit.android.tv.ui.settings

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.GeneralSettings
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.usecases.DurationFormatStyle
import fr.shiningcat.simplehiit.domain.common.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class SettingsViewStateMapperTest : AbstractMockkTest() {
    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val testedMapper =
        SettingsViewStateMapper(
            formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
            hiitLogger = mockHiitLogger,
        )

    @BeforeEach
    fun setUpMock() {
        coEvery {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = any(),
                formatStyle = any(),
            )
        } returns MOCK_DURATION_STRING
    }

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("generalSettingsArguments")
    fun `mapping generalSettings to correct viewstate`(
        input: Output<GeneralSettings>,
        expectedOutput: SettingsViewState,
    ) {
        val result = testedMapper.map(input)
        //
        assertEquals(expectedOutput, result)
        if (input is Output.Success) {
            coVerify(exactly = 1) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                    durationMs = input.result.cycleLengthMs,
                    formatStyle = DurationFormatStyle.SHORT,
                )
            }
        } else {
            coVerify(exactly = 0) {
                mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(any(), any())
            }
        }
    }

    private companion object {
        private val testUser1 = User(id = 123L, name = "test user 1 name", selected = true)
        private val testUser2 = User(id = 234L, name = "test user 2 name", selected = false)
        private val testUser3 = User(id = 345L, name = "test user 3 name", selected = true)
        private val testUser4 = User(id = 456L, name = "test user 4 name", selected = false)
        private val testExerciseTypeSelected1 =
            ExerciseTypeSelected(type = ExerciseType.CRAB, selected = false)
        private val testExerciseTypeSelected2 =
            ExerciseTypeSelected(type = ExerciseType.PLANK, selected = true)
        private val testExerciseTypeSelected3 =
            ExerciseTypeSelected(type = ExerciseType.SITTING, selected = false)
        private val testExerciseTypeSelected4 =
            ExerciseTypeSelected(type = ExerciseType.SQUAT, selected = true)
        private val testException = Exception("this is a test exception")
        private const val MOCK_DURATION_STRING = "This is a test duration string"

        @JvmStatic
        fun generalSettingsArguments() =
            Stream.of(
                Arguments.of(
                    Output.Success(
                        GeneralSettings(
                            workPeriodLengthMs = 15000L,
                            restPeriodLengthMs = 10000L,
                            numberOfWorkPeriods = 6,
                            cycleLengthMs = 123L,
                            beepSoundCountDownActive = true,
                            sessionStartCountDownLengthMs = 5000L,
                            periodsStartCountDownLengthMs = 20000L,
                            users = listOf(testUser1, testUser3, testUser2, testUser4),
                            exerciseTypes =
                                listOf(
                                    testExerciseTypeSelected1,
                                    testExerciseTypeSelected4,
                                ),
                        ),
                    ),
                    SettingsViewState.Nominal(
                        workPeriodLengthAsSeconds = "15",
                        restPeriodLengthAsSeconds = "10",
                        numberOfWorkPeriods = "6",
                        totalCycleLength = MOCK_DURATION_STRING,
                        beepSoundCountDownActive = true,
                        sessionStartCountDownLengthAsSeconds = "5",
                        periodsStartCountDownLengthAsSeconds = "20",
                        users = listOf(testUser1, testUser3, testUser2, testUser4),
                        exerciseTypes =
                            listOf(
                                testExerciseTypeSelected1,
                                testExerciseTypeSelected4,
                            ),
                    ),
                ),
                Arguments.of(
                    Output.Success(
                        GeneralSettings(
                            workPeriodLengthMs = 21000L,
                            restPeriodLengthMs = 13000L,
                            numberOfWorkPeriods = 7,
                            cycleLengthMs = 234L,
                            beepSoundCountDownActive = false,
                            sessionStartCountDownLengthMs = 7000L,
                            periodsStartCountDownLengthMs = 34000L,
                            users = listOf(testUser1, testUser2),
                            exerciseTypes =
                                listOf(
                                    testExerciseTypeSelected2,
                                    testExerciseTypeSelected3,
                                    testExerciseTypeSelected1,
                                ),
                        ),
                    ),
                    SettingsViewState.Nominal(
                        workPeriodLengthAsSeconds = "21",
                        restPeriodLengthAsSeconds = "13",
                        numberOfWorkPeriods = "7",
                        totalCycleLength = MOCK_DURATION_STRING,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthAsSeconds = "7",
                        periodsStartCountDownLengthAsSeconds = "34",
                        users = listOf(testUser1, testUser2),
                        exerciseTypes =
                            listOf(
                                testExerciseTypeSelected2,
                                testExerciseTypeSelected3,
                                testExerciseTypeSelected1,
                            ),
                    ),
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.NO_USERS_FOUND,
                        exception = testException,
                    ),
                    SettingsViewState.Error(Constants.Errors.NO_USERS_FOUND.code),
                ),
                Arguments.of(
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
                        exception = testException,
                    ),
                    SettingsViewState.Error(Constants.Errors.DATABASE_FETCH_FAILED.code),
                ),
                Arguments.of(
                    Output.Success(
                        GeneralSettings(
                            // testing one case of INT overflow
                            workPeriodLengthMs = Int.MAX_VALUE.toLong() * 1000L + 15L,
                            restPeriodLengthMs = 13000L,
                            numberOfWorkPeriods = 7,
                            cycleLengthMs = 234L,
                            beepSoundCountDownActive = false,
                            sessionStartCountDownLengthMs = 7000L,
                            periodsStartCountDownLengthMs = 34000L,
                            users = listOf(testUser1, testUser2),
                            exerciseTypes =
                                listOf(
                                    testExerciseTypeSelected2,
                                    testExerciseTypeSelected3,
                                    testExerciseTypeSelected1,
                                ),
                        ),
                    ),
                    SettingsViewState.Nominal(
                        workPeriodLengthAsSeconds = (Int.MAX_VALUE).toString(),
                        restPeriodLengthAsSeconds = "13",
                        numberOfWorkPeriods = "7",
                        totalCycleLength = MOCK_DURATION_STRING,
                        beepSoundCountDownActive = false,
                        sessionStartCountDownLengthAsSeconds = "7",
                        periodsStartCountDownLengthAsSeconds = "34",
                        users = listOf(testUser1, testUser2),
                        exerciseTypes =
                            listOf(
                                testExerciseTypeSelected2,
                                testExerciseTypeSelected3,
                                testExerciseTypeSelected1,
                            ),
                    ),
                ),
            )
    }
}

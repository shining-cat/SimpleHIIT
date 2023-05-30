package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.commondomain.models.DisplayStatisticType
import fr.shining_cat.simplehiit.commondomain.models.DisplayedStatistic
import fr.shining_cat.simplehiit.commondomain.models.DurationStringFormatter
import fr.shining_cat.simplehiit.commondomain.models.User
import fr.shining_cat.simplehiit.commondomain.models.UserStatistics
import fr.shining_cat.simplehiit.commondomain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class StatisticsMapperTest : AbstractMockkTest() {

    private val mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase =
        mockk<FormatLongDurationMsAsSmallestHhMmSsStringUseCase>()
    private val testedMapper = StatisticsMapper(
        formatLongDurationMsAsSmallestHhMmSsStringUseCase = mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase,
        hiitLogger = mockHiitLogger
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

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("statisticsArguments")
    fun `mapping statistics to correct viewstate`(
        input: UserStatistics,
        expectedOutput: StatisticsViewState
    ) {
        val result = testedMapper.map(
            input,
            DurationStringFormatter()
        )
        //
        coVerify(exactly = 1) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = input.cumulatedTimeOfExerciseMs,
                durationStringFormatter = DurationStringFormatter()
            )
        }
        coVerify(exactly = 1) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = input.averageSessionLengthMs,
                durationStringFormatter = DurationStringFormatter()
            )
        }
        assertEquals(expectedOutput, result)
    }

    @Test
    fun `mapping statistics to correct viewstate when user has no sessions`() {
        val input = UserStatistics(user = User(name = "test user name 4"))
        val expectedOutput = StatisticsViewState.NoSessions(user = User(name = "test user name 4"))
        val result = testedMapper.map(input, DurationStringFormatter())
        //
        coVerify(exactly = 0) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(any(), any())
        }
        assertEquals(expectedOutput, result)
    }

    private companion object {

        private const val mockDurationString = "This is a test duration string"

        @JvmStatic
        fun statisticsArguments() =
            Stream.of(
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 1"),
                        totalNumberOfSessions = 8,
                        cumulatedTimeOfExerciseMs = 1234L,
                        averageSessionLengthMs = 234,
                        longestStreakDays = 456,
                        currentStreakDays = 678,
                        averageNumberOfSessionsPerWeek = "1.7",
                    ),
                    StatisticsViewState.Nominal(
                        user = User(name = "test user name 1"),
                        listOf(
                            DisplayedStatistic("8", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.TOTAL_EXERCISE_TIME
                            ),
                            DisplayedStatistic("456", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("678", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH
                            ),
                            DisplayedStatistic(
                                "1.7",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK
                            )
                        )
                    )
                ),
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 2"),
                        totalNumberOfSessions = 9,
                        cumulatedTimeOfExerciseMs = 543L,
                        averageSessionLengthMs = 432,
                        longestStreakDays = 654,
                        currentStreakDays = 321,
                        averageNumberOfSessionsPerWeek = "6.4"
                    ),
                    StatisticsViewState.Nominal(
                        user = User(name = "test user name 2"),
                        listOf(
                            DisplayedStatistic("9", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.TOTAL_EXERCISE_TIME
                            ),
                            DisplayedStatistic("654", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("321", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH
                            ),
                            DisplayedStatistic(
                                "6.4",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK
                            )
                        )
                    )
                ),
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 3"),
                        totalNumberOfSessions = 157,
                        cumulatedTimeOfExerciseMs = 4567L,
                        averageSessionLengthMs = 9876,
                        longestStreakDays = 876,
                        currentStreakDays = 958,
                        averageNumberOfSessionsPerWeek = "14.3"
                    ),
                    StatisticsViewState.Nominal(
                        user = User(name = "test user name 3"),
                        listOf(
                            DisplayedStatistic("157", DisplayStatisticType.TOTAL_SESSIONS_NUMBER),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.TOTAL_EXERCISE_TIME
                            ),
                            DisplayedStatistic("876", DisplayStatisticType.LONGEST_STREAK),
                            DisplayedStatistic("958", DisplayStatisticType.CURRENT_STREAK),
                            DisplayedStatistic(
                                mockDurationString,
                                DisplayStatisticType.AVERAGE_SESSION_LENGTH
                            ),
                            DisplayedStatistic(
                                "14.3",
                                DisplayStatisticType.AVERAGE_SESSIONS_PER_WEEK
                            )
                        )
                    )
                )
            )
    }
}



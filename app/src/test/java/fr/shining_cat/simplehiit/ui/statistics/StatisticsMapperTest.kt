package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.models.UserStatistics
import fr.shining_cat.simplehiit.domain.usecases.FormatLongDurationMsAsSmallestHhMmSsStringUseCase
import fr.shining_cat.simplehiit.ui.settings.SettingsMapperTest
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
        coEvery { mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(any(), any(), any(), any(), any(), any(), any()) } returns mockDurationString
    }

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("statisticsArguments")
    fun `mapping statistics to correct viewstate`(
        input: UserStatistics,
        expectedOutput: StatisticsViewState
    ) {
        val result = testedMapper.map(
            input,
            formatStringHoursMinutesSeconds = "formatStringHoursMinutesSeconds",
            formatStringHoursMinutesNoSeconds = "formatStringHoursMinutesNoSeconds",
            formatStringHoursNoMinutesNoSeconds = "formatStringHoursNoMinutesNoSeconds",
            formatStringMinutesSeconds = "formatStringMinutesSeconds",
            formatStringMinutesNoSeconds = "formatStringMinutesNoSeconds",
            formatStringSeconds = "formatStringSeconds")
        //
        coVerify(exactly = 1) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = input.cumulatedTimeOfExerciseMs,
                formatStringHoursMinutesSeconds = "formatStringHoursMinutesSeconds",
                formatStringHoursMinutesNoSeconds = "formatStringHoursMinutesNoSeconds",
                formatStringHoursNoMinutesNoSeconds = "formatStringHoursNoMinutesNoSeconds",
                formatStringMinutesSeconds = "formatStringMinutesSeconds",
                formatStringMinutesNoSeconds = "formatStringMinutesNoSeconds",
                formatStringSeconds = "formatStringSeconds"
            )
        }
        coVerify(exactly = 1) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = input.averageSessionLengthMs,
                formatStringHoursMinutesSeconds = "formatStringHoursMinutesSeconds",
                formatStringHoursMinutesNoSeconds = "formatStringHoursMinutesNoSeconds",
                formatStringHoursNoMinutesNoSeconds = "formatStringHoursNoMinutesNoSeconds",
                formatStringMinutesSeconds = "formatStringMinutesSeconds",
                formatStringMinutesNoSeconds = "formatStringMinutesNoSeconds",
                formatStringSeconds = "formatStringSeconds"
            )
        }
        assertEquals(expectedOutput, result)
    }
    @Test
    fun `mapping statistics to correct viewstate when user has no sessions`() {
        val input = UserStatistics(user = User(name = "test user name 4"))
        val expectedOutput = StatisticsViewState.StatisticsNominal(
            user = User(name = "test user name 4"),
            totalNumberOfSessions = 0,
            cumulatedTimeOfExerciseFormatted = mockDurationString,
            averageSessionLengthFormatted = mockDurationString,
            longestStreakDays = 0,
            currentStreakDays = 0,
            averageNumberOfSessionsPerWeek = "0"
        )
        val result = testedMapper.map(
            input,
            formatStringHoursMinutesSeconds = "formatStringHoursMinutesSeconds",
            formatStringHoursMinutesNoSeconds = "formatStringHoursMinutesNoSeconds",
            formatStringHoursNoMinutesNoSeconds = "formatStringHoursNoMinutesNoSeconds",
            formatStringMinutesSeconds = "formatStringMinutesSeconds",
            formatStringMinutesNoSeconds = "formatStringMinutesNoSeconds",
            formatStringSeconds = "formatStringSeconds")
        //
        coVerify(exactly = 2) {
            mockFormatLongDurationMsAsSmallestHhMmSsStringUseCase.execute(
                durationMs = 0,
                formatStringHoursMinutesSeconds = "formatStringHoursMinutesSeconds",
                formatStringHoursMinutesNoSeconds = "formatStringHoursMinutesNoSeconds",
                formatStringHoursNoMinutesNoSeconds = "formatStringHoursNoMinutesNoSeconds",
                formatStringMinutesSeconds = "formatStringMinutesSeconds",
                formatStringMinutesNoSeconds = "formatStringMinutesNoSeconds",
                formatStringSeconds = "formatStringSeconds"
            )
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
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 1"),
                        totalNumberOfSessions = 8,
                        cumulatedTimeOfExerciseFormatted = mockDurationString,
                        averageSessionLengthFormatted = mockDurationString,
                        longestStreakDays = 456,
                        currentStreakDays = 678,
                        averageNumberOfSessionsPerWeek = "1.7",
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
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 2"),
                        totalNumberOfSessions = 9,
                        cumulatedTimeOfExerciseFormatted = mockDurationString,
                        averageSessionLengthFormatted = mockDurationString,
                        longestStreakDays = 654,
                        currentStreakDays = 321,
                        averageNumberOfSessionsPerWeek = "6.4"
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
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 3"),
                        totalNumberOfSessions = 157,
                        cumulatedTimeOfExerciseFormatted = mockDurationString,
                        averageSessionLengthFormatted = mockDurationString,
                        longestStreakDays = 876,
                        currentStreakDays = 958,
                        averageNumberOfSessionsPerWeek = "14.3"
                    )
                )
            )
    }
}



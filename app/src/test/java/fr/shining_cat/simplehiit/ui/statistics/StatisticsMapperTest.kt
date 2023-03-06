package fr.shining_cat.simplehiit.ui.statistics

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.models.UserStatistics
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class StatisticsMapperTest : AbstractMockkTest() {

    private val testedMapper = StatisticsMapper(mockHiitLogger)

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("statisticsArguments")
    fun `mapping statistics to correct viewstate`(
        input: UserStatistics,
        expectedOutput: StatisticsViewState
    ) {
        val result = testedMapper.map(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun statisticsArguments() =
            Stream.of(
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 1"),
                        totalNumberOfSessions = 8,
                        cumulatedTimeOfExerciseSeconds = 1234L,
                        averageSessionLengthSeconds = 234,
                        longestStreakDays = 456,
                        currentStreakDays = 678,
                        averageNumberOfSessionsPerWeek = 1.7,
                    ),
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 1"),
                        totalNumberOfSessions = 8,
                        cumulatedTimeOfExerciseSeconds = 1234L,
                        averageSessionLengthSeconds = 234,
                        longestStreakDays = 456,
                        currentStreakDays = 678,
                        averageNumberOfSessionsPerWeek = 1.7,
                    )
                ),
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 2"),
                        totalNumberOfSessions = 9,
                        cumulatedTimeOfExerciseSeconds = 543L,
                        averageSessionLengthSeconds = 432,
                        longestStreakDays = 654,
                        currentStreakDays = 321,
                        averageNumberOfSessionsPerWeek = 6.4
                    ),
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 2"),
                        totalNumberOfSessions = 9,
                        cumulatedTimeOfExerciseSeconds = 543L,
                        averageSessionLengthSeconds = 432,
                        longestStreakDays = 654,
                        currentStreakDays = 321,
                        averageNumberOfSessionsPerWeek = 6.4
                    )
                ),
                Arguments.of(
                    UserStatistics(
                        user = User(name = "test user name 3"),
                        totalNumberOfSessions = 157,
                        cumulatedTimeOfExerciseSeconds = 4567L,
                        averageSessionLengthSeconds = 9876,
                        longestStreakDays = 876,
                        currentStreakDays = 958,
                        averageNumberOfSessionsPerWeek = 14.3
                    ),
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 3"),
                        totalNumberOfSessions = 157,
                        cumulatedTimeOfExerciseSeconds = 4567L,
                        averageSessionLengthSeconds = 9876,
                        longestStreakDays = 876,
                        currentStreakDays = 958,
                        averageNumberOfSessionsPerWeek = 14.3
                    )
                ),
                Arguments.of(
                    UserStatistics(user = User(name = "test user name 4")),
                    StatisticsViewState.StatisticsNominal(
                        user = User(name = "test user name 4"),
                        totalNumberOfSessions = 0,
                        cumulatedTimeOfExerciseSeconds = 0L,
                        averageSessionLengthSeconds = 0,
                        longestStreakDays = 0,
                        currentStreakDays = 0,
                        averageNumberOfSessionsPerWeek = 0.0
                    )
                )
            )
    }
}



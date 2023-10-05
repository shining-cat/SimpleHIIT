package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateAverageSessionsPerWeekUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("averageArguments")
    fun `finding average number of sessions per 7-days period`(
        nowTimestamp: Long,
        oldestTimestamp: Long,
        numberOfSessions: Int,
        expectedAverageOutput: String
    ) = runTest {
        val testedUseCase =
            CalculateAverageSessionsPerWeekUseCase(
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
        val input =
            mutableListOf(oldestTimestamp) // ensure the first timestamp is actually our oldest one and not a random one
        input.addAll(List(numberOfSessions - 1) { Random.nextLong(oldestTimestamp, nowTimestamp) })
        val result = testedUseCase.execute(input, nowTimestamp)
        //
        assertEquals(expectedAverageOutput, result)
    }

    @Test
    fun `check if empty list returns 0`() = runTest {
        val testedUseCase =
            CalculateAverageSessionsPerWeekUseCase(
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
        val result = testedUseCase.execute(emptyList(), 1676640515000)
        //
        assertEquals("0", result)
    }

    private companion object {

        @JvmStatic
        fun averageArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    1676640515000, // "now" Friday, 17 February 2023 14:28:35 GMT+01:00
                    1664809200000, // "oldest session" Monday, 3 October 2022 17:00:00 GMT+02:00 DST ... diff 19.56 weeks
                    38, // total number of sessions
                    "1.94"
                ),
                Arguments.of(
                    1665241200000, // "now" Saturday, 8 October 2022 17:00:00 GMT+02:00 DST
                    1664636400000, // "oldest session" Saturday, 1 October 2022 17:00:00 GMT+02:00 DST ... exactly 1 week
                    6, // total number of sessions
                    "6"
                ),
                Arguments.of(
                    1634482800000, // "now" Sunday, 17 October 2021 17:00:00 GMT+02:00 DST
                    1633273200000, // "oldest session" Sunday, 3 October 2021 17:00:00 GMT+02:00 DST ... exactly 2 weeks
                    2, // total number of sessions
                    "1"
                ),
                Arguments.of(
                    1678028400000, // "now" Sunday, 5 March 2023 16:00:00 GMT+01:00
                    1588518000000, // "oldest session" Sunday, 3 May 2020 17:00:00 GMT+02:00 DST ... diff 148 weeks
                    354, // total number of sessions
                    "2.39"
                ),
                Arguments.of(
                    1680259247000, // "now" Friday, 31 March 2023 12:40:47 GMT+02:00 DST
                    1679744447000, // "oldest session" Saturday, 25 March 2023 12:40:47 GMT+01:00 ... diff less than a week
                    3, // total number of sessions
                    "3" // returning total number of sessions
                )
            )
    }
}

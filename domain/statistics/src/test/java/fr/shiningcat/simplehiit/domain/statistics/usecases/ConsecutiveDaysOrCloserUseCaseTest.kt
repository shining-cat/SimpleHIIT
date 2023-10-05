package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class ConsecutiveDaysOrCloserUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> given {0} should return {1}")
    @MethodSource("datesArguments")
    fun `checking if a pair of timestamps belong to two consecutive days, less, or more`(
        input: Pair<Long, Long>,
        expectedOutput: Consecutiveness
    ) = runTest {
        val testedUseCase =
            ConsecutiveDaysOrCloserUseCase(
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger
            )
        val result = testedUseCase.execute(input.first, input.second)
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun datesArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    Pair(
                        1645086990000, // Thursday, 17 February 2022 09:36:30 GMT+01:00
                        1645004700000 // Wednesday, 16 February 2022 10:45:00 GMT+01:00
                    ),
                    Consecutiveness.CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1654095600000, // Wednesday, 1 June 2022 17:00:00 GMT+02:00 DST
                        1654423200000 // Sunday, 5 June 2022 12:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.NON_CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1691920800000, // Sunday, 13 August 2023 12:00:00 GMT+02:00 DST
                        1691953200000 // Sunday, 13 August 2023 21:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.SAME_DAY
                ),
                Arguments.of(
                    Pair(
                        1702501200000, // Wednesday, 13 December 2023 22:00:00 GMT+01:00
                        1702515600000 // Thursday, 14 December 2023 02:00:00 GMT+01:00
                    ),
                    Consecutiveness.CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1704056400000, // Sunday, 31 December 2023 22:00:00 GMT+01:00
                        1704074400000 // Monday, 1 January 2024 00:00:00 GMT+01:00
                    ),
                    Consecutiveness.CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1522519200000, // Saturday, 31 March 2018 20:00:00 GMT+02:00 DST
                        1522548000000 // Sunday, 1 April 2018 04:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1537714800000, // Sunday, 23 September 2018 17:00:00 GMT+02:00 DST
                        1553335200000 // Saturday, 23 March 2019 11:00:00 GMT+01:00
                    ),
                    Consecutiveness.NON_CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1597780800000, // Tuesday, 18 August 2020 22:00:00 GMT+02:00 DST
                        1597658400000 // Monday, 17 August 2020 12:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1638550800000, // Friday, 3 December 2021 18:00:00 GMT+01:00
                        1637492400000 // Sunday, 21 November 2021 12:00:00 GMT+01:00
                    ),
                    Consecutiveness.NON_CONSECUTIVE_DAYS
                ),
                Arguments.of(
                    Pair(
                        1665162000000, // Friday, 7 October 2022 19:00:00 GMT+02:00 DST
                        1665133200000 // Friday, 7 October 2022 11:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.SAME_DAY
                ),
                Arguments.of(
                    Pair(
                        1665162000000, // Friday, 7 October 2022 19:00:00 GMT+02:00 DST
                        1665162000000 // Friday, 7 October 2022 19:00:00 GMT+02:00 DST
                    ),
                    Consecutiveness.SAME_DAY
                )
            )
    }
}

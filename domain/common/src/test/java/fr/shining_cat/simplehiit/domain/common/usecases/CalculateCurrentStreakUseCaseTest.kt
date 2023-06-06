package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class CalculateCurrentStreakUseCaseTest : AbstractMockkTest() {

    private val mockConsecutiveDaysOrCloserUseCaseTest = mockk<ConsecutiveDaysOrCloserUseCase>()

    @ParameterizedTest(name = "{index} -> should call ConsecutiveDaysOrCloserUseCase {1} time(s) and return {2}")
    @MethodSource("streakArguments")
    fun `finding current streak length in days`(
        consecutivenessReturns: List<Consecutiveness>,
        expectedCallsOnConsecutiveDaysOrCloserUseCase: Int,
        expectedStreakLengthOutput: Int
    ) = runTest {
        val testedUseCase = CalculateCurrentStreakUseCase(
            consecutiveDaysOrCloserUseCase = mockConsecutiveDaysOrCloserUseCaseTest,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        coEvery {
            mockConsecutiveDaysOrCloserUseCaseTest.execute(
                any(),
                any()
            )
        } returnsMany consecutivenessReturns
        //we don't care here about the actual content of the timestamp list as ConsecutiveDaysOrCloserUseCase will
        // be the one evaluating each pair's consecutiveness, and it's return is mocked here, as it is tested on its own already
        val input = List(consecutivenessReturns.size) {
            Random.nextLong(
                1264063781000L,
                1689580181000L
            )
        } // random timestamps between Thursday, 21 January 2010 09:49:41 GMT+01:00 and Monday, 17 July 2023 09:49:41 GMT+02:00 DST
        val now = Random.nextLong(1264063781000L, 1689580181000L)
        val result = testedUseCase.execute(input, now)
        coVerify(exactly = expectedCallsOnConsecutiveDaysOrCloserUseCase) {
            mockConsecutiveDaysOrCloserUseCaseTest.execute(
                any(),
                any()
            )
        }
        assertEquals(expectedStreakLengthOutput, result)
    }

    private companion object {

        @JvmStatic
        fun streakArguments() =
            Stream.of(
                Arguments.of(
                    emptyList<Consecutiveness>(),
                    0,
                    0
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS, // current streak is 4
                        Consecutiveness.NON_CONSECUTIVE_DAYS,//breaking streak
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    5,
                    4
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.SAME_DAY, //this will not be counted in the streak
                        Consecutiveness.CONSECUTIVE_DAYS, // current streak is 3
                        Consecutiveness.NON_CONSECUTIVE_DAYS,//breaking streak
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    5,
                    3
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.NON_CONSECUTIVE_DAYS, //current streak is 0 as last session and "now" are NON_CONSECUTIVE_DAYS
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    1,
                    0
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.NON_CONSECUTIVE_DAYS, //current streak is 0 as last session and "now" are NON_CONSECUTIVE_DAYS
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    1,
                    0
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.SAME_DAY, //current streak is 5 as last session and "now" are SAME_DAY, we must include today in the count
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    5,
                    5
                )
            )

    }


}
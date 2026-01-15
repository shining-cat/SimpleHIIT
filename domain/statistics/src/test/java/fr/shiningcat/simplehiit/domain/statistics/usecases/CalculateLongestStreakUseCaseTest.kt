/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
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
internal class CalculateLongestStreakUseCaseTest : AbstractMockkTest() {
    private val mockConsecutiveDaysOrCloserUseCaseTest = mockk<ConsecutiveDaysOrCloserUseCase>()

    @ParameterizedTest(name = "{index} -> should return {1}")
    @MethodSource("streakArguments")
    fun `finding longest streak length in days`(
        consecutivenessReturns: List<Consecutiveness>,
        expectedStreakLengthOutput: Int,
    ) = runTest {
        val testedUseCase =
            CalculateLongestStreakUseCase(
                consecutiveDaysOrCloserUseCase = mockConsecutiveDaysOrCloserUseCaseTest,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                logger = mockHiitLogger,
            )
        coEvery {
            mockConsecutiveDaysOrCloserUseCaseTest.execute(
                any(),
                any(),
            )
        } returnsMany consecutivenessReturns
        // we don't care here about the actual content of the timestamp list as ConsecutiveDaysOrCloserUseCase will
        // be the one evaluating each pair's consecutiveness, and it's return is mocked here, as it is tested on its own already
        val input =
            List(consecutivenessReturns.size) {
                // random timestamps between Thursday, 21 January 2010 09:49:41 GMT+01:00 and Monday, 17 July 2023 09:49:41 GMT+02:00 DST
                Random.nextLong(
                    1264063781000L,
                    1689580181000L,
                )
            }
        val now = Random.nextLong(1264063781000L, 1689580181000L)
        val result = testedUseCase.execute(input, now)
        assertEquals(expectedStreakLengthOutput, result)
    }

    private companion object {
        @JvmStatic
        fun streakArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    emptyList<Consecutiveness>(),
                    0,
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // current streak is 4
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    4,
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // this will not be counted in the streak
                        Consecutiveness.SAME_DAY,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // current streak is 3
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    3,
                ),
                Arguments.of(
                    listOf(
                        // current streak is 0 as last session and "now" are NON_CONSECUTIVE_DAYS
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // longest streak is 3
                    ),
                    3,
                ),
                Arguments.of(
                    listOf(
                        // current streak is 0 as last session and "now" are NON_CONSECUTIVE_DAYS
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // longest streak is 2
                    ),
                    2,
                ),
                Arguments.of(
                    listOf(
                        // current streak is 5 as last session and "now" are SAME_DAY, we must include today in the count
                        Consecutiveness.SAME_DAY,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    5,
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // this will not be counted in the streak:
                        Consecutiveness.SAME_DAY,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // current streak is 3
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // longest streak is 5
                    ),
                    5,
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // this will not be counted in the streak:
                        Consecutiveness.SAME_DAY,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // current streak is 3
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // longest streak is 6
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                    ),
                    6,
                ),
                Arguments.of(
                    listOf(
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // this will not be counted in the streak:
                        Consecutiveness.SAME_DAY,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // current streak is 3
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // breaking streak
                        Consecutiveness.NON_CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        Consecutiveness.CONSECUTIVE_DAYS,
                        // longest streak is 7
                    ),
                    7,
                ),
            )
    }
}

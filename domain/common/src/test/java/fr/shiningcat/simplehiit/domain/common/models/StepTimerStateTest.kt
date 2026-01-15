/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.common.models

import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class StepTimerStateTest : AbstractMockkTest() {
    @ParameterizedTest(name = "{index} -> remaining={0}ms, total={1}ms should give {2}%")
    @MethodSource("remainingPercentageArguments")
    fun `remainingPercentage calculates correct percentage`(
        milliSecondsRemaining: Long,
        totalMilliSeconds: Long,
        expectedPercentage: Float,
    ) {
        val stepTimerState =
            StepTimerState(
                milliSecondsRemaining = milliSecondsRemaining,
                totalMilliSeconds = totalMilliSeconds,
            )

        assertEquals(expectedPercentage, stepTimerState.remainingPercentage, 0.001f)
    }

    @ParameterizedTest(name = "{index} -> remaining={0}ms, total={1}ms should give 0% (invalid)")
    @MethodSource("invalidStateArguments")
    fun `remainingPercentage returns zero for invalid states`(
        milliSecondsRemaining: Long,
        totalMilliSeconds: Long,
    ) {
        val stepTimerState =
            StepTimerState(
                milliSecondsRemaining = milliSecondsRemaining,
                totalMilliSeconds = totalMilliSeconds,
            )

        assertEquals(0f, stepTimerState.remainingPercentage, 0.001f)
    }

    @ParameterizedTest(name = "{index} -> remaining={0}ms exceeds total={1}ms, should give 100%")
    @MethodSource("exceedsMaxArguments")
    fun `remainingPercentage coerces to max 100 percent when remaining exceeds total`(
        milliSecondsRemaining: Long,
        totalMilliSeconds: Long,
    ) {
        val stepTimerState =
            StepTimerState(
                milliSecondsRemaining = milliSecondsRemaining,
                totalMilliSeconds = totalMilliSeconds,
            )

        assertEquals(1.0f, stepTimerState.remainingPercentage, 0.001f)
    }

    // //////////////////////
    private companion object {
        @JvmStatic
        fun remainingPercentageArguments(): Stream<Arguments> =
            Stream.of(
                // Complete timer (100%)
                Arguments.of(10000L, 10000L, 1.0f),
                // Half remaining (50%)
                Arguments.of(5000L, 10000L, 0.5f),
                // Quarter remaining (25%)
                Arguments.of(2500L, 10000L, 0.25f),
                // Three quarters remaining (75%)
                Arguments.of(7500L, 10000L, 0.75f),
                // Timer finished (0%)
                Arguments.of(0L, 10000L, 0.0f),
                // 10% remaining
                Arguments.of(1000L, 10000L, 0.1f),
                // 90% remaining
                Arguments.of(9000L, 10000L, 0.9f),
                // Very small percentage
                Arguments.of(1L, 10000L, 0.0001f),
                // Large numbers
                Arguments.of(500000L, 1000000L, 0.5f),
                // Different time scales
                Arguments.of(30000L, 60000L, 0.5f), // 30s of 60s
                Arguments.of(45000L, 180000L, 0.25f), // 45s of 3min
            )

        @JvmStatic
        fun invalidStateArguments(): Stream<Arguments> =
            Stream.of(
                // Zero total (division by zero)
                Arguments.of(10000L, 0L),
                Arguments.of(0L, 0L),
                Arguments.of(1L, 0L),
                // Negative total
                Arguments.of(5000L, -10000L),
                Arguments.of(0L, -1L),
                // Negative remaining
                Arguments.of(-1L, 10000L),
                Arguments.of(-5000L, 10000L),
                // Both negative (default/uninitialized state)
                Arguments.of(-1L, -1L),
                Arguments.of(-5000L, -10000L),
            )

        @JvmStatic
        fun exceedsMaxArguments(): Stream<Arguments> =
            Stream.of(
                // Remaining exceeds total - should be coerced to 1.0
                Arguments.of(15000L, 10000L),
                Arguments.of(100000L, 10000L),
                Arguments.of(60001L, 60000L),
            )
    }
}

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ValidateInputNumberCyclesUseCaseTest : AbstractMockkTest() {
    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        expectedOutput: InputError?,
    ) = runTest {
        val testedUseCase =
            ValidateInputNumberCyclesUseCase(logger = mockHiitLogger)
        val result = testedUseCase.execute(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                // Valid inputs: length < 3 and is non-negative integer
                Arguments.of("0", null),
                Arguments.of("1", null),
                Arguments.of("3", null),
                Arguments.of("9", null),
                Arguments.of("10", null),
                Arguments.of("25", null),
                Arguments.of("99", null),
                Arguments.of("+5", null),
                // Invalid: length >= 3 (even if valid integer)
                Arguments.of("100", InputError.WRONG_FORMAT),
                Arguments.of("369", InputError.WRONG_FORMAT),
                Arguments.of("36945", InputError.WRONG_FORMAT),
                // Invalid: not an integer (even if length < 3)
                Arguments.of("", InputError.WRONG_FORMAT),
                Arguments.of(" ", InputError.WRONG_FORMAT),
                Arguments.of("a", InputError.WRONG_FORMAT),
                Arguments.of("1a", InputError.WRONG_FORMAT),
                Arguments.of("a1", InputError.WRONG_FORMAT),
                Arguments.of("12a", InputError.WRONG_FORMAT),
                Arguments.of("three", InputError.WRONG_FORMAT),
                Arguments.of("!!", InputError.WRONG_FORMAT),
                Arguments.of("1.5", InputError.WRONG_FORMAT),
                // Invalid: negative values
                Arguments.of("-1", InputError.WRONG_FORMAT),
                Arguments.of("-5", InputError.WRONG_FORMAT),
            )
    }
}

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ValidateNumberOfWorkPeriodsUseCaseTest : AbstractMockkTest() {
    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        expectedOutput: InputError?,
    ) = runTest {
        val testedUseCase =
            ValidateNumberOfWorkPeriodsUseCase(logger = mockHiitLogger)
        val result = testedUseCase.execute(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of("3", null),
                Arguments.of("369", null),
                Arguments.of("369346", null),
                Arguments.of("three", InputError.WRONG_FORMAT),
            )
    }
}

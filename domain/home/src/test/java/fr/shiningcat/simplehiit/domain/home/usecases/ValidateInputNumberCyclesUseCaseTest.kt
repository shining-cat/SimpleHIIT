package fr.shiningcat.simplehiit.domain.home.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
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
        expectedOutput: Constants.InputError,
    ) = runTest {
        val testedUseCase =
            ValidateInputNumberCyclesUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                // Valid inputs: length < 3 and is non-negative integer
                Arguments.of("0", Constants.InputError.NONE),
                Arguments.of("1", Constants.InputError.NONE),
                Arguments.of("3", Constants.InputError.NONE),
                Arguments.of("9", Constants.InputError.NONE),
                Arguments.of("10", Constants.InputError.NONE),
                Arguments.of("25", Constants.InputError.NONE),
                Arguments.of("99", Constants.InputError.NONE),
                Arguments.of("+5", Constants.InputError.NONE),
                // Invalid: length >= 3 (even if valid integer)
                Arguments.of("100", Constants.InputError.WRONG_FORMAT),
                Arguments.of("369", Constants.InputError.WRONG_FORMAT),
                Arguments.of("36945", Constants.InputError.WRONG_FORMAT),
                // Invalid: not an integer (even if length < 3)
                Arguments.of("", Constants.InputError.WRONG_FORMAT),
                Arguments.of(" ", Constants.InputError.WRONG_FORMAT),
                Arguments.of("a", Constants.InputError.WRONG_FORMAT),
                Arguments.of("1a", Constants.InputError.WRONG_FORMAT),
                Arguments.of("a1", Constants.InputError.WRONG_FORMAT),
                Arguments.of("12a", Constants.InputError.WRONG_FORMAT),
                Arguments.of("three", Constants.InputError.WRONG_FORMAT),
                Arguments.of("!!", Constants.InputError.WRONG_FORMAT),
                Arguments.of("1.5", Constants.InputError.WRONG_FORMAT),
                // Invalid: negative values
                Arguments.of("-1", Constants.InputError.WRONG_FORMAT),
                Arguments.of("-5", Constants.InputError.WRONG_FORMAT),
            )
    }
}

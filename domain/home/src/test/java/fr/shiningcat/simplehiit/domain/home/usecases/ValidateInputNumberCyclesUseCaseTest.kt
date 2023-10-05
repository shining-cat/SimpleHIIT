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
        expectedOutput: Constants.InputError
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
                Arguments.of("3", Constants.InputError.NONE),
                Arguments.of("369", Constants.InputError.WRONG_FORMAT),
                Arguments.of("36945", Constants.InputError.WRONG_FORMAT),
                Arguments.of("three", Constants.InputError.WRONG_FORMAT),
            )
    }
}
package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class ValidateInputSessionStartCountdownUseCaseTest : AbstractMockkTest() {
    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        expectedOutput: Constants.InputError,
    ) = runTest {
        val testedUseCase =
            ValidateInputSessionStartCountdownUseCase(
                hiitLogger = mockHiitLogger,
            )
        val result = testedUseCase.execute(input)
        //
        Assertions.assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments() =
            Stream.of(
                Arguments.of("3", Constants.InputError.NONE),
                Arguments.of("369", Constants.InputError.NONE),
                Arguments.of("369346", Constants.InputError.NONE),
                Arguments.of("three", Constants.InputError.WRONG_FORMAT),
            )
    }
}

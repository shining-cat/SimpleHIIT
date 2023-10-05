package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ValidatePeriodLengthUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        periodCountDownLengthSeconds: Long,
        expectedOutput: Constants.InputError
    ) = runTest {
        val testedUseCase =
            ValidatePeriodLengthUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(input, periodCountDownLengthSeconds)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of("three", 123L, Constants.InputError.WRONG_FORMAT),
                Arguments.of("10", 5L, Constants.InputError.NONE),
                Arguments.of("5", 5L, Constants.InputError.NONE),
                Arguments.of("4", 5L, Constants.InputError.VALUE_TOO_SMALL)
            )
    }
}

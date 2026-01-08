package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.models.InputError
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
        expectedOutput: InputError?,
    ) = runTest {
        val testedUseCase =
            ValidatePeriodLengthUseCase(logger = mockHiitLogger)
        val result = testedUseCase.execute(input, periodCountDownLengthSeconds)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of("three", 123L, InputError.WRONG_FORMAT),
                Arguments.of("10", 5L, null),
                Arguments.of("5", 5L, null),
                Arguments.of("4", 5L, InputError.VALUE_TOO_SMALL),
            )
    }
}

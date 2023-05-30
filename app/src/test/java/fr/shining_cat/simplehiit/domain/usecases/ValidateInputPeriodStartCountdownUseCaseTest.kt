package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ValidateInputPeriodStartCountdownUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        workPeriodLengthSeconds: Long,
        restPeriodLengthSeconds: Long,
        expectedOutput: Constants.InputError
    ) = runTest {
        val testedUseCase = ValidateInputPeriodStartCountdownUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(
            input = input,
            workPeriodLengthSeconds = workPeriodLengthSeconds,
            restPeriodLengthSeconds = restPeriodLengthSeconds
        )
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun numberCyclesTestArguments() =
            Stream.of(
                Arguments.of("three", 20L, 10L, Constants.InputError.WRONG_FORMAT),
                Arguments.of("3", 20L, 10L, Constants.InputError.NONE),
                Arguments.of("15", 20L, 10L, Constants.InputError.VALUE_TOO_BIG),
                Arguments.of("25", 20L, 10L, Constants.InputError.VALUE_TOO_BIG),
            )
    }
}
package fr.shining_cat.simplehiit.commondomain.usecases

import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class ValidateInputNumberCyclesUseCaseTest : AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        input: String,
        expectedOutput: fr.shining_cat.simplehiit.commondomain.Constants.InputError
    ) = runTest {
        val testedUseCase = ValidateInputNumberCyclesUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(input)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun numberCyclesTestArguments() =
            Stream.of(
                Arguments.of("3", fr.shining_cat.simplehiit.commondomain.Constants.InputError.NONE),
                Arguments.of("369", fr.shining_cat.simplehiit.commondomain.Constants.InputError.WRONG_FORMAT),
                Arguments.of("36945", fr.shining_cat.simplehiit.commondomain.Constants.InputError.WRONG_FORMAT),
                Arguments.of("three", fr.shining_cat.simplehiit.commondomain.Constants.InputError.WRONG_FORMAT),
            )
    }
}
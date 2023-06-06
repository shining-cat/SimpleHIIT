package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class ValidateInputUserNameUseCaseTest: AbstractMockkTest() {

    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        user: User,
        existingUsers: List<User>,
        expectedOutput: fr.shining_cat.simplehiit.domain.common.Constants.InputError
    ) = runTest {
        val testedUseCase = ValidateInputUserNameUseCase(hiitLogger = mockHiitLogger)
        val result = testedUseCase.execute(user, existingUsers)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {

        @JvmStatic
        fun numberCyclesTestArguments() =
            Stream.of(
                Arguments.of(
                    User(id=123L, name = "user test 2"),
                    listOf(
                        User(id=123L, name = "user test 1"),
                        User(id=234L, name = "user test 2"),
                        User(id=345L, name = "user test 3"),
                    ),
                    fr.shining_cat.simplehiit.domain.common.Constants.InputError.VALUE_ALREADY_TAKEN
                ),
                Arguments.of(
                    User(id=123L, name = "very very long user test name that is more than twenty-five characters since that is the limit we have chosen"),
                    listOf(
                        User(id=123L, name = "user test 1"),
                        User(id=234L, name = "user test 2"),
                        User(id=345L, name = "user test 3"),
                    ),
                    fr.shining_cat.simplehiit.domain.common.Constants.InputError.TOO_LONG
                ),
                Arguments.of(
                    User(id=123L, name = "user test 1"),
                    listOf(
                        User(id=123L, name = "user test 1"),
                        User(id=234L, name = "user test 2"),
                        User(id=345L, name = "user test 3"),
                    ),
                    fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE), //picking the same name for the same user id
                Arguments.of(
                    User(id=123L, name = "completely new user name"),//25 chars is the accepted limit
                    listOf(
                        User(id=123L, name = "user test 1"),
                        User(id=234L, name = "user test 2"),
                        User(id=345L, name = "user test 3"),
                    ),
                    fr.shining_cat.simplehiit.domain.common.Constants.InputError.NONE)
            )
    }
}
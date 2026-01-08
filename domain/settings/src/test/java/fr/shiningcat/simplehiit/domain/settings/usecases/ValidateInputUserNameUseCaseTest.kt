package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.models.InputError
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

internal class ValidateInputUserNameUseCaseTest : AbstractMockkTest() {
    @ParameterizedTest(name = "{index} -> should return {0}")
    @MethodSource("numberCyclesTestArguments")
    fun `finding average number of sessions per 7-days period`(
        user: User,
        existingUsers: List<User>,
        expectedOutput: InputError?,
    ) = runTest {
        val testedUseCase =
            ValidateInputUserNameUseCase(logger = mockHiitLogger)
        val result = testedUseCase.execute(user, existingUsers)
        //
        assertEquals(expectedOutput, result)
    }

    private companion object {
        @JvmStatic
        fun numberCyclesTestArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    User(id = 123L, name = "user test 2"),
                    listOf(
                        User(id = 123L, name = "user test 1"),
                        User(id = 234L, name = "user test 2"),
                        User(id = 345L, name = "user test 3"),
                    ),
                    InputError.VALUE_ALREADY_TAKEN,
                ),
                Arguments.of(
                    User(
                        id = 123L,
                        name =
                            "very very long user test name that is more than " +
                                "twenty-five characters since that is the limit we have chosen",
                    ),
                    listOf(
                        User(id = 123L, name = "user test 1"),
                        User(id = 234L, name = "user test 2"),
                        User(id = 345L, name = "user test 3"),
                    ),
                    InputError.TOO_LONG,
                ),
                Arguments.of(
                    User(
                        id = 123L,
                        name =
                            "",
                    ),
                    emptyList<User>(),
                    InputError.VALUE_EMPTY,
                ),
                Arguments.of(
                    User(
                        id = 123L,
                        name =
                            "    ",
                    ),
                    emptyList<User>(),
                    InputError.VALUE_EMPTY,
                ),
                // picking the same name for the same user id:
                Arguments.of(
                    User(id = 123L, name = "user test 1"),
                    listOf(
                        User(id = 123L, name = "user test 1"),
                        User(id = 234L, name = "user test 2"),
                        User(id = 345L, name = "user test 3"),
                    ),
                    null,
                ),
                Arguments.of(
                    // 25 chars is the accepted limit
                    User(
                        id = 123L,
                        name = "completely new user name",
                    ),
                    listOf(
                        User(id = 123L, name = "user test 1"),
                        User(id = 234L, name = "user test 2"),
                        User(id = 345L, name = "user test 3"),
                    ),
                    null,
                ),
            )
    }
}

package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.UsersRepository
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckIfAnotherUserUsesThatNameUseCaseTest : AbstractMockkTest() {
    private val mockUsersRepository = mockk<UsersRepository>()

    @Test
    fun `calls repo and return error if repo returns error`() =
        runTest {
            val testedUseCase =
                CheckIfAnotherUserUsesThatNameUseCase(
                    usersRepository = mockUsersRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testUser = User(name = "this is a test name")
            val testException1 = Exception("this is a test exception 1")
            val usersError = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException1)
            coEvery { mockUsersRepository.getUsersList() } answers { usersError }
            //
            val output = testedUseCase.execute(testUser)
            //
            coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
            assertEquals(usersError, output)
        }

    @ParameterizedTest(name = "{index} -> when input is {1} for list being {0}, result should be {2}")
    @MethodSource("getUsersListArguments")
    fun `calls repo and return Success with correct value repo returns success`(
        usersList: List<User>,
        input: User,
        expectedResult: Boolean,
    ) = runTest {
        val testedUseCase =
            CheckIfAnotherUserUsesThatNameUseCase(
                usersRepository = mockUsersRepository,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger,
            )
        coEvery { mockUsersRepository.getUsersList() } answers { Output.Success(usersList) }
        //
        val output = testedUseCase.execute(input)
        //
        coVerify(exactly = 1) { mockUsersRepository.getUsersList() }
        assertTrue(output is Output.Success)
        output as Output.Success
        assertEquals(expectedResult, output.result)
    }

    // /////////////////////
    private companion object {
        @JvmStatic
        fun getUsersListArguments(): Stream<Arguments> =
            Stream.of(
                Arguments.of(
                    listOf<User>(),
                    User(name = "whatever"),
                    false,
                ),
                Arguments.of(
                    listOf<User>(),
                    User(id = 123L, name = "whatever"),
                    false,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(name = "different name"),
                    false,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(id = 123L, name = "different name"),
                    false,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(id = 234L, name = "different name"),
                    false,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(name = "only user name"),
                    true,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(id = 123L, name = "only user name"),
                    // it's the same user
                    false,
                ),
                Arguments.of(
                    listOf(User(id = 123L, name = "only user name")),
                    User(id = 234L, name = "only user name"),
                    true,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(name = "a different name"),
                    false,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(id = 456L, name = "a different name"),
                    false,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(id = 654L, name = "a different name"),
                    false,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(name = "user name 2"),
                    true,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(id = 654L, name = "user name 2"),
                    true,
                ),
                Arguments.of(
                    listOf(
                        User(id = 123L, name = "user name 1"),
                        User(id = 234L, name = "user name 2"),
                        User(id = 345L, name = "user name 3"),
                        User(id = 456L, name = "user name 4"),
                    ),
                    User(id = 234L, name = "user name 2"),
                    // => this is precisely the same user,
                    false,
                ),
            )
    }
}

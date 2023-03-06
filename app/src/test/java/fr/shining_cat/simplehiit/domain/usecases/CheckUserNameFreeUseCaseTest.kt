package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckUserNameFreeUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = CheckUserNameFreeUseCase(mockSimpleHiitRepository, mockHiitLogger)

    private val testName = "this is a test name"

    @Test
    fun `calls repo and return error if repo returns error`() = runTest {
        val testException1 = Exception("this is a test exception 1")
        val usersError = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException1)
        coEvery { mockSimpleHiitRepository.getUsersList() } answers { usersError }
        //
        val output = testedUseCase.execute(testName)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.getUsersList() }
        assertEquals(usersError, output)
    }

    @ParameterizedTest(name = "{index} -> when input is {1} for list being {0}, result should be {2}")
    @MethodSource("getUsersListArguments")
    fun `calls repo and return Success with correct value repo returns success`(
        usersList: List<User>,
        input: String,
        expectedResult: Boolean
    ) = runTest {
        coEvery { mockSimpleHiitRepository.getUsersList() } answers { Output.Success(usersList) }
        //
        val output = testedUseCase.execute(input)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.getUsersList() }
        assertTrue(output is Output.Success)
        output as Output.Success
        assertEquals(expectedResult, output.result)
    }


    ///////////////////////
    private companion object {

        @JvmStatic
        fun getUsersListArguments() =
            Stream.of(
                Arguments.of(
                    listOf<User>(),
                    "whatever",
                    true
                ),
                Arguments.of(
                    listOf(User(name = "only user name")),
                    "different name",
                    true
                ),
                Arguments.of(
                    listOf(User(name = "only user name")),
                    "only user name",
                    false
                ),
                Arguments.of(
                    listOf(
                        User(name = "user name 1"),
                        User(name = "user name 2"),
                        User(name = "user name 3"),
                        User(name = "user name 4"),
                    ),
                    "a different name",
                    true
                ),
                Arguments.of(
                    listOf(
                        User(name = "user name 1"),
                        User(name = "user name 2"),
                        User(name = "user name 3"),
                        User(name = "user name 4"),
                    ),
                    "user name 2",
                    false
                )
            )
    }

}
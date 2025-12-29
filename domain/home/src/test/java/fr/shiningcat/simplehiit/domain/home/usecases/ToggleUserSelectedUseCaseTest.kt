package fr.shiningcat.simplehiit.domain.home.usecases

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
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class ToggleUserSelectedUseCaseTest : AbstractMockkTest() {
    private val mockUsersRepository = mockk<UsersRepository>()

    @Test
    fun `calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase =
                ToggleUserSelectedUseCase(
                    usersRepository = mockUsersRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    logger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val successFromRepo = Output.Success(1)
            coEvery { mockUsersRepository.updateUser(any()) } answers { successFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockUsersRepository.updateUser(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `calls repo with corresponding value and returns repo error`() =
        runTest {
            val testedUseCase =
                ToggleUserSelectedUseCase(
                    usersRepository = mockUsersRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    logger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockUsersRepository.updateUser(any()) } answers { errorFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockUsersRepository.updateUser(testValue) }
            assertEquals(errorFromRepo, result)
        }
}

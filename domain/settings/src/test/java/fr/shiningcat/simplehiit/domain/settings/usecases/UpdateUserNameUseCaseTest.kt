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

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateUserNameUseCaseTest : AbstractMockkTest() {
    private val mockUsersRepository = mockk<UsersRepository>()
    private val mockCheckIfAnotherUserUsesThatNameUseCase =
        mockk<CheckIfAnotherUserUsesThatNameUseCase>()

    @Test
    fun `if name is not used by any other user calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase =
                UpdateUserNameUseCase(
                    usersRepository = mockUsersRepository,
                    checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val successFromRepo = Output.Success(1)
            coEvery { mockUsersRepository.updateUser(any()) } answers { successFromRepo }
            val successFromNameCheck = Output.Success(false)
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromNameCheck }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `return corresponding error if checking name fails with error`() =
        runTest {
            val testedUseCase =
                UpdateUserNameUseCase(
                    usersRepository = mockUsersRepository,
                    checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { errorFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 0) { mockUsersRepository.updateUser(testValue) }
            assertEquals(errorFromRepo, result)
        }

    @Test
    fun `return correct error if name is already taken`() =
        runTest {
            val testedUseCase =
                UpdateUserNameUseCase(
                    usersRepository = mockUsersRepository,
                    checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val successFromRepo = Output.Success(true)
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 0) { mockUsersRepository.updateUser(testValue) }
            val expectedErrorCode = Constants.Errors.USER_NAME_TAKEN
            val expectedExceptionCode = Constants.Errors.USER_NAME_TAKEN.code
            assertTrue(result is Output.Error)
            result as Output.Error
            assertEquals(expectedErrorCode, result.errorCode)
            assertEquals(expectedExceptionCode, result.exception.message)
        }

    @Test
    fun `if name is not used by any other user but insertion fails returns repo insertion error`() =
        runTest {
            val testedUseCase =
                UpdateUserNameUseCase(
                    usersRepository = mockUsersRepository,
                    checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockUsersRepository.updateUser(any()) } answers { errorFromRepo }
            val successFromNameCheck = Output.Success(false)
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromNameCheck }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 1) { mockUsersRepository.updateUser(testValue) }
            assertEquals(errorFromRepo, result)
        }
}

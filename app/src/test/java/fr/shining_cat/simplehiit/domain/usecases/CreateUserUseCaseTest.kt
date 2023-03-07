package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CreateUserUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockCheckIfAnotherUserUsesThatNameUseCase = mockk<CheckIfAnotherUserUsesThatNameUseCase>()
    private val testedUseCase = CreateUserUseCase(mockSimpleHiitRepository, mockCheckIfAnotherUserUsesThatNameUseCase, mockHiitLogger)

    @Test
    fun `if name is not used by any other user calls repo with corresponding value and returns repo success`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val successFromRepo = Output.Success(1L)
        coEvery { mockSimpleHiitRepository.insertUser(any()) } answers {successFromRepo}
        val successFromNameCheck = Output.Success(false)
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers {successFromNameCheck}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 1) { mockSimpleHiitRepository.insertUser(testValue) }
        assertEquals(successFromRepo, result)
    }

    @Test
    fun `return corresponding error if checking name fails with error`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers {errorFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 0) { mockSimpleHiitRepository.insertUser(testValue) }
        assertEquals(errorFromRepo, result)
    }
    @Test
    fun `return correct error if name is already taken`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val successFromRepo = Output.Success(true)
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers {successFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 0) { mockSimpleHiitRepository.insertUser(testValue) }
        val expectedErrorCode = Constants.Errors.USER_NAME_TAKEN
        val expectedExceptionCode = Constants.Errors.USER_NAME_TAKEN.code
        assertTrue(result is Output.Error)
        result as Output.Error
        assertEquals(expectedErrorCode, result.errorCode)
        assertEquals(expectedExceptionCode, result.exception.message)
    }

    @Test
    fun `if name is not used by any other user but insertion fails returns repo insertion error`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockSimpleHiitRepository.insertUser(any()) } answers {errorFromRepo}
        val successFromNameCheck = Output.Success(false)
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers {successFromNameCheck}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 1) { mockSimpleHiitRepository.insertUser(testValue) }
        assertEquals(errorFromRepo, result)
    }}
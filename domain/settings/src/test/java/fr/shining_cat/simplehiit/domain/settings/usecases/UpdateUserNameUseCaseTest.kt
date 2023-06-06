package fr.shining_cat.simplehiit.domain.settings.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.domain.settings.usecases.CheckIfAnotherUserUsesThatNameUseCase
import fr.shining_cat.simplehiit.domain.settings.usecases.UpdateUserNameUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class UpdateUserNameUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockCheckIfAnotherUserUsesThatNameUseCase =
        mockk<CheckIfAnotherUserUsesThatNameUseCase>()

    @Test
    fun `if name is not used by any other user calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase = UpdateUserNameUseCase(
                simpleHiitRepository = mockSimpleHiitRepository,
                checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val successFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Success(1)
            coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { successFromRepo }
            val successFromNameCheck = fr.shining_cat.simplehiit.domain.common.Output.Success(false)
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromNameCheck }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `return corresponding error if checking name fails with error`() = runTest {
        val testedUseCase = UpdateUserNameUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Error(fr.shining_cat.simplehiit.domain.common.Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { errorFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 0) { mockSimpleHiitRepository.updateUser(testValue) }
        assertEquals(errorFromRepo, result)
    }

    @Test
    fun `return correct error if name is already taken`() = runTest {
        val testedUseCase = UpdateUserNameUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val successFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Success(true)
        coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
        coVerify(exactly = 0) { mockSimpleHiitRepository.updateUser(testValue) }
        val expectedErrorCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.USER_NAME_TAKEN
        val expectedExceptionCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.USER_NAME_TAKEN.code
        assertTrue(result is fr.shining_cat.simplehiit.domain.common.Output.Error)
        result as fr.shining_cat.simplehiit.domain.common.Output.Error
        assertEquals(expectedErrorCode, result.errorCode)
        assertEquals(expectedExceptionCode, result.exception.message)
    }

    @Test
    fun `if name is not used by any other user but insertion fails returns repo insertion error`() =
        runTest {
            val testedUseCase = UpdateUserNameUseCase(
                simpleHiitRepository = mockSimpleHiitRepository,
                checkIfAnotherUserUsesThatNameUseCase = mockCheckIfAnotherUserUsesThatNameUseCase,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
            val testValue = User(id = 123L, name = "test user name", selected = true)
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                fr.shining_cat.simplehiit.domain.common.Output.Error(fr.shining_cat.simplehiit.domain.common.Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockSimpleHiitRepository.updateUser(any()) } answers { errorFromRepo }
            val successFromNameCheck = fr.shining_cat.simplehiit.domain.common.Output.Success(false)
            coEvery { mockCheckIfAnotherUserUsesThatNameUseCase.execute(any()) } answers { successFromNameCheck }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockCheckIfAnotherUserUsesThatNameUseCase.execute(testValue) }
            coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(testValue) }
            assertEquals(errorFromRepo, result)
        }
}
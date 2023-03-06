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
internal class ToggleUserSelectedUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = ToggleUserSelectedUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls repo with corresponding value and returns repo success`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val successFromRepo = Output.Success(1)
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers {successFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(testValue) }
        assertEquals(successFromRepo, result)
    }

    @Test
    fun `calls repo with corresponding value and returns repo error`() = runTest {
        val testValue = User(id = 123L, name = "test user name", selected = true)
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockSimpleHiitRepository.updateUser(any()) } answers {errorFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.updateUser(testValue) }
        assertEquals(errorFromRepo, result)
    }
}
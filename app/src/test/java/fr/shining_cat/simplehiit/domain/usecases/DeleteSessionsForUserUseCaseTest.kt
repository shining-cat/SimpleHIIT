package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

internal class DeleteSessionsForUserUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = DeleteSessionsForUserUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @ParameterizedTest(name = "{index} -> when repo returns {0} should return success with {0}")
    @ValueSource(ints = [0, 1, 5, 9, 23, 64])
    fun `calls repo with corresponding value and returns repo success`() = runTest {
        val testValue = 123L
        val successFromRepo = Output.Success(1)
        coEvery { mockSimpleHiitRepository.deleteSessionsForUser(any()) } answers {successFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteSessionsForUser(testValue) }
        assertEquals(successFromRepo, result)
    }

    @Test
    fun `calls repo with corresponding value and returns repo error`() = runTest {
        val testValue = 123L
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockSimpleHiitRepository.deleteSessionsForUser(any()) } answers {errorFromRepo}
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteSessionsForUser(testValue) }
        assertEquals(errorFromRepo, result)
    }

}
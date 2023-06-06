package fr.shining_cat.simplehiit.domain.statistics.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.statistics.usecases.DeleteSessionsForUserUseCase
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class DeleteSessionsForUserUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @ParameterizedTest(name = "{index} -> when repo returns {0} should return success with {0}")
    @ValueSource(ints = [0, 1, 5, 9, 23, 64])
    fun `calls repo with corresponding value and returns repo success`() = runTest {
        val testedUseCase =
            DeleteSessionsForUserUseCase(
                simpleHiitRepository = mockSimpleHiitRepository,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
        val testValue = 123L
        val successFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Success(1)
        coEvery { mockSimpleHiitRepository.deleteSessionRecordsForUser(any()) } answers { successFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteSessionRecordsForUser(testValue) }
        assertEquals(successFromRepo, result)
    }

    @Test
    fun `calls repo with corresponding value and returns repo error`() = runTest {
        val testedUseCase =
            DeleteSessionsForUserUseCase(
                simpleHiitRepository = mockSimpleHiitRepository,
                defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                simpleHiitLogger = mockHiitLogger
            )
        val testValue = 123L
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Error(fr.shining_cat.simplehiit.domain.common.Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockSimpleHiitRepository.deleteSessionRecordsForUser(any()) } answers { errorFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.deleteSessionRecordsForUser(testValue) }
        assertEquals(errorFromRepo, result)
    }

}
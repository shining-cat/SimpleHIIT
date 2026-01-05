package fr.shiningcat.simplehiit.domain.statistics.usecases

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class DeleteSessionsForUserUseCaseTest : AbstractMockkTest() {
    private val mockSessionsRepository = mockk<SessionsRepository>()

    @ParameterizedTest(name = "{index} -> when repo returns {0} should return success with {0}")
    @ValueSource(ints = [0, 1, 5, 9, 23, 64])
    fun `calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase =
                DeleteSessionsForUserUseCase(
                    sessionsRepository = mockSessionsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = 123L
            val successFromRepo = Output.Success(1)
            coEvery { mockSessionsRepository.deleteSessionRecordsForUser(any()) } answers { successFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSessionsRepository.deleteSessionRecordsForUser(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `calls repo with corresponding value and returns repo error`() =
        runTest {
            val testedUseCase =
                DeleteSessionsForUserUseCase(
                    sessionsRepository = mockSessionsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = 123L
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                Output.Error(DomainError.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockSessionsRepository.deleteSessionRecordsForUser(any()) } answers { errorFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSessionsRepository.deleteSessionRecordsForUser(testValue) }
            assertEquals(errorFromRepo, result)
        }
}

package fr.shiningcat.simplehiit.domain.session.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SessionsRepository
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
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
internal class InsertSessionUseCaseTest : AbstractMockkTest() {
    private val mockSessionsRepository = mockk<SessionsRepository>()

    @Test
    fun `calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase =
                InsertSessionUseCase(
                    sessionsRepository = mockSessionsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue =
                SessionRecord(
                    id = 123L,
                    timeStamp = 78696L,
                    durationMs = 345L,
                    usersIds = listOf(1234L, 2345L),
                )
            val successFromRepo = Output.Success(2)
            coEvery { mockSessionsRepository.insertSessionRecord(any()) } answers { successFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSessionsRepository.insertSessionRecord(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `calls repo with corresponding value and returns repo error`() =
        runTest {
            val testedUseCase =
                InsertSessionUseCase(
                    sessionsRepository = mockSessionsRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue =
                SessionRecord(
                    id = 123L,
                    timeStamp = 78696L,
                    durationMs = 345L,
                    usersIds = listOf(1234L, 2345L),
                )
            val exceptionMessage = "this is a test exception"
            val errorFromRepo =
                Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockSessionsRepository.insertSessionRecord(any()) } answers { errorFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSessionsRepository.insertSessionRecord(testValue) }
            assertEquals(errorFromRepo, result)
        }
}

package fr.shining_cat.simplehiit.domain.common.usecases

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.SessionRecord
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class InsertSessionUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @Test
    fun `calls repo with corresponding value and returns repo success`() = runTest {
        val testedUseCase = InsertSessionUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testValue = SessionRecord(
            id = 123L,
            timeStamp = 78696L,
            durationMs = 345L,
            usersIds = listOf(1234L, 2345L)
        )
        val successFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Success(2)
        coEvery { mockSimpleHiitRepository.insertSessionRecord(any()) } answers { successFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.insertSessionRecord(testValue) }
        assertEquals(successFromRepo, result)
    }

    @Test
    fun `calls repo with corresponding value and returns repo error`() = runTest {
        val testedUseCase = InsertSessionUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testValue = SessionRecord(
            id = 123L,
            timeStamp = 78696L,
            durationMs = 345L,
            usersIds = listOf(1234L, 2345L)
        )
        val exceptionMessage = "this is a test exception"
        val errorFromRepo = fr.shining_cat.simplehiit.domain.common.Output.Error(fr.shining_cat.simplehiit.domain.common.Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
        coEvery { mockSimpleHiitRepository.insertSessionRecord(any()) } answers { errorFromRepo }
        //
        val result = testedUseCase.execute(testValue)
        //
        coVerify(exactly = 1) { mockSimpleHiitRepository.insertSessionRecord(testValue) }
        assertEquals(errorFromRepo, result)
    }
}
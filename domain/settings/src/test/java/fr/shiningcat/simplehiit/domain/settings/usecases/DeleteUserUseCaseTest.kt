package fr.shiningcat.simplehiit.domain.settings.usecases

import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
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
internal class DeleteUserUseCaseTest : AbstractMockkTest() {
    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()

    @Test
    fun `calls repo with corresponding value and returns repo success`() =
        runTest {
            val testedUseCase =
                DeleteUserUseCase(
                    simpleHiitRepository = mockSimpleHiitRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(name = "test user name", selected = true)
            val successFromRepo = Output.Success(1)
            coEvery { mockSimpleHiitRepository.deleteUser(any()) } answers { successFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitRepository.deleteUser(testValue) }
            assertEquals(successFromRepo, result)
        }

    @Test
    fun `calls repo with corresponding value and returns repo error`() =
        runTest {
            val testedUseCase =
                DeleteUserUseCase(
                    simpleHiitRepository = mockSimpleHiitRepository,
                    defaultDispatcher = UnconfinedTestDispatcher(testScheduler),
                    simpleHiitLogger = mockHiitLogger,
                )
            val testValue = User(name = "test user name", selected = true)
            val exceptionMessage = "this is a test exception"
            val errorFromRepo = Output.Error(Constants.Errors.EMPTY_RESULT, Exception(exceptionMessage))
            coEvery { mockSimpleHiitRepository.deleteUser(any()) } answers { errorFromRepo }
            //
            val result = testedUseCase.execute(testValue)
            //
            coVerify(exactly = 1) { mockSimpleHiitRepository.deleteUser(testValue) }
            assertEquals(errorFromRepo, result)
        }
}

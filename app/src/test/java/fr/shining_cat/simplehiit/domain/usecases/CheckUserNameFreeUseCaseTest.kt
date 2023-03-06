package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class CheckUserNameFreeUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = CheckUserNameFreeUseCase(mockSimpleHiitRepository, mockHiitLogger)

    private val testName = "this is a test name"

/*
    @Test
    fun `calls repo and return error if repo returns error`() = runTest {
        val testException1 = Exception("this is a test exception 1")
        val testException2 = Exception("this is a test exception 2")
        val usersError1 = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException1)
        val usersError2 = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException2)
        val usersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        println("test start")
        usersFlow.emit(usersError1)
        println("first emission")
        val result = testedUseCase.execute(testName)
        println("execute called")
        usersFlow.emit(usersError2)
        println("second emission")
        advanceUntilIdle()
        println("advance")
        assertEquals(usersError1, result)
    }
*/


}
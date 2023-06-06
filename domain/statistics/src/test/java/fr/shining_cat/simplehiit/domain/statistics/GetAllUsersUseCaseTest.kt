package fr.shining_cat.simplehiit.domain.statistics

import fr.shining_cat.simplehiit.domain.common.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetAllUsersUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val testedUseCase = fr.shining_cat.simplehiit.domain.statistics.GetAllUsersUseCase(
        mockSimpleHiitRepository,
        mockHiitLogger
    )

    @Test
    fun `calls repo and return corresponding values in order`() = runTest {
        val user1 = User(id = 123L, name = "user 1 name", selected = true)
        val user2 = User(id = 234L, name = "user 2 name", selected = true)
        val user3 = User(id = 345L, name = "user 3 name", selected = true)
        val user4 = User(id = 456L, name = "user 4 name", selected = true)
        val usersList1 = fr.shining_cat.simplehiit.domain.common.Output.Success(listOf(user1, user3))
        val usersList2 = fr.shining_cat.simplehiit.domain.common.Output.Success(listOf(user1, user2, user4))
        val usersFlow = MutableSharedFlow<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val usersFlowAsList = mutableListOf<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(usersFlowAsList)
        }
        //
        usersFlow.emit(usersList1)
        assertEquals(1, usersFlowAsList.size)
        val users1 = usersFlowAsList[0]
        assertEquals(usersList1, users1)
        //
        usersFlow.emit(usersList2)
        assertEquals(2, usersFlowAsList.size)
        val users2 = usersFlowAsList[1]
        assertEquals(usersList2, users2)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo and return error if repo returns empty list`() = runTest {
        val usersEmptyList = fr.shining_cat.simplehiit.domain.common.Output.Success(emptyList<User>())
        val usersFlow = MutableSharedFlow<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val usersFlowAsList = mutableListOf<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(usersFlowAsList)
        }
        //
        usersFlow.emit(usersEmptyList)
        assertEquals(1, usersFlowAsList.size)
        val users = usersFlowAsList[0]
        assertTrue(users is fr.shining_cat.simplehiit.domain.common.Output.Error)
        users as fr.shining_cat.simplehiit.domain.common.Output.Error
        assertEquals(fr.shining_cat.simplehiit.domain.common.Constants.Errors.NO_USERS_FOUND, users.errorCode)
        assertEquals(fr.shining_cat.simplehiit.domain.common.Constants.NO_RESULTS_FOUND, users.exception.message)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo and return error if repo returns error`() = runTest {
        val testException = Exception("this is a test exception")
        val usersError = fr.shining_cat.simplehiit.domain.common.Output.Error(fr.shining_cat.simplehiit.domain.common.Constants.Errors.DATABASE_FETCH_FAILED, testException)
        val usersFlow = MutableSharedFlow<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getUsers() } answers { usersFlow }
        //
        val usersFlowAsList = mutableListOf<fr.shining_cat.simplehiit.domain.common.Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(usersFlowAsList)
        }
        //
        usersFlow.emit(usersError)
        assertEquals(1, usersFlowAsList.size)
        val users = usersFlowAsList[0]
        assertEquals(usersError, users)
        //
        collectJob.cancel()
    }

}
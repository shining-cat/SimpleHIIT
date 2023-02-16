package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.utils.HiitLogger
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
internal class GetSelectedUsersUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockSimpleHiitLogger = mockk<HiitLogger>()
    private val testedUseCase = GetSelectedUsersUseCase(mockSimpleHiitRepository, mockHiitLogger)

    @Test
    fun `calls repo and return corresponding values in order`() = runTest {
        val user1 = User(id = 123L, name = "user 1 name", selected = true)
        val user2 = User(id = 234L, name = "user 2 name", selected = true)
        val user3 = User(id = 345L, name = "user 3 name", selected = true)
        val user4 = User(id = 456L, name = "user 4 name", selected = true)
        val selectedUsersList1 = Output.Success(listOf(user1, user3))
        val selectedUsersList2 = Output.Success(listOf(user1, user2, user4))
        val selectedUsersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getSelectedUsers() } answers { selectedUsersFlow }
        //
        val selectedUsersFlowAsList = mutableListOf<Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(selectedUsersFlowAsList)
        }
        //
        selectedUsersFlow.emit(selectedUsersList1)
        assertEquals(1, selectedUsersFlowAsList.size)
        val selectedUsers1 = selectedUsersFlowAsList[0]
        assertEquals(selectedUsersList1, selectedUsers1)
        //
        selectedUsersFlow.emit(selectedUsersList2)
        assertEquals(2, selectedUsersFlowAsList.size)
        val selectedUsers2 = selectedUsersFlowAsList[1]
        assertEquals(selectedUsersList2, selectedUsers2)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo and return error if repo returns empty list`() = runTest {
        val selectedUsersEmptyList = Output.Success(emptyList<User>())
        val selectedUsersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getSelectedUsers() } answers { selectedUsersFlow }
        //
        val selectedUsersFlowAsList = mutableListOf<Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(selectedUsersFlowAsList)
        }
        //
        selectedUsersFlow.emit(selectedUsersEmptyList)
        assertEquals(1, selectedUsersFlowAsList.size)
        val selectedUsers = selectedUsersFlowAsList[0]
        assertTrue(selectedUsers is Output.Error)
        selectedUsers as Output.Error
        assertEquals(Constants.Errors.EMPTY_RESULT, selectedUsers.errorCode)
        assertEquals(Constants.NO_RESULTS_FOUND, selectedUsers.exception.message)
        //
        collectJob.cancel()
    }

    @Test
    fun `calls repo and return error if repo returns error`() = runTest {
        val testException = Exception("this is a test exception")
        val selectedUsersError = Output.Error(Constants.Errors.DATABASE_FETCH_FAILED, testException)
        val selectedUsersFlow = MutableSharedFlow<Output<List<User>>>()
        coEvery { mockSimpleHiitRepository.getSelectedUsers() } answers { selectedUsersFlow }
        //
        val selectedUsersFlowAsList = mutableListOf<Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            testedUseCase.execute().toList(selectedUsersFlowAsList)
        }
        //
        selectedUsersFlow.emit(selectedUsersError)
        assertEquals(1, selectedUsersFlowAsList.size)
        val selectedUsers = selectedUsersFlowAsList[0]
        assertEquals(selectedUsersError, selectedUsers)
        //
        collectJob.cancel()
    }

}
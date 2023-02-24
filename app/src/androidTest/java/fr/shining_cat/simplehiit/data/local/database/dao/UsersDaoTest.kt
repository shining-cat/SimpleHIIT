package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UsersDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: SimpleHiitDatabase
    private lateinit var usersDao: UsersDao

    @Before
    fun setup() {
        hiltRule.inject()
        usersDao = database.userDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

////////////////

    @Test
    fun testInsertUser() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch { usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getUsers().first()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
    }

    @Test
    fun testInsertUserWithId() = runTest {
        val testUserID = 1243L
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getUsers().first()
        assertEquals(1, tableContent.size)
        assertEquals(testUserID, tableContent[0].userId)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
    }

    @Test
    fun testInsertUserWithConflict() = runTest {
        //inserting first user
        val testUserID = 1243L
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //inserting second user
        val testUserName2 = "test user name"
        val testSelected2 = false
        val user2 = UserEntity(userId = testUserID, name = testUserName2, selected = testSelected2)
        launch {usersDao.insert(user2)}
        advanceUntilIdle()
        //only second user should be in table
        val tableContent = usersDao.getUsers().first()
        assertEquals(1, tableContent.size)
        assertEquals(testUserID, tableContent[0].userId)
        assertEquals(testUserName2, tableContent[0].name)
        assertEquals(testSelected2, tableContent[0].selected)
    }

////////////////

    @Test
    fun getUsersEmptyTable() = runTest {
        val tableContent = usersDao.getUsers().first()
        assertTrue(tableContent.isEmpty())
    }

    @Test
    fun getUsersOnlyOne() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getUsers().first()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
    }

    @Test
    fun getUsersList() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        usersDao.insert(user1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        usersDao.insert(user2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        usersDao.insert(user3)
        val testUserName4 = "test user name 4"
        val testSelected4 = false
        val user4 = UserEntity(name = testUserName4, selected = testSelected4)
        usersDao.insert(user4)
        val testUserName5 = "test user name 5"
        val testSelected5 = true
        val user5 = UserEntity(name = testUserName5, selected = testSelected5)
        launch {usersDao.insert(user5)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getUsers().first()
        assertEquals(5, tableContent.size)
        assertEquals(testUserName1, tableContent[0].name)
        assertEquals(testSelected1, tableContent[0].selected)
        assertEquals(testUserName2, tableContent[1].name)
        assertEquals(testSelected2, tableContent[1].selected)
        assertEquals(testUserName3, tableContent[2].name)
        assertEquals(testSelected3, tableContent[2].selected)
        assertEquals(testUserName4, tableContent[3].name)
        assertEquals(testSelected4, tableContent[3].selected)
        assertEquals(testUserName5, tableContent[4].name)
        assertEquals(testSelected5, tableContent[4].selected)
    }
    @Test
    fun getUsersAsTheyAreInserted() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        //
        val usersFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            usersDao.getUsers().toList(usersFlowAsList)
        }
        //
        assertEquals(1, usersFlowAsList.size)//first emission is an empty state
        //
        usersDao.insert(user1)//this insertion will cause an emission
        assertEquals(2, usersFlowAsList.size)
        val users1 = usersFlowAsList[1]
        assertEquals(1, users1.size)
        assertEquals(testUserName1, users1[0].name)
        assertEquals(testSelected1, users1[0].selected)
        //
        usersDao.insert(user2) //this insertion will cause an emission
        usersDao.insert(user3)//this insertion will cause an emission
        assertEquals(4, usersFlowAsList.size) // flow has emitted 4 times
        val users2 = usersFlowAsList[3]
        assertEquals(3, users2.size) //we have now inserted a total of 3 users
        assertEquals(testUserName1, users2[0].name)
        assertEquals(testSelected1, users2[0].selected)
        assertEquals(testUserName2, users2[1].name)
        assertEquals(testSelected2, users2[1].selected)
        assertEquals(testUserName3, users2[2].name)
        assertEquals(testSelected3, users2[2].selected)
        //
        collectJob.cancel()
    }

////////////////

    @Test
    fun getSelectedUsersEmptyTable() = runTest {
        val tableContent = usersDao.getSelectedUsers().first()
        assertTrue(tableContent.isEmpty())
    }

    @Test
    fun getSelectedUsersOnlyOneAndIsSelected() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getSelectedUsers().first()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
    }

    @Test
    fun getSelectedUsersOnlyOneAndIsNotSelected() = runTest {
        val testUserName = "test user name"
        val testSelected = false
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableContent = usersDao.getSelectedUsers().first()
        assertEquals(0, tableContent.size)
    }

    @Test
    fun getSelectedUsersList() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        val testUserName4 = "test user name 4"
        val testSelected4 = false
        val user4 = UserEntity(name = testUserName4, selected = testSelected4)
        val testUserName5 = "test user name 5"
        val testSelected5 = true
        val user5 = UserEntity(name = testUserName5, selected = testSelected5)
        launch {
            usersDao.insert(user1)
            usersDao.insert(user2)
            usersDao.insert(user3)
            usersDao.insert(user4)
            usersDao.insert(user5)
        }
        advanceUntilIdle()
        //
        val tableContent = usersDao.getSelectedUsers().first()
        assertEquals(3, tableContent.size)//we got all the users with selected = true
        assertEquals(testUserName1, tableContent[0].name)
        assertEquals(testUserName3, tableContent[1].name)
        assertEquals(testUserName5, tableContent[2].name)
        //assert that all returned users are selected=true
        for(user in tableContent){
            assertTrue(user.selected)
        }
    }

    @Test
    fun getSelectedUsersAsTheyAreInserted() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        //
        val usersFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            usersDao.getSelectedUsers().toList(usersFlowAsList)
        }
        //
        assertEquals(1, usersFlowAsList.size)//first emission is an empty state
        //
        usersDao.insert(user1)//this insertion will cause an emission
        assertEquals(2, usersFlowAsList.size)
        val users1 = usersFlowAsList[1]
        assertEquals(1, users1.size)
        assertEquals(testUserName1, users1[0].name)
        assertEquals(testSelected1, users1[0].selected)
        //
        usersDao.insert(user2) //this insertion will cause an emission
        usersDao.insert(user3)//this insertion will cause an emission
        assertEquals(4, usersFlowAsList.size) // flow has emitted 4 times
        val users2 = usersFlowAsList[3]
        assertEquals(2, users2.size) //we have now inserted a total of 3 users, but one is not selected
        assertEquals(testUserName1, users2[0].name)
        assertEquals(testSelected1, users2[0].selected)
        assertEquals(testUserName3, users2[1].name)
        assertEquals(testSelected3, users2[1].selected)
        //
        collectJob.cancel()
    }

////////////////

    @Test
    fun updateUser() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        //
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(1, tableContentBefore.size)
        assertEquals(testUserName, tableContentBefore[0].name)
        assertEquals(testSelected, tableContentBefore[0].selected)
        //
        val userId = tableContentBefore[0].userId
        val updatedName = "updated user name"
        val updatedSelected = false
        val updatedUser = tableContentBefore[0].copy(name = updatedName, selected = updatedSelected)
        val updated = usersDao.update(updatedUser)
        //
        assertEquals(1, updated)
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertEquals(1, tableContentAfter.size)
        assertEquals(userId, tableContentAfter[0].userId)
        assertEquals(updatedName, tableContentAfter[0].name)
        assertEquals(updatedSelected, tableContentAfter[0].selected)
        collectJob.cancel()
    }

    @Test
    fun updateUserNotPresent() = runTest {
        val userId = 1234L
        val updatedName = "updated user name"
        val updatedSelected = false
        val updatedUser = UserEntity(userId = userId, name = updatedName, selected = updatedSelected)
        var updated:Int = -1
        launch {updated = usersDao.update(updatedUser)}
        advanceUntilIdle()
        //
        assertEquals(0, updated)
        val tableContentUpdated = usersDao.getUsers().first()
        assertEquals(0, tableContentUpdated.size)
    }

    @Test
    fun updateOneUserDoesNotAffectRestOfTable() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        val testUserName4 = "test user name 4"
        val testSelected4 = false
        val user4 = UserEntity(name = testUserName4, selected = testSelected4)
        val testUserName5 = "test user name 5"
        val testSelected5 = true
        val user5 = UserEntity(name = testUserName5, selected = testSelected5)
        launch {
            usersDao.insert(user1)
            usersDao.insert(user2)
            usersDao.insert(user3)
            usersDao.insert(user4)
            usersDao.insert(user5)
        }
        advanceUntilIdle()
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(5, tableContentBefore.size)
        assertEquals(testUserName1, tableContentBefore[0].name)
        assertEquals(testSelected1, tableContentBefore[0].selected)
        assertEquals(testUserName2, tableContentBefore[1].name)
        assertEquals(testSelected2, tableContentBefore[1].selected)
        assertEquals(testUserName3, tableContentBefore[2].name)
        assertEquals(testSelected3, tableContentBefore[2].selected)
        assertEquals(testUserName4, tableContentBefore[3].name)
        assertEquals(testSelected4, tableContentBefore[3].selected)
        assertEquals(testUserName5, tableContentBefore[4].name)
        assertEquals(testSelected5, tableContentBefore[4].selected)
        //NOW updating user 3
        val userNameUpdated = "updated user name"
        val userSelectedUpdated = false
        val userUpdated = tableContentBefore[2].copy(name = userNameUpdated, selected = userSelectedUpdated)
        var updated = -1
        launch {updated = usersDao.update(userUpdated)}
        advanceUntilIdle()
        //
        assertEquals(1, updated)
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertEquals(5, tableContentAfter.size)
        assertEquals(testUserName1, tableContentAfter[0].name)
        assertEquals(testSelected1, tableContentAfter[0].selected)
        assertEquals(testUserName2, tableContentAfter[1].name)
        assertEquals(testSelected2, tableContentAfter[1].selected)
        assertEquals(userNameUpdated, tableContentAfter[2].name)
        assertEquals(userSelectedUpdated, tableContentAfter[2].selected)
        assertEquals(testUserName4, tableContentAfter[3].name)
        assertEquals(testSelected4, tableContentAfter[3].selected)
        assertEquals(testUserName5, tableContentAfter[4].name)
        assertEquals(testSelected5, tableContentAfter[4].selected)
        collectJob.cancel()
    }

////////////////

    @Test
    fun deleteUserEmptyTable() = runTest {
        val userId = 12345L
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(userId = userId, name = testUserName, selected = testSelected)
        var deleted = -1
        launch {deleted = usersDao.delete(user)}
        advanceUntilIdle()
        assertEquals(0, deleted)
    }

    @Test
    fun deleteUserOnlyOneInTable() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(1, tableContentBefore.size)
        assertEquals(testUserName, tableContentBefore[0].name)
        assertEquals(testSelected, tableContentBefore[0].selected)
        //
        val userToDelete = tableContentBefore[0]
        var deleted = -1
        launch {deleted = usersDao.delete(userToDelete)}
        advanceUntilIdle()
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertEquals(1, deleted)
        assertEquals(0, tableContentAfter.size)
        collectJob.cancel()
    }

    @Test
    fun deleteOneUserDoesNotAffectRestOfTable() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        val testUserName4 = "test user name 4"
        val testSelected4 = false
        val user4 = UserEntity(name = testUserName4, selected = testSelected4)
        val testUserName5 = "test user name 5"
        val testSelected5 = true
        val user5 = UserEntity(name = testUserName5, selected = testSelected5)
        launch {
            usersDao.insert(user1)
            usersDao.insert(user2)
            usersDao.insert(user3)
            usersDao.insert(user4)
            usersDao.insert(user5)}
        advanceUntilIdle()
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(5, tableContentBefore.size)
        assertEquals(testUserName1, tableContentBefore[0].name)
        assertEquals(testSelected1, tableContentBefore[0].selected)
        assertEquals(testUserName2, tableContentBefore[1].name)
        assertEquals(testSelected2, tableContentBefore[1].selected)
        assertEquals(testUserName3, tableContentBefore[2].name)
        assertEquals(testSelected3, tableContentBefore[2].selected)
        assertEquals(testUserName4, tableContentBefore[3].name)
        assertEquals(testSelected4, tableContentBefore[3].selected)
        assertEquals(testUserName5, tableContentBefore[4].name)
        assertEquals(testSelected5, tableContentBefore[4].selected)
        //NOW deleting user 3
        val userToDelete = tableContentBefore[2]
        var deleted = -1
        launch {deleted = usersDao.delete(userToDelete)}
        advanceUntilIdle()
        //
        assertEquals(1, deleted)
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertEquals(4, tableContentAfter.size)
        assertEquals(testUserName1, tableContentAfter[0].name)
        assertEquals(testSelected1, tableContentAfter[0].selected)
        assertEquals(testUserName2, tableContentAfter[1].name)
        assertEquals(testSelected2, tableContentAfter[1].selected)
        assertEquals(testUserName4, tableContentAfter[2].name)
        assertEquals(testSelected4, tableContentAfter[2].selected)
        assertEquals(testUserName5, tableContentAfter[3].name)
        assertEquals(testSelected5, tableContentAfter[3].selected)
        collectJob.cancel()
    }


////////////////

    @Test
    fun deleteAllUsersEmptyTable() = runTest {
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertTrue(tableContentBefore.isEmpty())
        //
        launch {usersDao.deleteAllUsers()}
        advanceUntilIdle()
        assertEquals(1, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[0]
        assertTrue(tableContentAfter.isEmpty())
        collectJob.cancel()

    }

    @Test
    fun deleteAllUsersOnlyOneInTable() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        launch {usersDao.insert(user)}
        advanceUntilIdle()
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(1, tableContentBefore.size)
        assertEquals(testUserName, tableContentBefore[0].name)
        assertEquals(testSelected, tableContentBefore[0].selected)
        //
        launch {usersDao.deleteAllUsers()}
        advanceUntilIdle()
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertTrue(tableContentAfter.isEmpty())
        collectJob.cancel()
    }

    @Test
    fun deleteAllUsersEmptiesTable() = runTest {
        val testUserName1 = "test user name 1"
        val testSelected1 = true
        val user1 = UserEntity(name = testUserName1, selected = testSelected1)
        val testUserName2 = "test user name 2"
        val testSelected2 = false
        val user2 = UserEntity(name = testUserName2, selected = testSelected2)
        val testUserName3 = "test user name 3"
        val testSelected3 = true
        val user3 = UserEntity(name = testUserName3, selected = testSelected3)
        val testUserName4 = "test user name 4"
        val testSelected4 = false
        val user4 = UserEntity(name = testUserName4, selected = testSelected4)
        val testUserName5 = "test user name 5"
        val testSelected5 = true
        val user5 = UserEntity(name = testUserName5, selected = testSelected5)
        launch {
            usersDao.insert(user1)
            usersDao.insert(user2)
            usersDao.insert(user3)
            usersDao.insert(user4)
            usersDao.insert(user5)}
        advanceUntilIdle()
        //
        val tableFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(tableFlowAsList)
        }
        val tableContentBefore = tableFlowAsList[0]
        assertEquals(5, tableContentBefore.size)
        assertEquals(testUserName1, tableContentBefore[0].name)
        assertEquals(testSelected1, tableContentBefore[0].selected)
        assertEquals(testUserName2, tableContentBefore[1].name)
        assertEquals(testSelected2, tableContentBefore[1].selected)
        assertEquals(testUserName3, tableContentBefore[2].name)
        assertEquals(testSelected3, tableContentBefore[2].selected)
        assertEquals(testUserName4, tableContentBefore[3].name)
        assertEquals(testSelected4, tableContentBefore[3].selected)
        assertEquals(testUserName5, tableContentBefore[4].name)
        assertEquals(testSelected5, tableContentBefore[4].selected)
        //
        launch {usersDao.deleteAllUsers()}
        advanceUntilIdle()
        assertEquals(2, tableFlowAsList.size)
        val tableContentAfter = tableFlowAsList[1]
        assertTrue(tableContentAfter.isEmpty())
        collectJob.cancel()
    }


}
package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        usersDao.insert(user)
        //
        val tableContent = usersDao.getUsers()
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
        usersDao.insert(user)
        //
        val tableContent = usersDao.getUsers()
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
        usersDao.insert(user)
        //inserting second user
        val testUserName2 = "test user name"
        val testSelected2 = false
        val user2 = UserEntity(userId = testUserID, name = testUserName2, selected = testSelected2)
        usersDao.insert(user2)
        //only second user should be in table
        val tableContent = usersDao.getUsers()
        assertEquals(1, tableContent.size)
        assertEquals(testUserID, tableContent[0].userId)
        assertEquals(testUserName2, tableContent[0].name)
        assertEquals(testSelected2, tableContent[0].selected)
    }

////////////////

    @Test
    fun getUsersEmptyTable() = runTest {
        val tableContent = usersDao.getUsers()
        assertTrue(tableContent.isEmpty())
    }

    @Test
    fun getUsersOnlyOne() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableContent = usersDao.getUsers()
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
        usersDao.insert(user5)
        //
        val tableContent = usersDao.getUsers()
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

////////////////

    @Test
    fun getSelectedUsersEmptyTable() = runTest {
        val tableContent = usersDao.getSelectedUsers()
        assertTrue(tableContent.isEmpty())
    }

    @Test
    fun getSelectedUsersOnlyOneIsSelected() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableContent = usersDao.getSelectedUsers()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
    }

    @Test
    fun getSelectedUsersOnlyOneIsNotSelected() = runTest {
        val testUserName = "test user name"
        val testSelected = false
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableContent = usersDao.getSelectedUsers()
        assertEquals(0, tableContent.size)
    }

    @Test
    fun getSelectedUsersList() = runTest {
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
        usersDao.insert(user5)
        //
        val tableContent = usersDao.getSelectedUsers()
        assertEquals(3, tableContent.size)
        assertEquals(testUserName1, tableContent[0].name)
        assertEquals(testSelected1, tableContent[0].selected)
        assertEquals(testUserName3, tableContent[1].name)
        assertEquals(testSelected3, tableContent[1].selected)
        assertEquals(testUserName5, tableContent[2].name)
        assertEquals(testSelected5, tableContent[2].selected)
    }

////////////////

    @Test
    fun updateUser() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableContent = usersDao.getUsers()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
        //
        val userId = tableContent[0].userId
        val updatedName = "updated user name"
        val updatedSelected = false
        val updatedUser = tableContent[0].copy(name = updatedName, selected = updatedSelected)
        val updated = usersDao.update(updatedUser)
        //
        assertEquals(1, updated)
        val tableContentUpdated = usersDao.getUsers()
        assertEquals(1, tableContentUpdated.size)
        assertEquals(userId, tableContentUpdated[0].userId)
        assertEquals(updatedName, tableContentUpdated[0].name)
        assertEquals(updatedSelected, tableContentUpdated[0].selected)
    }

    @Test
    fun updateUserNotPresent() = runTest {
        val userId = 1234L
        val updatedName = "updated user name"
        val updatedSelected = false
        val updatedUser = UserEntity(userId = userId, name = updatedName, selected = updatedSelected)
        val updated = usersDao.update(updatedUser)
        //
        assertEquals(0, updated)
        val tableContentUpdated = usersDao.getUsers()
        assertEquals(0, tableContentUpdated.size)
    }

    @Test
    fun updateOneUserDoesNotAffectRestOfTable() = runTest {
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
        usersDao.insert(user5)
        //
        val tableContent = usersDao.getUsers()
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
        //NOW updating user 3
        val userNameUpdated = "updated user name"
        val userSelectedUpdated = false
        val userUpdated = tableContent[2].copy(name = userNameUpdated, selected = userSelectedUpdated)
        val updated = usersDao.update(userUpdated)
        //
        assertEquals(1, updated)
        val tableContentUpdated = usersDao.getUsers()
        assertEquals(5, tableContent.size)
        assertEquals(testUserName1, tableContentUpdated[0].name)
        assertEquals(testSelected1, tableContentUpdated[0].selected)
        assertEquals(testUserName2, tableContentUpdated[1].name)
        assertEquals(testSelected2, tableContentUpdated[1].selected)
        assertEquals(userNameUpdated, tableContentUpdated[2].name)
        assertEquals(userSelectedUpdated, tableContentUpdated[2].selected)
        assertEquals(testUserName4, tableContentUpdated[3].name)
        assertEquals(testSelected4, tableContentUpdated[3].selected)
        assertEquals(testUserName5, tableContentUpdated[4].name)
        assertEquals(testSelected5, tableContentUpdated[4].selected)

    }

////////////////

    @Test
    fun deleteUserEmptyTable() = runTest {
        val userId = 12345L
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(userId = userId, name = testUserName, selected = testSelected)
        val deleted = usersDao.delete(user)
        assertEquals(0, deleted)
    }

    @Test
    fun deleteUserOnlyOneInTable() = runTest {
        val testUserName = "test user name"
        val testSelected = true
        val user = UserEntity(name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val tableContent = usersDao.getUsers()
        assertEquals(1, tableContent.size)
        assertEquals(testUserName, tableContent[0].name)
        assertEquals(testSelected, tableContent[0].selected)
        //
        val userToDelete = tableContent[0]
        val deleted = usersDao.delete(userToDelete)
        assertEquals(1, deleted)
        val tableContentAfterDelete = usersDao.getUsers()
        assertEquals(0, tableContentAfterDelete.size)
    }

    @Test
    fun deleteeOneUserDoesNotAffectRestOfTable() = runTest {
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
        usersDao.insert(user5)
        //
        val tableContent = usersDao.getUsers()
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
        //NOW deleting user 3
        val userToDelete = tableContent[2]
        val deleted = usersDao.delete(userToDelete)
        //
        assertEquals(1, deleted)
        val tableContentAfterDelete = usersDao.getUsers()
        assertEquals(4, tableContentAfterDelete.size)
        assertEquals(testUserName1, tableContentAfterDelete[0].name)
        assertEquals(testSelected1, tableContentAfterDelete[0].selected)
        assertEquals(testUserName2, tableContentAfterDelete[1].name)
        assertEquals(testSelected2, tableContentAfterDelete[1].selected)
        assertEquals(testUserName4, tableContentAfterDelete[2].name)
        assertEquals(testSelected4, tableContentAfterDelete[2].selected)
        assertEquals(testUserName5, tableContentAfterDelete[3].name)
        assertEquals(testSelected5, tableContentAfterDelete[3].selected)

    }


}
package fr.shining_cat.simplehiit.data.local.database.dao

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@OptIn(ExperimentalCoroutinesApi::class)
@HiltAndroidTest
class UsersJoinSessionsDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: SimpleHiitDatabase
    private lateinit var usersDao: UsersDao
    private lateinit var sessionsDao: SessionsDao

    @Before
    fun setup() {
        hiltRule.inject()
        usersDao = database.userDao()
        sessionsDao = database.sessionsDao()
    }

    @After
    fun tearDown() {
        database.close()
    }

////////////////

    private val testUserID = 1243L
    private val testUserID2 = 9876L
    private val testUserName = "test user name"
    private val testUserName2 = "test user name 2"
    private val testSelected = true
    private val testSessionDuration = 234L
    private val testSessionTimeStamp = 345L
    private val testSessionDuration2 = 678L
    private val testSessionTimeStamp2 = 901L
    private val testSessionDuration3 = 12345L
    private val testSessionTimeStamp3 = 23456L
    private val testSessionDuration4 = 345467L
    private val testSessionTimeStamp4 = 56789L
    private val testSessionDuration5 = 345467L
    private val testSessionTimeStamp5 = 56789L
    private val testSessionDuration6 = 345467L
    private val testSessionTimeStamp6 = 56789L
    private val testSessionDuration7 = 345467L
    private val testSessionTimeStamp7 = 56789L

    @Test
    fun cascadeSessionsDeletionWhenDeletingAUser() = runTest {
        //first insert users in users table
        val testUser1 =
            UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        val testUser2 =
            UserEntity(userId = testUserID2, name = testUserName2, selected = testSelected)
        launch {
            usersDao.insert(testUser1)
            usersDao.insert(testUser2)
        }
        advanceUntilIdle()
        //
        val testSession = SessionEntity(
            userId = testUserID,
            durationSeconds = testSessionDuration,
            timeStamp = testSessionTimeStamp
        )
        val testSession2 = SessionEntity(
            userId = testUserID,
            durationSeconds = testSessionDuration2,
            timeStamp = testSessionTimeStamp2
        )
        val testSession3 = SessionEntity(
            userId = testUserID,
            durationSeconds = testSessionDuration3,
            timeStamp = testSessionTimeStamp3
        )
        val testSession4 = SessionEntity(
            userId = testUserID,
            durationSeconds = testSessionDuration4,
            timeStamp = testSessionTimeStamp4
        )
        val testSession5 = SessionEntity(
            userId = testUserID2,
            durationSeconds = testSessionDuration5,
            timeStamp = testSessionTimeStamp5
        )
        val testSession6 = SessionEntity(
            userId = testUserID2,
            durationSeconds = testSessionDuration6,
            timeStamp = testSessionTimeStamp6
        )
        val testSession7 = SessionEntity(
            userId = testUserID2,
            durationSeconds = testSessionDuration7,
            timeStamp = testSessionTimeStamp7
        )
        launch {
            sessionsDao.insert(
                listOf(
                    testSession,
                    testSession2,
                    testSession3,
                    testSession4,
                    testSession5,
                    testSession6,
                    testSession7
                )
            )
        }
        advanceUntilIdle()
        //Assert that we have stored expected users in users table
        val usersFlowAsList = mutableListOf<List<UserEntity>>()
        val collectJob = launch(UnconfinedTestDispatcher()){
            usersDao.getUsers().toList(usersFlowAsList)
        }
        val usersTableContentBefore = usersFlowAsList[0]
        assertEquals(2, usersTableContentBefore.size)
        assertEquals(testUserID, usersTableContentBefore[0].userId)
        assertEquals(testUserName, usersTableContentBefore[0].name)
        assertEquals(testUserID2, usersTableContentBefore[1].userId)
        assertEquals(testUserName2, usersTableContentBefore[1].name)
        //assert that the expected user 1's sessions are stored in sessions table
        val sessionsForUser1 = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(4, sessionsForUser1.size)
        val retrievedSession1 = sessionsForUser1[0]
        assertEquals(testUserID, retrievedSession1.userId)
        assertEquals(testSessionDuration, retrievedSession1.durationSeconds)
        assertEquals(testSessionTimeStamp, retrievedSession1.timeStamp)
        val retrievedSession2 = sessionsForUser1[1]
        assertEquals(testUserID, retrievedSession2.userId)
        assertEquals(testSessionDuration2, retrievedSession2.durationSeconds)
        assertEquals(testSessionTimeStamp2, retrievedSession2.timeStamp)
        val retrievedSession3 = sessionsForUser1[2]
        assertEquals(testUserID, retrievedSession3.userId)
        assertEquals(testSessionDuration3, retrievedSession3.durationSeconds)
        assertEquals(testSessionTimeStamp3, retrievedSession3.timeStamp)
        val retrievedSession4 = sessionsForUser1[3]
        assertEquals(testUserID, retrievedSession4.userId)
        assertEquals(testSessionDuration4, retrievedSession4.durationSeconds)
        assertEquals(testSessionTimeStamp4, retrievedSession4.timeStamp)
        //same for user 2's sessions
        val sessionsForUser2 = sessionsDao.getSessionsForUser(userId = testUserID2)
        assertEquals(3, sessionsForUser2.size)
        val retrievedSession5 = sessionsForUser2[0]
        assertEquals(testUserID2, retrievedSession5.userId)
        assertEquals(testSessionDuration5, retrievedSession5.durationSeconds)
        assertEquals(testSessionTimeStamp5, retrievedSession5.timeStamp)
        val retrievedSession6 = sessionsForUser2[1]
        assertEquals(testUserID2, retrievedSession6.userId)
        assertEquals(testSessionDuration6, retrievedSession6.durationSeconds)
        assertEquals(testSessionTimeStamp6, retrievedSession6.timeStamp)
        val retrievedSession7 = sessionsForUser2[2]
        assertEquals(testUserID2, retrievedSession7.userId)
        assertEquals(testSessionDuration7, retrievedSession7.durationSeconds)
        assertEquals(testSessionTimeStamp7, retrievedSession7.timeStamp)
        //delete user 1 in users table
        var deleted = -1
        launch {deleted = usersDao.delete(testUser1)}
        advanceUntilIdle()
        assertEquals(1, deleted)
        //assert user 1 is not in users table anymore
        assertEquals(2, usersFlowAsList.size)
        val retrievedUsersAfterDeletion = usersFlowAsList[1]
        assertEquals(1, retrievedUsersAfterDeletion.size)
        assertEquals(testUser2, retrievedUsersAfterDeletion[0])
        //assert no sessions are found in sessions table for user 1
        val sessionsForUser1AfterDeletion = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(0, sessionsForUser1AfterDeletion.size)
        //assert sessions for user 2 are still all there
        val sessionsForUser2AfterDeletion = sessionsDao.getSessionsForUser(userId = testUserID2)
        assertEquals(3, sessionsForUser2AfterDeletion.size)
        //
        collectJob.cancel()
    }


}
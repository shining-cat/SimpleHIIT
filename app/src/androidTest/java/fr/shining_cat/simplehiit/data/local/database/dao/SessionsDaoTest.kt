package fr.shining_cat.simplehiit.data.local.database.dao

import android.database.sqlite.SQLiteConstraintException
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import fr.shining_cat.simplehiit.data.local.database.SimpleHiitDatabase
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
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
class SessionsDaoTest {

    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var database: SimpleHiitDatabase
    private lateinit var sessionsDao: SessionsDao
    private lateinit var usersDao: UsersDao

    @Before
    fun setup() {
        hiltRule.inject()
        sessionsDao = database.sessionsDao()
        usersDao = database.userDao()
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
    private val testSessionDate = 345L
    private val testSessionId = 567L
    private val testSessionDuration2 = 678L
    private val testSessionDate2 = 901L
    private val testSessionDuration3 = 12345L
    private val testSessionDate3 = 23456L
    private val testSessionDuration4 = 345467L
    private val testSessionDate4 = 56789L
    private val testSessionDuration5 = 345467L
    private val testSessionDate5 = 56789L
    private val testSessionDuration6 = 345467L
    private val testSessionDate6 = 56789L
    private val testSessionDuration7 = 345467L
    private val testSessionDate7 = 56789L

    @Test
    fun testInsertSession() = runTest {
        //first insert a user in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val testSession = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        sessionsDao.insert(listOf(testSession))
        //
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(1, tableContent.size)
        val retrievedSession = tableContent[0]
        assertEquals(testUserID, retrievedSession.userId)
        assertEquals(testSessionDuration, retrievedSession.durationMs)
        assertEquals(testSessionDate, retrievedSession.date)
    }

    @Test
    fun testInsertSessionWithId() = runTest {
        //first insert a user in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val testSession = SessionEntity(
            sessionId = testSessionId,
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        sessionsDao.insert(listOf(testSession))
        //
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(1, tableContent.size)
        val retrievedSession = tableContent[0]
        assertEquals(testSessionId, retrievedSession.sessionId)
        assertEquals(testUserID, retrievedSession.userId)
        assertEquals(testSessionDuration, retrievedSession.durationMs)
        assertEquals(testSessionDate, retrievedSession.date)
    }

    @Test
    fun testInsertSessionWithConflict() = runTest {
        //first insert a user in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        //inserting first session
        val testSession = SessionEntity(
            sessionId = testSessionId,
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        sessionsDao.insert(listOf(testSession))
        //inserting second session
        val testSession2 = SessionEntity(
            sessionId = testSessionId,
            userId = testUserID,
            durationMs = testSessionDuration2,
            date = testSessionDate2
        )
        sessionsDao.insert(listOf(testSession2))
        //
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(1, tableContent.size)
        val retrievedSession = tableContent[0]
        assertEquals(testSessionId, retrievedSession.sessionId)
        assertEquals(testUserID, retrievedSession.userId)
        assertEquals(testSessionDuration2, retrievedSession.durationMs)
        assertEquals(testSessionDate2, retrievedSession.date)
    }

    @Test(expected = SQLiteConstraintException::class)
    fun testInsertSessionWithUnknownUserId() = runTest {
        //first check there are no user with id in the users table
        val users = usersDao.getUsers()
        assertEquals(0, users.size)
        //try to insert session
        val testUserID = 1243L
        val testSessionDuration = 234L
        val testSessionDate = 345L
        val testSession = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        sessionsDao.insert(listOf(testSession))
    }

////////////////

    @Test
    fun getSessionsForUserEmptyTable() = runTest {
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID)
        assertTrue(tableContent.isEmpty())
    }

    @Test
    fun getSessionsForUserOnlyOne() = runTest {
        //first insert a user in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val testSession = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        sessionsDao.insert(listOf(testSession))
        //
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(1, tableContent.size)
        val retrievedSession = tableContent[0]
        assertEquals(testUserID, retrievedSession.userId)
        assertEquals(testSessionDuration, retrievedSession.durationMs)
        assertEquals(testSessionDate, retrievedSession.date)
    }

    @Test
    fun getSessionsForUserNotPresentList() = runTest {
        //first insert a user in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        //
        val testSession = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        val testSession2 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration2,
            date = testSessionDate2
        )
        val testSession3 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration3,
            date = testSessionDate3
        )
        val testSession4 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration4,
            date = testSessionDate4
        )
        sessionsDao.insert(listOf(testSession, testSession2, testSession3, testSession4))
        //
        val tableContent = sessionsDao.getSessionsForUser(userId = testUserID2)
        assertEquals(0, tableContent.size)
    }

    @Test
    fun getSessionsForUserList() = runTest {
        //first insert users in users table
        val user = UserEntity(userId = testUserID, name = testUserName, selected = testSelected)
        usersDao.insert(user)
        val user2 = UserEntity(userId = testUserID2, name = testUserName2, selected = testSelected)
        usersDao.insert(user2)
        //
        val testSession = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration,
            date = testSessionDate
        )
        val testSession2 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration2,
            date = testSessionDate2
        )
        val testSession3 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration3,
            date = testSessionDate3
        )
        val testSession4 = SessionEntity(
            userId = testUserID,
            durationMs = testSessionDuration4,
            date = testSessionDate4
        )
        val testSession5 = SessionEntity(
            userId = testUserID2,
            durationMs = testSessionDuration5,
            date = testSessionDate5
        )
        val testSession6 = SessionEntity(
            userId = testUserID2,
            durationMs = testSessionDuration6,
            date = testSessionDate6
        )
        val testSession7 = SessionEntity(
            userId = testUserID2,
            durationMs = testSessionDuration7,
            date = testSessionDate7
        )
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
        //assert that the expected sessions are stored in sessions table
        val sessionsForUser1 = sessionsDao.getSessionsForUser(userId = testUserID)
        assertEquals(4, sessionsForUser1.size)
        val retrievedSession1 = sessionsForUser1[0]
        assertEquals(testUserID, retrievedSession1.userId)
        assertEquals(testSessionDuration, retrievedSession1.durationMs)
        assertEquals(testSessionDate, retrievedSession1.date)
        val retrievedSession2 = sessionsForUser1[1]
        assertEquals(testUserID, retrievedSession2.userId)
        assertEquals(testSessionDuration2, retrievedSession2.durationMs)
        assertEquals(testSessionDate2, retrievedSession2.date)
        val retrievedSession3 = sessionsForUser1[2]
        assertEquals(testUserID, retrievedSession3.userId)
        assertEquals(testSessionDuration3, retrievedSession3.durationMs)
        assertEquals(testSessionDate3, retrievedSession3.date)
        val retrievedSession4 = sessionsForUser1[3]
        assertEquals(testUserID, retrievedSession4.userId)
        assertEquals(testSessionDuration4, retrievedSession4.durationMs)
        assertEquals(testSessionDate4, retrievedSession4.date)
        //
        val sessionsForUser2 = sessionsDao.getSessionsForUser(userId = testUserID2)
        assertEquals(3, sessionsForUser2.size)
        val retrievedSession5 = sessionsForUser2[0]
        assertEquals(testUserID2, retrievedSession5.userId)
        assertEquals(testSessionDuration5, retrievedSession5.durationMs)
        assertEquals(testSessionDate5, retrievedSession5.date)
        val retrievedSession6 = sessionsForUser2[1]
        assertEquals(testUserID2, retrievedSession6.userId)
        assertEquals(testSessionDuration6, retrievedSession6.durationMs)
        assertEquals(testSessionDate6, retrievedSession6.date)
        val retrievedSession7 = sessionsForUser2[2]
        assertEquals(testUserID2, retrievedSession7.userId)
        assertEquals(testSessionDuration7, retrievedSession7.durationMs)
        assertEquals(testSessionDate7, retrievedSession7.date)
    }

}
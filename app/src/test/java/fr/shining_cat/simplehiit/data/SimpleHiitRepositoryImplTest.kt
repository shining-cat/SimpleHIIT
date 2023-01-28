package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.Session
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitPreferences = mockk<SimpleHiitPreferences>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitPreferences = mockSimpleHiitPreferences,
        hiitLogger = mockHiitLogger
    )

//////////////
//   INSERT USER
    @Test
    fun `insert user returns success when dao insert succeeds`() = runTest {
        val insertedId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = insertedId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.insert(any()) } answers { insertedId }
        //
        val actual = simpleHiitRepository.insertUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.insert(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        val expectedOutput = Output.Success(result = insertedId)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `insert user returns error when dao insert throws exception`() = runTest {
        val insertedId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = insertedId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.insert(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.insertUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.insert(convertedUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed inserting user", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `insert user rethrows CancellationException when it gets thrown`() = runTest {
        val insertedId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = insertedId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.insert(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.insertUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.insert(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
    }

//////////////
//   GET USERS

    @ParameterizedTest(name = "{index} -> when DAO getusers returns {0} should return Success containing correct number of users")
    @MethodSource("getUsersArguments")
    fun `get users returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
        runTest {
            val convertedUser = User(id = 123L, name = "test user name")
            coEvery { mockUsersDao.getUsers() } answers { daoOutput }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { convertedUser }
            //
            val actual = simpleHiitRepository.getUsers()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any()) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
            assertTrue(actual is Output.Success)
            actual as Output.Success
            assertEquals(numberOfUsers, actual.result.size)
        }

    @Test
    fun `get users returns error when dao get users throws exception`() = runTest {
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.getUsers() } throws thrownException
        //
        val actual = simpleHiitRepository.getUsers()
        //
        coVerify(exactly = 1) { mockUsersDao.getUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting users", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `get users rethrows CancellationException when it gets thrown`() = runTest {
        coEvery { mockUsersDao.getUsers() } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getUsers()
        }
        //
        coVerify(exactly = 1) { mockUsersDao.getUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

//////////////
//   UPDATE USER
    @Test
    fun `update user returns success output when dao returns 1`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.update(any()) } returns 1
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        assertEquals(Output.Success(1), actual)
    }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("updateUserArguments")
    fun `update user returns correct output`(
        daoAnswer: Int,
        expectedOutput: Output.Error
    ) = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.update(any()) } answers { daoAnswer }
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user") }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `update user returns error when dao update users throws exception`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.update(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `update rethrows CancellationException when it gets thrown`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.update(any()) } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.updateUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

//////////////
//   DELETE USER

    @Test
    fun `delete user returns error when usersDao delete throws exception`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { userEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user rethrows CancellationException when usersDao delete throws CancellationException`() =
        runTest {
            val userId = 123L
            val userName = "test user name"
            val inputUser = User(id = userId, name = userName)
            val userEntity = UserEntity(userId = userId, name = userName)
            coEvery { mockUserMapper.convert(any<User>()) } answers { userEntity }
            coEvery { mockUsersDao.delete(any()) } throws mockk<CancellationException>()
            //
            assertThrows<CancellationException> {
                simpleHiitRepository.deleteUser(inputUser)
            }
            //
            coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
            coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("deleteUserArguments")
    fun `delete user returns error when usersDao delete fails`(
        daoAnswer: Int,
        expectedOutput: Output.Error
    ) = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { userEntity }
        coEvery { mockUsersDao.delete(any()) } returns daoAnswer
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed deleting user") }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `delete user returns success when usersDao delete succeeds`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { userEntity }
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(1, actual.result)
    }

//////////////
//   INSERT SESSION

    @Test
    fun `insert session returns error when users list is empty`() = runTest {
        val session = Session(date = 12345L, duration = 123L, usersIds = emptyList())
        //
        val actual = simpleHiitRepository.insertSession(session)
        //
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "insertSession::Error - no user provided") }
        coVerify(exactly = 0) { mockSessionsDao.insert(any()) }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(Constants.Errors.NO_USER_PROVIDED, actual.errorCode)
        assertEquals("No user provided when trying to insert session", actual.exception.message)
    }

    @Test
    fun `insert session rethrows CancellationException when it gets thrown`() = runTest {
        val sessionId = 1234L
        val date = 2345L
        val userId1 = 345L
        val duration = 123L
        val session = Session(date = date, duration = duration, usersIds = listOf(userId1))
        val sessionEntity = SessionEntity(sessionId = sessionId, date = date, durationMs = duration, userId = userId1)
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { listOf(sessionEntity) }
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsDao.insert(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.insertSession(session)
        }
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(session) }
        coVerify(exactly = 1) { mockSessionsDao.insert(listOf(sessionEntity)) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
    }

    @Test
    fun `insert user session error when dao insert throws exception`() = runTest {
        val sessionId = 1234L
        val date = 2345L
        val userId1 = 345L
        val duration = 123L
        val session = Session(date = date, duration = duration, usersIds = listOf(userId1))
        val sessionEntity = SessionEntity(sessionId = sessionId, date = date, durationMs = duration, userId = userId1)
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { listOf(sessionEntity) }
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsDao.insert(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.insertSession(session)
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(session) }
        coVerify(exactly = 1) { mockSessionsDao.insert(listOf(sessionEntity)) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed inserting session", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @ParameterizedTest(name = "{index} -> when inserting session {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("insertSessionArguments")
    fun `insert session behaves correctly in happy cases`(
        inputSession: Session,
        converterOutput : List<SessionEntity>,
        daoAnswer: List<Long>
    ) = runTest {
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { converterOutput }
        coEvery { mockSessionsDao.insert(any()) } answers {daoAnswer}
        //
        val actual = simpleHiitRepository.insertSession(inputSession)
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(inputSession) }
        val entityListSlot = slot<List<SessionEntity>>()
        coVerify(exactly = 1) { mockSessionsDao.insert(capture(entityListSlot))}
        assertEquals(converterOutput, entityListSlot.captured)
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(daoAnswer.size, actual.result)
    }

//////////////
//   GET SESSIONS FOR USER

    @Test
    fun `get sessions for user returns error when dao get sessions throws exception`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsDao.getSessionsForUser(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.getSessionsForUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = userId) }
        coVerify(exactly = 0) { mockSessionMapper.convert(any<SessionEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting sessions", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `get sessions for user rethrows CancellationException when it gets thrown`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        coEvery { mockSessionsDao.getSessionsForUser(any()) } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getSessionsForUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = userId) }
        coVerify(exactly = 0) { mockSessionMapper.convert(any<SessionEntity>()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    @ParameterizedTest(name = "{index} -> when getting sessions {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("getSessionArguments")
    fun `get sessions for user behaves correctly in happy cases`(
        daoAnswer: List<SessionEntity>
    ) = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        coEvery { mockSessionsDao.getSessionsForUser(any()) } answers {daoAnswer}
        val converterOutput = Session(date = 123L, duration = 234L, usersIds = listOf(345L))
        coEvery { mockSessionMapper.convert(any<SessionEntity>()) } answers { converterOutput }
        //
        val actual = simpleHiitRepository.getSessionsForUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = userId)}
        val numberOfSessionsResult = daoAnswer.size
        coVerify(exactly = numberOfSessionsResult) { mockSessionMapper.convert(any<SessionEntity>()) }
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(numberOfSessionsResult, actual.result.size)
    }


    ////////////////////////
    private companion object {

        @JvmStatic
        fun getUsersArguments() =
            Stream.of(
                Arguments.of(emptyList<UserEntity>()),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1")
                    )
                ),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1"),
                        UserEntity(userId = 1234L, name = "user test name 2"),
                        UserEntity(userId = 1235L, name = "user test name 3"),
                        UserEntity(userId = 1236L, name = "user test name 4"),
                        UserEntity(userId = 1237L, name = "user test name 5"),
                    )
                )
            )

        @JvmStatic
        fun updateUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user")
                    )
                ),
                Arguments.of(
                    7,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user")
                    )
                )

            )

        @JvmStatic
        fun deleteUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user")
                    )
                ),
                Arguments.of(
                    7,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user")
                    )
                )

            )

        @JvmStatic
        fun insertSessionArguments() =
            Stream.of(
                Arguments.of(
                    Session(date = 123L, duration = 234L, usersIds = listOf(345L)),
                    listOf(SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 345L)),
                    listOf(321L)
                ),
                Arguments.of(
                    Session(date = 123L, duration = 234L, usersIds = listOf(345L, 678L, 789L, 891L)),
                    listOf(
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 345L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 678L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 789L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 891L),
                    ),
                    listOf(321L, 543L, 654L, 765L)
                )

            )
        @JvmStatic
        fun getSessionArguments() =
            Stream.of(
                Arguments.of(
                    listOf(SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 345L))
                ),
                Arguments.of(
                    listOf(
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 345L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 678L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 789L),
                        SessionEntity(sessionId = 456L, date = 123L, durationMs = 234L, userId = 891L),
                    )
                )

            )

    }
}
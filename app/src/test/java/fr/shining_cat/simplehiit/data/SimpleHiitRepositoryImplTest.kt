package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsUsersLinkDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionsUsersLinkEntity
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockSessionsUsersLinkDao = mockk<SessionsUsersLinkDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        sessionsUsersLinkDao = mockSessionsUsersLinkDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
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
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
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
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
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
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockUsersDao.getUsers() }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

//////////////
//   UPDATE USER
    @Test
    fun `update user returns success output when dao returns 1`() = runTest {
        val insertedId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = insertedId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.update(any()) } answers {1}
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any()) }
        assertEquals(Output.Success(1), actual)
    }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("updateUserArguments")
    fun `update user returns correct output`(
        daoAnswer: Int,
        expectedOutput: Output.Error
    ) = runTest {
        val insertedId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = insertedId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        coEvery { mockUsersDao.update(any()) } answers { daoAnswer}
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any()) }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `update user returns error when dao update users throws exception`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.update(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.updateUser(inputUser)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_UPDATE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

//////////////
//   DELETE USER

    @Test
    fun `delete user returns error when sessionsUsersLinkDao getSessionsUsersLinksForUser throws exception`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.deleteByLinkId(any()) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user returns error when sessionsUsersLinkDao getSessionsUsersLinksForUser returns 1 link containing user and getSessionsUsersLinksForSession throws exception`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.deleteByLinkId(any()) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user returns error when sessionsUsersLinkDao getSessionsUsersLinksForUser and getSessionsUsersLinksForSession return both 1 link containing user and deleteByLinkId throws exception`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user returns success when sessionsUsersLinkDao getSessionsUsersLinksForUser and getSessionsUsersLinksForSession return both 1 link containing NON-matching user id proceeds but does not delete session`() = runTest {
        val userId = 123L
        val userId2 = 1234556L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUserLinkId2 = 67890L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinksForSessionEntity = SessionsUsersLinkEntity(userId = userId2, sessionId = sessionId, linkId = sessionUserLinkId2)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinksForSessionEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } answers {1}
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(convertedUserEntity) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
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
                    Output.Error(errorCode = Constants.Errors.DATABASE_UPDATE_FAILED, exception = Exception("failed updating user"))
                ),
                Arguments.of(
                    7,
                    Output.Error(errorCode = Constants.Errors.DATABASE_UPDATE_FAILED, exception = Exception("failed updating user"))
                )

            )
    }
}
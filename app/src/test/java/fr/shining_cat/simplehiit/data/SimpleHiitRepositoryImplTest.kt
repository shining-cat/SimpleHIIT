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

    @Test
    fun `get users rethrows CancellationException when it gets thrown`() = runTest {
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.getUsers() } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getUsers()
        }
        //
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockUsersDao.getUsers() }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
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

    @Test
    fun `update rethrows CancellationException when it gets thrown`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(name = userName)
        val convertedUserEntity = UserEntity(userId = userId, name = userName)
        coEvery { mockUserMapper.convert(any<User>()) } answers { convertedUserEntity }
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.update(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.updateUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.update(convertedUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
    }

//////////////
//   DELETE USER

    //sessionsUsersLinkDao.getSessionsUsersLinksForUser failures
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
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }
    @Test
    fun `delete user rethrows CancellationException when sessionsUsersLinkDao getSessionsUsersLinksForUser throws CancellationException`() = runTest {
        val userId = 123L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.deleteByLinkId(any()) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    //sessionsUsersLinkDao.getSessionsUsersLinksForSession failures
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
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user rethrows CancellationException when sessionsUsersLinkDao getSessionsUsersLinksForSession throws CancellationException`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 0) { mockSessionsUsersLinkDao.deleteByLinkId(any()) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    //linksForSession.size > 1
    @Test
    fun `delete user returns success when sessionsUsersLinkDao getSessionsUsersLinksForSession return more than 1 link for a linked session - only deletes user and link for user`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession bigger than 1 - delete user returns success if deleting user succeeds but deleting user session link fails`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 0
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed") }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession bigger than 1 - delete user returns success if deleting user succeeds but deleting user session link throws Exception`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed", thrownException) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession bigger than 1 - delete user rethrows CancellationException if deleting user session link throws CancellationException - does not delete user`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    @Test
    fun `linksForSession bigger than 1 - delete user returns error when usersDao delete fails`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 0
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error") }
        val expectedErrorCode = Constants.Errors.DATABASE_DELETE_FAILED
        val expectedExceptionMessage = "failed deleting user"
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedErrorCode, actual.errorCode)
        assertEquals(expectedExceptionMessage, actual.exception.message)
    }

    @Test
    fun `linksForSession bigger than 1 - delete user returns error when usersDao delete throws exception`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
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
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession bigger than 1 - delete user rethrows CancellationException when usersDao delete throws CancellationException`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
    }

    //linksForSession.size == 1 and linksForSession[0].userId != user.id
    @Test
    fun `delete user returns success when sessionsUsersLinkDao getSessionsUsersLinksForSession return 1 non-matching link for a linked session - only deletes user and link for user`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::Inconsistent data -> deleting ONLY found link for input user") }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user returns success if deleting user succeeds but deleting user session link fails`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity, returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 0
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed") }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user returns success if deleting user succeeds but deleting user session link throws Exception`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed", thrownException) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user rethrows CancellationException if deleting user session link throws CancellationException - does not delete user`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user returns error when usersDao delete fails`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 0
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error") }
        val expectedErrorCode = Constants.Errors.DATABASE_DELETE_FAILED
        val expectedExceptionMessage = "failed deleting user"
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedErrorCode, actual.errorCode)
        assertEquals(expectedExceptionMessage, actual.exception.message)
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user returns error when usersDao delete throws exception`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
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
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 but userIds do not match - delete user rethrows CancellationException when usersDao delete throws CancellationException`() = runTest {
        val userId = 123L
        val user2Id = 456L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val sessionUser2LinkId = 901L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        val returnedSessionsUsersLinkEntity2 = SessionsUsersLinkEntity(userId = user2Id, sessionId = sessionId, linkId = sessionUser2LinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity2)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
    }


    //linksForSession.size == 1 and linksForSession[0].userId == user.id
    @Test
    fun `delete user returns success when sessionsUsersLinkDao getSessionsUsersLinksForSession return 1 matching link for a linked session - deletes user, session, and link for user`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns success if deleting user succeeds but deleting user session link fails`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 0
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed") }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns success if deleting user succeeds but deleting user session link throws Exception`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::link deletion failed", thrownException) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns success if deleting user succeeds but deleting session fails`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockSessionsDao.delete(any()) } returns 0
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::session deletion failed") }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns success if deleting user succeeds but deleting session throws Exception`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsDao.delete(any()) } throws thrownException
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error::session deletion failed", thrownException) }
        val expectedOutput = Output.Success(1)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user rethrows CancellationException if deleting user session link throws CancellationException - does not delete user`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 0) { mockSessionsDao.delete(any()) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user rethrows CancellationException if deleting session throws CancellationException - does not delete user`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsDao.delete(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 0) { mockUserMapper.convert(any<User>()) }
        coVerify(exactly = 0) { mockUsersDao.delete(any()) }
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns error when usersDao delete fails`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        coEvery { mockUsersDao.delete(any()) } returns 0
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "deleteUser::Error") }
        val expectedErrorCode = Constants.Errors.DATABASE_DELETE_FAILED
        val expectedExceptionMessage = "failed deleting user"
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedErrorCode, actual.errorCode)
        assertEquals(expectedExceptionMessage, actual.exception.message)
    }

    @Test
    fun `linksForSession is 1 and userIds match - delete user returns error when usersDao delete throws exception`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(inputUser)
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
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
    fun `linksForSession is 1 and userIds match - delete user rethrows CancellationException when usersDao delete throws CancellationException`() = runTest {
        val userId = 123L
        val sessionId = 345L
        val sessionUserLinkId = 678L
        val userName = "test user name"
        val inputUser = User(id = userId, name = userName)
        val userEntity = UserEntity(userId = userId, name = userName)
        val returnedSessionsUsersLinkEntity = SessionsUsersLinkEntity(userId = userId, sessionId = sessionId, linkId = sessionUserLinkId)
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(any()) } answers { listOf(returnedSessionsUsersLinkEntity) }
        coEvery { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(any()) } answers {listOf(returnedSessionsUsersLinkEntity)}
        coEvery { mockSessionsUsersLinkDao.deleteByLinkId(any()) } returns 1
        coEvery { mockSessionsDao.delete(any()) } returns 1
        coEvery { mockUserMapper.convert(any<User>()) } answers {userEntity}
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.deleteUser(inputUser)
        }
        //
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForUser(userId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.getSessionsUsersLinksForSession(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockSessionsUsersLinkDao.deleteByLinkId(returnedSessionsUsersLinkEntity.linkId) }
        coVerify(exactly = 1) { mockSessionsDao.delete(returnedSessionsUsersLinkEntity.sessionId) }
        coVerify(exactly = 1) { mockUserMapper.convert(inputUser) }
        coVerify(exactly = 1) { mockUsersDao.delete(userEntity) }
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
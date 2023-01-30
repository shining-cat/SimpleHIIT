package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.preferences.SimpleHiitPreferences
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
internal class SimpleHiitRepositoryImplGetUsersTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitPreferences = mockk<SimpleHiitPreferences>()

    private val testUserId = 123L
    private val testUserName = "test user name"
    private val testIsSelected = true
    private val testUserModel = User(id = testUserId, name = testUserName, selected = testIsSelected)

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitPreferences = mockSimpleHiitPreferences,
        hiitLogger = mockHiitLogger
    )

//////////////
//   GET USERS

    @ParameterizedTest(name = "{index} -> when DAO getusers returns {0} should return Success containing correct number of users")
    @MethodSource("getUsersArguments")
    fun `get users returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
        runTest {
            coEvery { mockUsersDao.getUsers() } answers { daoOutput }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val actual = simpleHiitRepository.getUsers()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
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
//   GET SELECTED USERS

    @ParameterizedTest(name = "{index} -> when DAO getSelectedusers returns {0} should return Success containing correct number of users")
    @MethodSource("getSelectedUsersArguments")
    fun `get seleceted users returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
        runTest {
            coEvery { mockUsersDao.getSelectedUsers() } answers { daoOutput }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val actual = simpleHiitRepository.getSelectedUsers()
            //
            coVerify(exactly = 1) { mockUsersDao.getSelectedUsers() }
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
            assertTrue(actual is Output.Success)
            actual as Output.Success
            assertEquals(numberOfUsers, actual.result.size)
        }

    @Test
    fun `get selected users returns error when dao get users throws exception`() = runTest {
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.getSelectedUsers() } throws thrownException
        //
        val actual = simpleHiitRepository.getSelectedUsers()
        //
        coVerify(exactly = 1) { mockUsersDao.getSelectedUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting selected users", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `get selected users rethrows CancellationException when it gets thrown`() = runTest {
        coEvery { mockUsersDao.getSelectedUsers() } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getSelectedUsers()
        }
        //
        coVerify(exactly = 1) { mockUsersDao.getSelectedUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun getUsersArguments() =
            Stream.of(
                Arguments.of(emptyList<UserEntity>()),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true)
                    )
                ),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true),
                        UserEntity(userId = 1234L, name = "user test name 2", selected = true),
                        UserEntity(userId = 1235L, name = "user test name 3", selected = true),
                        UserEntity(userId = 1236L, name = "user test name 4", selected = true),
                        UserEntity(userId = 1237L, name = "user test name 5", selected = true)
                    )
                )
            )

        @JvmStatic
        fun getSelectedUsersArguments() =
            Stream.of(
                Arguments.of(emptyList<UserEntity>()),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true)
                    )
                ),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true),
                        UserEntity(userId = 1234L, name = "user test name 2", selected = true),
                        UserEntity(userId = 1235L, name = "user test name 3", selected = true),
                        UserEntity(userId = 1236L, name = "user test name 4", selected = true),
                        UserEntity(userId = 1237L, name = "user test name 5", selected = true)
                    )
                )
            )
    }
}
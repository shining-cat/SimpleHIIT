package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
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
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplInsertUserTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    private val testUserId = 123L
    private val testUserName = "test user name"
    private val testIsSelected = true
    private val testUserModel =
        User(id = testUserId, name = testUserName, selected = testIsSelected)
    private val testUserEntity =
        UserEntity(userId = testUserId, name = testUserName, selected = testIsSelected)

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitDataStoreManager = mockSimpleHiitDataStoreManager,
        hiitLogger = mockHiitLogger
    )

    //////////////
//   INSERT USER
    @Test
    fun `insert user returns success when dao insert succeeds`() = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.insert(any()) } answers { testUserId }
        //
        val actual = simpleHiitRepository.insertUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.insert(testUserEntity) }
        val expectedOutput = Output.Success(result = testUserId)
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `insert user returns error when dao insert throws exception`() = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.insert(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.insertUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.insert(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed inserting user", thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `insert user rethrows CancellationException when it gets thrown`() = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        val thrownException = mockk<CancellationException>()
        coEvery { mockUsersDao.insert(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.insertUser(testUserModel)
        }
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.insert(testUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
    }

}
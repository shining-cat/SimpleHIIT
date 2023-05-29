package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
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
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelAndJoin
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplInsertUserTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionRecordsDao = mockk<SessionRecordsDao>()
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

    //////////////
//   INSERT USER
    @Test
    fun `insert user returns success when dao insert succeeds`() = runTest {
        val simpleHiitRepository = SimpleHiitRepositoryImpl(
            usersDao = mockUsersDao,
            sessionRecordsDao = mockSessionRecordsDao,
            userMapper = mockUserMapper,
            sessionMapper = mockSessionMapper,
            hiitDataStoreManager = mockSimpleHiitDataStoreManager,
            hiitLogger = mockHiitLogger,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        //
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
    fun `insert user throws CancellationException when job is cancelled`() = runTest {
        val simpleHiitRepository = SimpleHiitRepositoryImpl(
            usersDao = mockUsersDao,
            sessionRecordsDao = mockSessionRecordsDao,
            userMapper = mockUserMapper,
            sessionMapper = mockSessionMapper,
            hiitDataStoreManager = mockSimpleHiitDataStoreManager,
            hiitLogger = mockHiitLogger,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        //
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.insert(any()) }  coAnswers {
            println("inserting delay in DAO call to allow for job cancellation before result is returned")
            delay(100L)
            testUserId
        }
        //
        val job = Job()
        launch(job) {
            assertThrows<CancellationException> {
                simpleHiitRepository.insertUser(testUserModel)
            }
        }
        delay(50L)
        println("canceling job")
        job.cancelAndJoin()
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.insert(testUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    @Test
    fun `insert user returns error when dao insert throws exception`() = runTest {
        val simpleHiitRepository = SimpleHiitRepositoryImpl(
            usersDao = mockUsersDao,
            sessionRecordsDao = mockSessionRecordsDao,
            userMapper = mockUserMapper,
            sessionMapper = mockSessionMapper,
            hiitDataStoreManager = mockSimpleHiitDataStoreManager,
            hiitLogger = mockHiitLogger,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        //
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
    fun `insert user catches rogue CancellationException`() = runTest {
        val simpleHiitRepository = SimpleHiitRepositoryImpl(
            usersDao = mockUsersDao,
            sessionRecordsDao = mockSessionRecordsDao,
            userMapper = mockUserMapper,
            sessionMapper = mockSessionMapper,
            hiitDataStoreManager = mockSimpleHiitDataStoreManager,
            hiitLogger = mockHiitLogger,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler)
        )
        //
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        val thrownException = CancellationException()
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

}
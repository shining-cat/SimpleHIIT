package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.models.User
import io.mockk.*
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplDeleteAllUsersTest : AbstractMockkTest() {

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
//   DELETE USER

    @Test
    fun `delete all users returns error when usersDao delete all users throws exception`() = runTest {
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
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.deleteAllUsers() } throws thrownException
        //
        assertThrows<Exception> {
            simpleHiitRepository.deleteAllUsers()
        }
        //
        coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
    }

    @Test
    fun `delete all users rethrows CancellationException when usersDao delete all users throws CancellationException`() =
        runTest {
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
            val thrownCancellationException = CancellationException()
            coEvery { mockUsersDao.deleteAllUsers() } throws thrownCancellationException
            //
            assertThrows<CancellationException> {
                simpleHiitRepository.deleteAllUsers()
            }
            //
            coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
            coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownCancellationException) }
        }

    @Test
    fun `delete all user returns nothing when usersDao delete all users succeeds`() = runTest {
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
        coEvery { mockUsersDao.deleteAllUsers() } just Runs
        //
        simpleHiitRepository.deleteAllUsers()
        //
        coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
    }

}
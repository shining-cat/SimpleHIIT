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
import io.mockk.*
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
internal class SimpleHiitRepositoryImplDeleteAllUsersTest : AbstractMockkTest() {

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
//   DELETE USER

    @Test
    fun `delete all users returns error when usersDao delete all users throws exception`() = runTest {
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
        coEvery { mockUsersDao.deleteAllUsers() } just Runs
        //
        simpleHiitRepository.deleteAllUsers()
        //
        coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
    }

}
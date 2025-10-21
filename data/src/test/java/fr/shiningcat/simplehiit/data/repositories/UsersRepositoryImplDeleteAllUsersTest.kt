package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

@OptIn(ExperimentalCoroutinesApi::class)
internal class UsersRepositoryImplDeleteAllUsersTest : AbstractMockkTest() {
    private val mockUsersDao = mockk<UsersDao>()
    private val mockUserMapper = mockk<UserMapper>()

// ////////////
//   DELETE USER

    @Test
    fun `delete all users returns error when usersDao delete all users throws exception`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val thrownException = Exception("this is a test exception")
            coEvery { mockUsersDao.deleteAllUsers() } throws thrownException
            //
            assertThrows<Exception> {
                usersRepository.deleteAllUsers()
            }
            //
            coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
            coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        }

    @Test
    fun `delete all user returns nothing when usersDao delete all users succeeds`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUsersDao.deleteAllUsers() } just Runs
            //
            usersRepository.deleteAllUsers()
            //
            coVerify(exactly = 1) { mockUsersDao.deleteAllUsers() }
        }
}

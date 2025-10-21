package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.database.dao.UsersDao
import fr.shiningcat.simplehiit.data.local.database.entities.UserEntity
import fr.shiningcat.simplehiit.data.mappers.UserMapper
import fr.shiningcat.simplehiit.domain.common.Constants
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class UsersRepositoryImplGetUsersAsListTest : AbstractMockkTest() {
    private val mockUsersDao = mockk<UsersDao>()
    private val mockUserMapper = mockk<UserMapper>()

// ////////////
//   GET USERS

    @ParameterizedTest(name = "{index} -> when DAO getusersList returns {0} should return Success containing correct number of users")
    @MethodSource("getUsersArguments")
    fun `get users as list returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUsersDao.getUsersList() } returns daoOutput
            val mappedUser = User(name = "user name test")
            coEvery { mockUserMapper.convert(any<UserEntity>()) } returns mappedUser
            //
            val output = usersRepository.getUsersList()
            //
            assertTrue(output is Output.Success)
            output as Output.Success
            assertEquals(daoOutput.size, output.result.size)
            for (input in daoOutput) {
                coVerify(exactly = 1) { mockUserMapper.convert(input) }
            }
        }

    @Test
    fun `get users as list returns error when dao get users throws exception`() =
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
            coEvery { mockUsersDao.getUsersList() } throws thrownException
            //
            val output = usersRepository.getUsersList()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsersList() }
            coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed getting users as List",
                    thrownException,
                )
            }
            assertTrue(output is Output.Error)
            output as Output.Error
            assertEquals(Constants.Errors.DATABASE_FETCH_FAILED, output.errorCode)
            assertEquals(thrownException, output.exception)
        }

    @Test
    fun `get users as list throws CancellationException when job is cancelled`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val mappedUser = User(name = "user name test")
            coEvery { mockUserMapper.convert(any<UserEntity>()) } returns mappedUser
            coEvery { mockUsersDao.getUsersList() } coAnswers {
                println("inserting delay in DAO call to allow for job cancellation before result is returned")
                delay(100L)
                listOf(
                    UserEntity(userId = 123L, name = "user test name 1", selected = true),
                )
            }
            //
            val job = Job()
            launch(job) {
                assertThrows<CancellationException> {
                    usersRepository.getUsersList()
                }
            }
            delay(50L)
            println("canceling job")
            job.cancelAndJoin()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsersList() }
            coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) } // this would happen after cancellation
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @Test
    fun `get users as list catches rogue CancellationException`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            val thrownException = CancellationException()
            coEvery { mockUsersDao.getUsersList() } throws thrownException
            //
            val actual = usersRepository.getUsersList()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsersList() }
            coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed getting users as List",
                    thrownException,
                )
            }
            assertTrue(actual is Output.Error)
            actual as Output.Error
            assertEquals(Constants.Errors.DATABASE_FETCH_FAILED, actual.errorCode)
            assertEquals(thrownException, actual.exception)
        }

// //////////////////////

    private companion object {
        @JvmStatic
        fun getUsersArguments() =
            Stream.of(
                Arguments.of(emptyList<UserEntity>()),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true),
                    ),
                ),
                Arguments.of(
                    listOf(
                        UserEntity(userId = 123L, name = "user test name 1", selected = true),
                        UserEntity(userId = 1234L, name = "user test name 2", selected = true),
                        UserEntity(userId = 1235L, name = "user test name 3", selected = false),
                        UserEntity(userId = 1236L, name = "user test name 4", selected = true),
                        UserEntity(userId = 1237L, name = "user test name 5", selected = false),
                    ),
                ),
            )
    }
}

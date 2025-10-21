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
internal class UsersRepositoryImplDeleteUserTest : AbstractMockkTest() {
    private val mockUsersDao = mockk<UsersDao>()
    private val mockUserMapper = mockk<UserMapper>()

    private val testUserId = 123L
    private val testUserName = "test user name"
    private val testIsSelected = true
    private val testUserModel =
        User(id = testUserId, name = testUserName, selected = testIsSelected)
    private val testUserEntity =
        UserEntity(userId = testUserId, name = testUserName, selected = testIsSelected)

// ////////////
//   DELETE USER

    @Test
    fun `delete user returns error when usersDao delete throws exception`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            val thrownException = Exception("this is a test exception")
            coEvery { mockUsersDao.delete(any()) } throws thrownException
            //
            val actual = usersRepository.deleteUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
            coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
            val expectedOutput =
                Output.Error(
                    errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    @Test
    fun `delete user throws CancellationException when job is cancelled`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            coEvery { mockUsersDao.delete(any()) } coAnswers {
                println("inserting delay in DAO call to allow for job cancellation before result is returned")
                delay(100L)
                2
            }
            //
            val job = Job()
            launch(job) {
                assertThrows<CancellationException> {
                    usersRepository.deleteUser(testUserModel)
                }
            }
            delay(50L)
            println("canceling job")
            job.cancelAndJoin()
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @Test
    fun `delete user catches rogue CancellationException`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            val thrownException = CancellationException()
            coEvery { mockUsersDao.delete(any()) } throws thrownException
            //
            val actual = usersRepository.deleteUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
            coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
            val expectedOutput =
                Output.Error(
                    errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("deleteUserArguments")
    fun `delete user returns error when usersDao delete fails`(
        daoAnswer: Int,
        expectedOutput: Output.Error,
    ) = runTest {
        val usersRepository =
            UsersRepositoryImpl(
                usersDao = mockUsersDao,
                userMapper = mockUserMapper,
                ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                hiitLogger = mockHiitLogger,
            )
        //
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.delete(any()) } returns daoAnswer
        //
        val actual = usersRepository.deleteUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed deleting user") }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `delete user returns success when usersDao delete succeeds`() =
        runTest {
            val usersRepository =
                UsersRepositoryImpl(
                    usersDao = mockUsersDao,
                    userMapper = mockUserMapper,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                    hiitLogger = mockHiitLogger,
                )
            //
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            coEvery { mockUsersDao.delete(any()) } returns 1
            //
            val actual = usersRepository.deleteUser(testUserModel)
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
            assertTrue(actual is Output.Success)
            actual as Output.Success
            assertEquals(1, actual.result)
        }

    // //////////////////////
    private companion object {
        @JvmStatic
        fun deleteUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user"),
                    ),
                ),
                Arguments.of(
                    7,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user"),
                    ),
                ),
            )
    }
}

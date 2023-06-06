package fr.shining_cat.simplehiit.data.repositories

import fr.shining_cat.simplehiit.domain.common.models.User
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.testutils.AbstractMockkTest
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
internal class SimpleHiitRepositoryImplUpdateUserTest : AbstractMockkTest() {

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
//   UPDATE USER
    @Test
    fun `update user returns success output when dao returns 1`() = runTest {
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
        coEvery { mockUsersDao.update(any()) } returns 1
        //
        val actual = simpleHiitRepository.updateUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        assertEquals(fr.shining_cat.simplehiit.domain.common.Output.Success(1), actual)
    }

    @Test
    fun `update user throws CancellationException when job is cancelled`() = runTest {
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
        coEvery { mockUsersDao.update(any()) }  coAnswers {
            println("inserting delay in DAO call to allow for job cancellation before result is returned")
            delay(100L)
            1
        }
        //
        val job = Job()
        launch(job){
            assertThrows<CancellationException> {
                simpleHiitRepository.updateUser(testUserModel)
            }
        }
        delay(50L)
        println("canceling job")
        job.cancelAndJoin()
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("updateUserArguments")
    fun `update user returns correct output`(
        daoAnswer: Int,
        expectedOutput: fr.shining_cat.simplehiit.domain.common.Output.Error
    ) = runTest {
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
        coEvery { mockUsersDao.update(any()) } answers { daoAnswer }
        //
        val actual = simpleHiitRepository.updateUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user") }
        assertTrue(actual is fr.shining_cat.simplehiit.domain.common.Output.Error)
        actual as fr.shining_cat.simplehiit.domain.common.Output.Error
        assertEquals(expectedOutput.errorCode, actual.errorCode)
        assertEquals(expectedOutput.exception.message, actual.exception.message)
    }

    @Test
    fun `update user returns error when dao update users throws exception`() = runTest {
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
        coEvery { mockUsersDao.update(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.updateUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user", thrownException) }
        val expectedOutput = fr.shining_cat.simplehiit.domain.common.Output.Error(
            errorCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.DATABASE_UPDATE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `update rethrows catches rogue CancellationException`() = runTest {
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
        coEvery { mockUsersDao.update(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.updateUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.update(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed updating user", thrownException) }
        val expectedOutput = fr.shining_cat.simplehiit.domain.common.Output.Error(
            errorCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.DATABASE_UPDATE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun updateUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    fr.shining_cat.simplehiit.domain.common.Output.Error(
                        errorCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user")
                    )
                ),
                Arguments.of(
                    7,
                    fr.shining_cat.simplehiit.domain.common.Output.Error(
                        errorCode = fr.shining_cat.simplehiit.domain.common.Constants.Errors.DATABASE_UPDATE_FAILED,
                        exception = Exception("failed updating user")
                    )
                )

            )

    }
}
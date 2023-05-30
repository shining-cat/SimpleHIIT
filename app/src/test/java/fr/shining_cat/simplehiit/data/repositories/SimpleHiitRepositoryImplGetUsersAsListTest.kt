package fr.shining_cat.simplehiit.data.repositories

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.UserEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.data.repositories.SimpleHiitRepositoryImpl
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
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplGetUsersAsListTest : AbstractMockkTest() {

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

//////////////
//   GET USERS

    @ParameterizedTest(name = "{index} -> when DAO getusersList returns {0} should return Success containing correct number of users")
    @MethodSource("getUsersArguments")
    fun `get users as list returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
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
            coEvery { mockUsersDao.getUsersList() } returns daoOutput
            val mappedUser = User(name = "user name test")
            coEvery { mockUserMapper.convert(any<UserEntity>()) } returns mappedUser
            //
            val output = simpleHiitRepository.getUsersList()
            //
            assertTrue(output is Output.Success)
            output as Output.Success
            assertEquals(daoOutput.size, output.result.size)
            for(input in daoOutput){
                coVerify (exactly = 1){ mockUserMapper.convert(input) }
            }
        }

    @Test
    fun `get users as list returns error when dao get users throws exception`() = runTest {
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
        coEvery { mockUsersDao.getUsersList() } throws thrownException
        //
        val output = simpleHiitRepository.getUsersList()
        //
        coVerify(exactly = 1) { mockUsersDao.getUsersList() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting users as List", thrownException) }
        assertTrue(output is Output.Error)
        output as Output.Error
        assertEquals(Constants.Errors.DATABASE_FETCH_FAILED, output.errorCode)
        assertEquals(thrownException, output.exception)
    }

    @Test
    fun `get users as list throws CancellationException when job is cancelled`() =
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
            val mappedUser = User(name = "user name test")
            coEvery { mockUserMapper.convert(any<UserEntity>()) } returns mappedUser
            coEvery { mockUsersDao.getUsersList() } coAnswers {
                println("inserting delay in DAO call to allow for job cancellation before result is returned")
                delay(100L)
                listOf(
                    UserEntity(userId = 123L, name = "user test name 1", selected = true)
                )
            }
            //
            val job = Job()
            launch(job){
                assertThrows<CancellationException> {
                    simpleHiitRepository.getUsersList()
                }
            }
            delay(50L)
            println("canceling job")
            job.cancelAndJoin()
            //
            coVerify(exactly = 1) { mockUsersDao.getUsersList() }
            coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) } //this would happen after cancellation
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }


    @Test
    fun `get users as list catches rogue CancellationException`() = runTest {
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
        val thrownException = CancellationException()
        coEvery { mockUsersDao.getUsersList() } throws thrownException
        //
        val actual = simpleHiitRepository.getUsersList()
        //
        coVerify(exactly = 1) { mockUsersDao.getUsersList() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting users as List", thrownException) }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(Constants.Errors.DATABASE_FETCH_FAILED, actual.errorCode)
        assertEquals(thrownException, actual.exception)
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
                        UserEntity(userId = 1235L, name = "user test name 3", selected = false),
                        UserEntity(userId = 1236L, name = "user test name 4", selected = true),
                        UserEntity(userId = 1237L, name = "user test name 5", selected = false)
                    )
                )
            )
    }

}
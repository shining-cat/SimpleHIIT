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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.toList
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
internal class SimpleHiitRepositoryImplGetUsersTest : AbstractMockkTest() {

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

    @ParameterizedTest(name = "{index} -> when DAO getusers returns {0} should return Success containing correct number of users")
    @MethodSource("getUsersArguments")
    fun `get users returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
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
            val daoFlow = MutableSharedFlow<List<UserEntity>>()
            coEvery { mockUsersDao.getUsers() } answers { daoFlow }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val usersFlowAsList = mutableListOf<Output<List<User>>>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                simpleHiitRepository.getUsers().toList(usersFlowAsList)
            }
            daoFlow.emit(daoOutput)
            //
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
            assertEquals(1, usersFlowAsList.size)
            val usersResult = usersFlowAsList[0]
            assertTrue(usersResult is Output.Success)
            usersResult as Output.Success
            assertEquals(numberOfUsers, usersResult.result.size)
            //
            collectJob.cancel()
        }

    @Test
    fun `get users returns success when dao get users succeeds and updates returned flow content`() =
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
            val daoOutput1 = emptyList<UserEntity>()
            val daoOutput2 =
                listOf(UserEntity(userId = 123L, name = "user test name 1", selected = true))
            val daoOutput3 = listOf(
                UserEntity(userId = 123L, name = "user test name 1", selected = true),
                UserEntity(userId = 1234L, name = "user test name 2", selected = true),
                UserEntity(userId = 1235L, name = "user test name 3", selected = true),
                UserEntity(userId = 1236L, name = "user test name 4", selected = true),
                UserEntity(userId = 1237L, name = "user test name 5", selected = true)
            )
            val daoFlow = MutableSharedFlow<List<UserEntity>>()
            coEvery { mockUsersDao.getUsers() } answers { daoFlow }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val usersFlowAsList = mutableListOf<Output<List<User>>>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                simpleHiitRepository.getUsers().toList(usersFlowAsList)
            }
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
            // first emission
            daoFlow.emit(daoOutput1)
            val numberOfUsers1 = daoOutput1.size
            assertEquals(1, usersFlowAsList.size)
            val usersResult1 = usersFlowAsList[0]
            assertTrue(usersResult1 is Output.Success)
            usersResult1 as Output.Success
            assertEquals(numberOfUsers1, usersResult1.result.size)
            // second emission
            daoFlow.emit(daoOutput2)
            val numberOfUsers2 = daoOutput2.size
            assertEquals(2, usersFlowAsList.size)
            val usersResult2 = usersFlowAsList[1]
            assertTrue(usersResult2 is Output.Success)
            usersResult2 as Output.Success
            assertEquals(numberOfUsers2, usersResult2.result.size)
            // third emission
            daoFlow.emit(daoOutput3)
            val numberOfUsers3 = daoOutput3.size
            assertEquals(3, usersFlowAsList.size)
            val usersResult3 = usersFlowAsList[2]
            assertTrue(usersResult3 is Output.Success)
            usersResult3 as Output.Success
            assertEquals(numberOfUsers3, usersResult3.result.size)
            //
            collectJob.cancel()
        }

    @Test
    fun `get users returns error when dao get users throws exception`() = runTest {
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
        coEvery { mockUsersDao.getUsers() } throws thrownException
        //
        val usersFlowAsList = mutableListOf<Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            simpleHiitRepository.getUsers().toList(usersFlowAsList)
        }
        //
        coVerify(exactly = 1) { mockUsersDao.getUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "failed getting users", thrownException) }
        assertEquals(1, usersFlowAsList.size)
        val result = usersFlowAsList[0]
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, result)
        collectJob.cancel()
    }

    @Test
    fun `get users rethrows CancellationException when it gets thrown`() = runTest {
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
    fun `get selected users returns success when dao get users succeeds`(daoOutput: List<UserEntity>) =
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
            val daoFlow = MutableSharedFlow<List<UserEntity>>()
            coEvery { mockUsersDao.getUsers() } answers { daoFlow }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val usersFlowAsList = mutableListOf<Output<List<User>>>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                simpleHiitRepository.getUsers().toList(usersFlowAsList)
            }
            daoFlow.emit(daoOutput)
            //
            coVerify(exactly = 1) { mockUsersDao.getUsers() }
            val numberOfUsers = daoOutput.size
            coVerify(exactly = numberOfUsers) { mockUserMapper.convert(any<UserEntity>()) }
            assertEquals(1, usersFlowAsList.size)
            val usersResult = usersFlowAsList[0]
            assertTrue(usersResult is Output.Success)
            usersResult as Output.Success
            assertEquals(numberOfUsers, usersResult.result.size)
            //
            collectJob.cancel()
        }

    @Test
    fun `get selected users returns success when dao get users succeeds and updates returned flow content`() =
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
            val daoOutput1 = emptyList<UserEntity>()
            val daoOutput2 =
                listOf(UserEntity(userId = 123L, name = "user test name 1", selected = true))
            val daoOutput3 = listOf(
                UserEntity(userId = 123L, name = "user test name 1", selected = true),
                UserEntity(userId = 1235L, name = "user test name 3", selected = true),
                UserEntity(userId = 1237L, name = "user test name 5", selected = true)
            )
            val daoFlow = MutableSharedFlow<List<UserEntity>>()
            coEvery { mockUsersDao.getSelectedUsers() } answers { daoFlow }
            coEvery { mockUserMapper.convert(any<UserEntity>()) } answers { testUserModel }
            //
            val usersFlowAsList = mutableListOf<Output<List<User>>>()
            val collectJob = launch(UnconfinedTestDispatcher()) {
                simpleHiitRepository.getSelectedUsers().toList(usersFlowAsList)
            }
            coVerify(exactly = 1) { mockUsersDao.getSelectedUsers() }
            // first emission
            daoFlow.emit(daoOutput1)
            val numberOfUsers1 = 0
            assertEquals(1, usersFlowAsList.size)
            val usersResult1 = usersFlowAsList[0]
            assertTrue(usersResult1 is Output.Success)
            usersResult1 as Output.Success
            assertEquals(numberOfUsers1, usersResult1.result.size)
            // second emission
            daoFlow.emit(daoOutput2)
            val numberOfSelectedUsers2 = daoOutput2.filter { it.selected }.size
            assertEquals(2, usersFlowAsList.size)
            val usersResult2 = usersFlowAsList[1]
            assertTrue(usersResult2 is Output.Success)
            usersResult2 as Output.Success
            assertEquals(numberOfSelectedUsers2, usersResult2.result.size)
            // third emission
            val numberOfSelectedUsers3 = daoOutput3.size
            daoFlow.emit(daoOutput3)
            assertEquals(3, usersFlowAsList.size)
            val usersResult3 = usersFlowAsList[2]
            assertTrue(usersResult3 is Output.Success)
            usersResult3 as Output.Success
            assertEquals(numberOfSelectedUsers3, usersResult3.result.size)
            //
            collectJob.cancel()
        }


    @Test
    fun `get selected users returns error when dao get users throws exception`() = runTest {
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
        coEvery { mockUsersDao.getSelectedUsers() } throws thrownException
        //
        val usersFlowAsList = mutableListOf<Output<List<User>>>()
        val collectJob = launch(UnconfinedTestDispatcher()) {
            simpleHiitRepository.getSelectedUsers().toList(usersFlowAsList)
        }
        //
        coVerify(exactly = 1) { mockUsersDao.getSelectedUsers() }
        coVerify(exactly = 0) { mockUserMapper.convert(any<UserEntity>()) }
        coVerify(exactly = 1) {
            mockHiitLogger.e(
                any(),
                "failed getting selected users",
                thrownException
            )
        }
        assertEquals(1, usersFlowAsList.size)
        val result = usersFlowAsList[0]
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, result)
        collectJob.cancel()
    }

    @Test
    fun `get selected users rethrows CancellationException when it gets thrown`() = runTest {
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
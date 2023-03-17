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
internal class SimpleHiitRepositoryImplDeleteUserTest : AbstractMockkTest() {

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

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionRecordsDao = mockSessionRecordsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitDataStoreManager = mockSimpleHiitDataStoreManager,
        hiitLogger = mockHiitLogger
    )

//////////////
//   DELETE USER

    @Test
    fun `delete user returns error when usersDao delete throws exception`() = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        val thrownException = Exception("this is a test exception")
        coEvery { mockUsersDao.delete(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete user rethrows CancellationException when usersDao delete throws CancellationException`() =
        runTest {
            coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
            coEvery { mockUsersDao.delete(any()) } throws mockk<CancellationException>()
            //
            assertThrows<CancellationException> {
                simpleHiitRepository.deleteUser(testUserModel)
            }
            //
            coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
            coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return error")
    @MethodSource("deleteUserArguments")
    fun `delete user returns error when usersDao delete fails`(
        daoAnswer: Int,
        expectedOutput: Output.Error
    ) = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.delete(any()) } returns daoAnswer
        //
        val actual = simpleHiitRepository.deleteUser(testUserModel)
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
    fun `delete user returns success when usersDao delete succeeds`() = runTest {
        coEvery { mockUserMapper.convert(any<User>()) } answers { testUserEntity }
        coEvery { mockUsersDao.delete(any()) } returns 1
        //
        val actual = simpleHiitRepository.deleteUser(testUserModel)
        //
        coVerify(exactly = 1) { mockUserMapper.convert(testUserModel) }
        coVerify(exactly = 1) { mockUsersDao.delete(testUserEntity) }
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(1, actual.result)
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun deleteUserArguments() =
            Stream.of(
                Arguments.of(
                    0,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user")
                    )
                ),
                Arguments.of(
                    7,
                    Output.Error(
                        errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
                        exception = Exception("failed deleting user")
                    )
                )

            )

    }
}
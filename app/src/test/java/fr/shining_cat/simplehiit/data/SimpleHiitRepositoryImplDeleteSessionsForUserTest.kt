package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
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
import org.junit.jupiter.params.provider.ValueSource

@OptIn(ExperimentalCoroutinesApi::class)
internal class SimpleHiitRepositoryImplDeleteSessionsForUserTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionRecordsDao = mockk<SessionRecordsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    private val testUserId = 123L

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionRecordsDao = mockSessionRecordsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitDataStoreManager = mockSimpleHiitDataStoreManager,
        hiitLogger = mockHiitLogger
    )

//////////////
//   DELETE SESSIONS FOR USER

    @Test
    fun `delete sessions for user returns error when sessionsDao delete throws exception`() = runTest {
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionRecordsDao.deleteForUser(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.deleteSessionRecordsForUser(testUserId)
        //
        coVerify(exactly = 1) { mockSessionRecordsDao.deleteForUser(testUserId) }
        coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_DELETE_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `delete sessions for user rethrows CancellationException when sessionsDao delete throws CancellationException`() =
        runTest {
            coEvery { mockSessionRecordsDao.deleteForUser(any()) } throws mockk<CancellationException>()
            //
            assertThrows<CancellationException> {
                simpleHiitRepository.deleteSessionRecordsForUser(testUserId)
            }
            //
            coVerify(exactly = 1) { mockSessionRecordsDao.deleteForUser(testUserId) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @ParameterizedTest(name = "{index} -> when DAO update user returns {0} should return success with delete count")
    @ValueSource(ints = [0, 1, 5, 9, 23, 64])
    fun `delete sessions for user returns success when sessionsDao delete succeeds`(
        testValue: Int
    ) = runTest {
        coEvery { mockSessionRecordsDao.deleteForUser(any()) } returns testValue
        //
        val actual = simpleHiitRepository.deleteSessionRecordsForUser(testUserId)
        //
        coVerify(exactly = 1) { mockSessionRecordsDao.deleteForUser(testUserId) }
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(testValue, actual.result)
    }
}
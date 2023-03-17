package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.SessionRecord
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
internal class SimpleHiitRepositoryImplGetSessionsTest : AbstractMockkTest() {

    private val mockUsersDao = mockk<UsersDao>()
    private val mockSessionRecordsDao = mockk<SessionRecordsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionRecordsDao = mockSessionRecordsDao,
        userMapper = mockUserMapper,
        sessionMapper = mockSessionMapper,
        hiitDataStoreManager = mockSimpleHiitDataStoreManager,
        hiitLogger = mockHiitLogger
    )

    private val testDate = 2345L
    private val testSessionUserId1 = 345L
    private val testSessionUserModel =
        User(id = testSessionUserId1, name = "test user name", selected = true)
    private val testDuration = 123L
    private val testSessionRecord =
        SessionRecord(timeStamp = testDate, durationMs = testDuration, usersIds = listOf(testSessionUserId1))

//////////////
//   GET SESSIONS FOR USER

    @Test
    fun `get sessions for user returns error when dao get sessions throws exception`() = runTest {
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionRecordsDao.getSessionsForUser(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.getSessionRecordsForUser(testSessionUserModel)
        //
        coVerify(exactly = 1) { mockSessionRecordsDao.getSessionsForUser(userId = testSessionUserId1) }
        coVerify(exactly = 0) { mockSessionMapper.convert(any<SessionEntity>()) }
        coVerify(exactly = 1) {
            mockHiitLogger.e(
                any(),
                "failed getting sessions",
                thrownException
            )
        }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_FETCH_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @Test
    fun `get sessions for user rethrows CancellationException when it gets thrown`() = runTest {
        coEvery { mockSessionRecordsDao.getSessionsForUser(any()) } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getSessionRecordsForUser(testSessionUserModel)
        }
        //
        coVerify(exactly = 1) { mockSessionRecordsDao.getSessionsForUser(userId = testSessionUserId1) }
        coVerify(exactly = 0) { mockSessionMapper.convert(any<SessionEntity>()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    @ParameterizedTest(name = "{index} -> when getting sessions {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("getSessionArguments")
    fun `get sessions for user behaves correctly in happy cases`(
        daoAnswer: List<SessionEntity>
    ) = runTest {
        coEvery { mockSessionRecordsDao.getSessionsForUser(any()) } answers { daoAnswer }
        coEvery { mockSessionMapper.convert(any<SessionEntity>()) } answers { testSessionRecord }
        //
        val actual = simpleHiitRepository.getSessionRecordsForUser(testSessionUserModel)
        //
        coVerify(exactly = 1) { mockSessionRecordsDao.getSessionsForUser(userId = testSessionUserId1) }
        val numberOfSessionsResult = daoAnswer.size
        coVerify(exactly = numberOfSessionsResult) { mockSessionMapper.convert(any<SessionEntity>()) }
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(numberOfSessionsResult, actual.result.size)
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun getSessionArguments() =
            Stream.of(
                Arguments.of(
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 345L
                        )
                    )
                ),
                Arguments.of(
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 345L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 678L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 789L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 891L
                        ),
                    )
                )

            )

    }
}
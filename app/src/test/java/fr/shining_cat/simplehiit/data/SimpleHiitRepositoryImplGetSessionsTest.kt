package fr.shining_cat.simplehiit.data

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.data.local.database.dao.SessionsDao
import fr.shining_cat.simplehiit.data.local.database.dao.UsersDao
import fr.shining_cat.simplehiit.data.local.database.entities.SessionEntity
import fr.shining_cat.simplehiit.data.local.datastore.SimpleHiitDataStoreManager
import fr.shining_cat.simplehiit.data.mappers.SessionMapper
import fr.shining_cat.simplehiit.data.mappers.UserMapper
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.models.Session
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
    private val mockSessionsDao = mockk<SessionsDao>()
    private val mockUserMapper = mockk<UserMapper>()
    private val mockSessionMapper = mockk<SessionMapper>()
    private val mockSimpleHiitDataStoreManager = mockk<SimpleHiitDataStoreManager>()

    private val simpleHiitRepository = SimpleHiitRepositoryImpl(
        usersDao = mockUsersDao,
        sessionsDao = mockSessionsDao,
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
    private val testSession =
        Session(date = testDate, duration = testDuration, usersIds = listOf(testSessionUserId1))

//////////////
//   GET SESSIONS FOR USER

    @Test
    fun `get sessions for user returns error when dao get sessions throws exception`() = runTest {
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsDao.getSessionsForUser(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.getSessionsForUser(testSessionUserModel)
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = testSessionUserId1) }
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
        coEvery { mockSessionsDao.getSessionsForUser(any()) } throws mockk<CancellationException>()
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.getSessionsForUser(testSessionUserModel)
        }
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = testSessionUserId1) }
        coVerify(exactly = 0) { mockSessionMapper.convert(any<SessionEntity>()) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
    }

    @ParameterizedTest(name = "{index} -> when getting sessions {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("getSessionArguments")
    fun `get sessions for user behaves correctly in happy cases`(
        daoAnswer: List<SessionEntity>
    ) = runTest {
        coEvery { mockSessionsDao.getSessionsForUser(any()) } answers { daoAnswer }
        coEvery { mockSessionMapper.convert(any<SessionEntity>()) } answers { testSession }
        //
        val actual = simpleHiitRepository.getSessionsForUser(testSessionUserModel)
        //
        coVerify(exactly = 1) { mockSessionsDao.getSessionsForUser(userId = testSessionUserId1) }
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
                            date = 123L,
                            durationMs = 234L,
                            userId = 345L
                        )
                    )
                ),
                Arguments.of(
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            date = 123L,
                            durationMs = 234L,
                            userId = 345L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            date = 123L,
                            durationMs = 234L,
                            userId = 678L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            date = 123L,
                            durationMs = 234L,
                            userId = 789L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            date = 123L,
                            durationMs = 234L,
                            userId = 891L
                        ),
                    )
                )

            )

    }
}
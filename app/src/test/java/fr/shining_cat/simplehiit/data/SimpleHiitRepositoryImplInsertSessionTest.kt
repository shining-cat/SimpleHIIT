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
import io.mockk.slot
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
internal class SimpleHiitRepositoryImplInsertSessionTest : AbstractMockkTest() {

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

//////////////
//   INSERT SESSION

    private val testSessionId = 1234L
    private val testDate = 2345L
    private val testSessionUserId1 = 345L
    private val testSessionUserModel =
        User(id = testSessionUserId1, name = "test user name", selected = true)
    private val testDuration = 123L
    private val testSession =
        Session(timeStamp = testDate, durationSeconds = testDuration, usersIds = listOf(testSessionUserId1))
    private val testSessionEntity = SessionEntity(
        sessionId = testSessionId,
        timeStamp = testDate,
        durationSeconds = testDuration,
        userId = testSessionUserId1
    )

    @Test
    fun `insert session returns error when users list is empty`() = runTest {
        val session = Session(timeStamp = testDate, durationSeconds = testDuration, usersIds = emptyList())
        //
        val actual = simpleHiitRepository.insertSession(session)
        //
        coVerify(exactly = 1) { mockHiitLogger.e(any(), "insertSession::Error - no user provided") }
        coVerify(exactly = 0) { mockSessionsDao.insert(any()) }
        assertTrue(actual is Output.Error)
        actual as Output.Error
        assertEquals(Constants.Errors.NO_USER_PROVIDED, actual.errorCode)
        assertEquals("No user provided when trying to insert session", actual.exception.message)
    }

    @Test
    fun `insert session rethrows CancellationException when it gets thrown`() = runTest {
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { listOf(testSessionEntity) }
        val thrownException = mockk<CancellationException>()
        coEvery { mockSessionsDao.insert(any()) } throws thrownException
        //
        assertThrows<CancellationException> {
            simpleHiitRepository.insertSession(testSession)
        }
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(testSession) }
        coVerify(exactly = 1) { mockSessionsDao.insert(listOf(testSessionEntity)) }
        coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), thrownException) }
    }

    @Test
    fun `insert user session error when dao insert throws exception`() = runTest {
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { listOf(testSessionEntity) }
        val thrownException = Exception("this is a test exception")
        coEvery { mockSessionsDao.insert(any()) } throws thrownException
        //
        val actual = simpleHiitRepository.insertSession(testSession)
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(testSession) }
        coVerify(exactly = 1) { mockSessionsDao.insert(listOf(testSessionEntity)) }
        coVerify(exactly = 1) {
            mockHiitLogger.e(
                any(),
                "failed inserting session",
                thrownException
            )
        }
        val expectedOutput = Output.Error(
            errorCode = Constants.Errors.DATABASE_INSERT_FAILED,
            exception = thrownException
        )
        assertEquals(expectedOutput, actual)
    }

    @ParameterizedTest(name = "{index} -> when inserting session {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("insertSessionArguments")
    fun `insert session behaves correctly in happy cases`(
        inputSession: Session,
        converterOutput: List<SessionEntity>,
        daoAnswer: List<Long>
    ) = runTest {
        coEvery { mockSessionMapper.convert(any<Session>()) } answers { converterOutput }
        coEvery { mockSessionsDao.insert(any()) } answers { daoAnswer }
        //
        val actual = simpleHiitRepository.insertSession(inputSession)
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(inputSession) }
        val entityListSlot = slot<List<SessionEntity>>()
        coVerify(exactly = 1) { mockSessionsDao.insert(capture(entityListSlot)) }
        assertEquals(converterOutput, entityListSlot.captured)
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(daoAnswer.size, actual.result)
    }

    ////////////////////////
    private companion object {

        @JvmStatic
        fun insertSessionArguments() =
            Stream.of(
                Arguments.of(
                    Session(timeStamp = 123L, durationSeconds = 234L, usersIds = listOf(345L)),
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationSeconds = 234L,
                            userId = 345L
                        )
                    ),
                    listOf(321L)
                ),
                Arguments.of(
                    Session(
                        timeStamp = 123L,
                        durationSeconds = 234L,
                        usersIds = listOf(345L, 678L, 789L, 891L)
                    ),
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationSeconds = 234L,
                            userId = 345L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationSeconds = 234L,
                            userId = 678L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationSeconds = 234L,
                            userId = 789L
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationSeconds = 234L,
                            userId = 891L
                        ),
                    ),
                    listOf(321L, 543L, 654L, 765L)
                )

            )

    }
}
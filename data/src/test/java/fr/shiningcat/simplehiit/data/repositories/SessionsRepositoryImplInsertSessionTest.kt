/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.data.repositories

import fr.shiningcat.simplehiit.data.local.database.dao.SessionRecordsDao
import fr.shiningcat.simplehiit.data.local.database.entities.SessionEntity
import fr.shiningcat.simplehiit.data.mappers.SessionMapper
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.SessionRecord
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import io.mockk.slot
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
internal class SessionsRepositoryImplInsertSessionTest : AbstractMockkTest() {
    private val mockSessionRecordsDao = mockk<SessionRecordsDao>()
    private val mockSessionMapper = mockk<SessionMapper>()

// ////////////
//   INSERT SESSION

    private val testSessionId = 1234L
    private val testDate = 2345L
    private val testSessionUserId1 = 345L
    private val testDuration = 123L
    private val testSessionRecord =
        SessionRecord(
            timeStamp = testDate,
            durationMs = testDuration,
            usersIds = listOf(testSessionUserId1),
        )
    private val testSessionEntity =
        SessionEntity(
            sessionId = testSessionId,
            timeStamp = testDate,
            durationMs = testDuration,
            userId = testSessionUserId1,
        )

    @Test
    fun `insert session returns error when users list is empty`() =
        runTest {
            val sessionsRepository =
                SessionsRepositoryImpl(
                    sessionsDao = mockSessionRecordsDao,
                    sessionMapper = mockSessionMapper,
                    hiitLogger = mockHiitLogger,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )
            //
            val sessionRecord =
                SessionRecord(
                    timeStamp = testDate,
                    durationMs = testDuration,
                    usersIds = emptyList(),
                )
            //
            val actual = sessionsRepository.insertSessionRecord(sessionRecord)
            //
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "insertSession::Error - no user provided",
                )
            }
            coVerify(exactly = 0) { mockSessionRecordsDao.insert(any()) }
            assertTrue(actual is Output.Error)
            actual as Output.Error
            assertEquals(DomainError.NO_USER_PROVIDED, actual.errorCode)
            assertEquals("No user provided when trying to insert session", actual.exception.message)
        }

    @Test
    fun `insert session throws CancellationException when job is cancelled`() =
        runTest {
            val sessionsRepository =
                SessionsRepositoryImpl(
                    sessionsDao = mockSessionRecordsDao,
                    sessionMapper = mockSessionMapper,
                    hiitLogger = mockHiitLogger,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )
            //
            coEvery { mockSessionMapper.convert(any<SessionRecord>()) } answers {
                listOf(
                    SessionEntity(
                        sessionId = 456L,
                        timeStamp = 123L,
                        durationMs = 234L,
                        userId = 345L,
                    ),
                )
            }
            coEvery { mockSessionRecordsDao.insert(any()) } coAnswers {
                println("inserting delay in DAO call to allow for job cancellation before result is returned")
                delay(100L)
                listOf(321L)
            }
            //
            val job = Job()
            launch(job) {
                assertThrows<CancellationException> {
                    sessionsRepository.insertSessionRecord(testSessionRecord)
                }
            }
            delay(50L)
            println("canceling job")
            job.cancelAndJoin()
            //
            coVerify(exactly = 1) { mockSessionMapper.convert(testSessionRecord) }
            val entityListSlot = slot<List<SessionEntity>>()
            coVerify(exactly = 1) { mockSessionRecordsDao.insert(capture(entityListSlot)) }
            coVerify(exactly = 0) { mockHiitLogger.e(any(), any(), any()) }
        }

    @Test
    fun `insert session catches rogue CancellationException`() =
        runTest {
            val sessionsRepository =
                SessionsRepositoryImpl(
                    sessionsDao = mockSessionRecordsDao,
                    sessionMapper = mockSessionMapper,
                    hiitLogger = mockHiitLogger,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )
            //
            coEvery { mockSessionMapper.convert(any<SessionRecord>()) } answers {
                listOf(
                    testSessionEntity,
                )
            }
            val thrownException = CancellationException()
            coEvery { mockSessionRecordsDao.insert(any()) } throws thrownException
            //
            val actual = sessionsRepository.insertSessionRecord(testSessionRecord)
            //
            coVerify(exactly = 1) { mockSessionMapper.convert(testSessionRecord) }
            coVerify(exactly = 1) { mockSessionRecordsDao.insert(listOf(testSessionEntity)) }
            coVerify(exactly = 1) { mockHiitLogger.e(any(), any(), thrownException) }
            val expectedOutput =
                Output.Error(
                    errorCode = DomainError.DATABASE_INSERT_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    @Test
    fun `insert user session error when dao insert throws exception`() =
        runTest {
            val sessionsRepository =
                SessionsRepositoryImpl(
                    sessionsDao = mockSessionRecordsDao,
                    sessionMapper = mockSessionMapper,
                    hiitLogger = mockHiitLogger,
                    ioDispatcher = UnconfinedTestDispatcher(testScheduler),
                )
            //
            coEvery { mockSessionMapper.convert(any<SessionRecord>()) } answers {
                listOf(
                    testSessionEntity,
                )
            }
            val thrownException = Exception("this is a test exception")
            coEvery { mockSessionRecordsDao.insert(any()) } throws thrownException
            //
            val actual = sessionsRepository.insertSessionRecord(testSessionRecord)
            //
            coVerify(exactly = 1) { mockSessionMapper.convert(testSessionRecord) }
            coVerify(exactly = 1) { mockSessionRecordsDao.insert(listOf(testSessionEntity)) }
            coVerify(exactly = 1) {
                mockHiitLogger.e(
                    any(),
                    "failed inserting session",
                    thrownException,
                )
            }
            val expectedOutput =
                Output.Error(
                    errorCode = DomainError.DATABASE_INSERT_FAILED,
                    exception = thrownException,
                )
            assertEquals(expectedOutput, actual)
        }

    @ParameterizedTest(name = "{index} -> when inserting session {0} should insert {1} through DAO and return dao insert result size")
    @MethodSource("insertSessionArguments")
    fun `insert session behaves correctly in happy cases`(
        inputSessionRecord: SessionRecord,
        converterOutput: List<SessionEntity>,
        daoAnswer: List<Long>,
    ) = runTest {
        val sessionsRepository =
            SessionsRepositoryImpl(
                sessionsDao = mockSessionRecordsDao,
                sessionMapper = mockSessionMapper,
                hiitLogger = mockHiitLogger,
                ioDispatcher = UnconfinedTestDispatcher(testScheduler),
            )
        //
        coEvery { mockSessionMapper.convert(any<SessionRecord>()) } answers { converterOutput }
        coEvery { mockSessionRecordsDao.insert(any()) } answers { daoAnswer }
        //
        val actual = sessionsRepository.insertSessionRecord(inputSessionRecord)
        //
        coVerify(exactly = 1) { mockSessionMapper.convert(inputSessionRecord) }
        val entityListSlot = slot<List<SessionEntity>>()
        coVerify(exactly = 1) { mockSessionRecordsDao.insert(capture(entityListSlot)) }
        assertEquals(converterOutput, entityListSlot.captured)
        assertTrue(actual is Output.Success)
        actual as Output.Success
        assertEquals(daoAnswer.size, actual.result)
    }

    // //////////////////////
    private companion object {
        @JvmStatic
        fun insertSessionArguments() =
            Stream.of(
                Arguments.of(
                    SessionRecord(timeStamp = 123L, durationMs = 234L, usersIds = listOf(345L)),
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 345L,
                        ),
                    ),
                    listOf(321L),
                ),
                Arguments.of(
                    SessionRecord(
                        timeStamp = 123L,
                        durationMs = 234L,
                        usersIds = listOf(345L, 678L, 789L, 891L),
                    ),
                    listOf(
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 345L,
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 678L,
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 789L,
                        ),
                        SessionEntity(
                            sessionId = 456L,
                            timeStamp = 123L,
                            durationMs = 234L,
                            userId = 891L,
                        ),
                    ),
                    listOf(321L, 543L, 654L, 765L),
                ),
            )
    }
}

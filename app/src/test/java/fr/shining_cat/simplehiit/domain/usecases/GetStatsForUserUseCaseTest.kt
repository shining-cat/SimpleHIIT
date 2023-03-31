package fr.shining_cat.simplehiit.domain.usecases

import fr.shining_cat.simplehiit.AbstractMockkTest
import fr.shining_cat.simplehiit.domain.Constants
import fr.shining_cat.simplehiit.domain.Output
import fr.shining_cat.simplehiit.domain.datainterfaces.SimpleHiitRepository
import fr.shining_cat.simplehiit.domain.models.SessionRecord
import fr.shining_cat.simplehiit.domain.models.User
import fr.shining_cat.simplehiit.domain.models.UserStatistics
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.util.stream.Stream
import kotlin.random.Random

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetStatsForUserUseCaseTest : AbstractMockkTest() {

    private val mockSimpleHiitRepository = mockk<SimpleHiitRepository>()
    private val mockCalculateCurrentStreakUseCase = mockk<CalculateCurrentStreakUseCase>()
    private val mockCalculateLongestStreakUseCase = mockk<CalculateLongestStreakUseCase>()
    private val mockCalculateAverageSessionsPerWeekUseCase = mockk<CalculateAverageSessionsPerWeekUseCase>()

    private val testNow = 12345L

    @Test
    fun `return error when repo call fails`() = runTest {
        val testedUseCase = GetStatsForUserUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            calculateCurrentStreakUseCase = mockCalculateCurrentStreakUseCase,
            calculateLongestStreakUseCase = mockCalculateLongestStreakUseCase,
            calculateAverageSessionsPerWeekUseCase = mockCalculateAverageSessionsPerWeekUseCase,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testException = Exception("this is a test exception")
        val testError = Output.Error(errorCode = Constants.Errors.DATABASE_FETCH_FAILED, exception = testException)
        coEvery { mockSimpleHiitRepository.getSessionRecordsForUser(any()) } answers { testError}
        //
        val result = testedUseCase.execute(testUser, testNow)
        //
        coVerify (exactly = 1){ mockSimpleHiitRepository.getSessionRecordsForUser(testUser) }
        assertEquals(testError, result)
    }

    @Test
    fun `return default empty value when repo returns empty list`() = runTest {
        val testedUseCase = GetStatsForUserUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            calculateCurrentStreakUseCase = mockCalculateCurrentStreakUseCase,
            calculateLongestStreakUseCase = mockCalculateLongestStreakUseCase,
            calculateAverageSessionsPerWeekUseCase = mockCalculateAverageSessionsPerWeekUseCase,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testSessionRecords = emptyList<SessionRecord>()
        coEvery { mockSimpleHiitRepository.getSessionRecordsForUser(any()) } answers { Output.Success(testSessionRecords)}
        coEvery { mockCalculateCurrentStreakUseCase.execute(any(), any()) } returns 0
        coEvery { mockCalculateLongestStreakUseCase.execute(any(), any()) } returns 0
        coEvery { mockCalculateAverageSessionsPerWeekUseCase.execute(any(), any()) } returns "0"
        //
        val output = testedUseCase.execute(testUser, testNow)
        //
        coVerify (exactly = 1){ mockSimpleHiitRepository.getSessionRecordsForUser(testUser) }
        coVerify(exactly = 0) { mockCalculateCurrentStreakUseCase.execute(any(), any()) }
        coVerify(exactly = 0) { mockCalculateLongestStreakUseCase.execute(any(), any()) }
        coVerify(exactly = 0) { mockCalculateAverageSessionsPerWeekUseCase.execute(any(), any()) }
        assertTrue(output is Output.Success)
        output as Output.Success
        assertEquals(UserStatistics(testUser), output.result)
    }

    @ParameterizedTest(name = "{index} -> when called should return UserStatistics with {2} totalNumberOfSessions, {3} cumulatedTimeOfExerciseSeconds, {4} averageSessionLengthSeconds")
    @MethodSource("sessionsArguments")
    fun `return correct value when repo call succeeds`(
        testSessionRecords: List<SessionRecord>,
        expectedNumberOfCallsSubUseCases: Int,
        expectedTotalNumberOfSessions: Int,
        expectedCumulatedTimeOfExerciseSeconds: Long,
        expectedAverageSessionLengthSeconds: Long
    ) = runTest {
        val testedUseCase = GetStatsForUserUseCase(
            simpleHiitRepository = mockSimpleHiitRepository,
            calculateCurrentStreakUseCase = mockCalculateCurrentStreakUseCase,
            calculateLongestStreakUseCase = mockCalculateLongestStreakUseCase,
            calculateAverageSessionsPerWeekUseCase = mockCalculateAverageSessionsPerWeekUseCase,
            ioDispatcher = UnconfinedTestDispatcher(testScheduler),
            simpleHiitLogger = mockHiitLogger
        )
        val testCurrentStreak = Random.nextInt(10,5000)
        val testLongestStreak = Random.nextInt(5000,10000)
        val testAverageWeek = (Random.nextInt(1,100).toDouble() / 100.toDouble()).toString()
        coEvery { mockSimpleHiitRepository.getSessionRecordsForUser(any()) } answers { Output.Success(testSessionRecords)}
        coEvery { mockCalculateCurrentStreakUseCase.execute(any(), any()) } returns testCurrentStreak
        coEvery { mockCalculateLongestStreakUseCase.execute(any(), any()) } returns testLongestStreak
        coEvery { mockCalculateAverageSessionsPerWeekUseCase.execute(any(), any()) } returns testAverageWeek
        //
        val output = testedUseCase.execute(testUser, testNow)
        //
        coVerify (exactly = 1){ mockSimpleHiitRepository.getSessionRecordsForUser(testUser) }
        coVerify(exactly = expectedNumberOfCallsSubUseCases) { mockCalculateCurrentStreakUseCase.execute(any(), any()) }
        coVerify(exactly = expectedNumberOfCallsSubUseCases) { mockCalculateLongestStreakUseCase.execute(any(), any()) }
        coVerify(exactly = expectedNumberOfCallsSubUseCases) { mockCalculateAverageSessionsPerWeekUseCase.execute(any(), any()) }
        assertTrue(output is Output.Success)
        output as Output.Success
        assertEquals(testUser, output.result.user)
        assertEquals(expectedTotalNumberOfSessions, output.result.totalNumberOfSessions)
        assertEquals(expectedCumulatedTimeOfExerciseSeconds, output.result.cumulatedTimeOfExerciseMs)
        assertEquals(expectedAverageSessionLengthSeconds, output.result.averageSessionLengthMs)
        assertEquals(testLongestStreak, output.result.longestStreakDays)
        assertEquals(testCurrentStreak, output.result.currentStreakDays)
        assertEquals(testAverageWeek, output.result.averageNumberOfSessionsPerWeek)
    }

    ////////////////////////
    private companion object {

        val testUser = User(id= 123L, name = "test user name", selected = true)

        @JvmStatic
        fun sessionsArguments() =
            Stream.of(
                Arguments.of(
                    listOf(
                        SessionRecord(
                            id = 123L,
                            timeStamp = 1234L,
                            durationMs = 12345L,
                            usersIds = listOf(testUser.id)
                        )
                    ),
                    1,
                    1,
                    12345L,
                    12345L
                ),
                Arguments.of(
                    listOf(
                        SessionRecord(
                            id = 123L,
                            timeStamp = 1234L,
                            durationMs = 1500L,
                            usersIds = listOf(testUser.id)
                        ),
                        SessionRecord(
                            id = 234L,
                            timeStamp = 2345L,
                            durationMs = 2700L,
                            usersIds = listOf(testUser.id)
                        ),
                        SessionRecord(
                            id = 345L,
                            timeStamp = 3456L,
                            durationMs = 1800L,
                            usersIds = listOf(testUser.id)
                        ),
                        SessionRecord(
                            id = 456L,
                            timeStamp = 4567L,
                            durationMs = 3000L,
                            usersIds = listOf(testUser.id)
                        ),
                    ),
                    1,
                    4,
                    9000L,
                    2250L
                ),
                Arguments.of(
                    listOf(
                        SessionRecord(
                            id = 123L,
                            timeStamp = 1234L,
                            durationMs = 1500L,
                            usersIds = listOf(testUser.id)
                        ),
                        SessionRecord(
                            id = 345L,
                            timeStamp = 3456L,
                            durationMs = 1500L,
                            usersIds = listOf(testUser.id)
                        ),
                        SessionRecord(
                            id = 456L,
                            timeStamp = 4567L,
                            durationMs = 1500L,
                            usersIds = listOf(testUser.id)
                        ),
                    ),
                    1,
                    3,
                    4500L,
                    1500L
                ),

            )

    }


}
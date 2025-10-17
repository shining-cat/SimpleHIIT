package fr.shiningcat.simplehiit.android.mobile.ui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.domain.common.usecases.ResetWholeAppUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.DeleteSessionsForUserUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetAllUsersUseCase
import fr.shiningcat.simplehiit.domain.statistics.usecases.GetStatsForUserUseCase
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.Runs
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsInteractorImplTest : AbstractMockkTest() {
    private val mockGetAllUsersUseCase = mockk<GetAllUsersUseCase>()
    private val mockGetStatsForUserUseCase = mockk<GetStatsForUserUseCase>()
    private val mockDeleteSessionsForUserUseCase = mockk<DeleteSessionsForUserUseCase>()
    private val mockResetWholeAppUseCase = mockk<ResetWholeAppUseCase>()

    private val allUsersFlow = MutableSharedFlow<Output<NonEmptyList<User>>>()
    private val testUser = User(name = "test user name")
    private val mockUserStats = mockk<UserStatistics>()
    private val testReturnInt = 123

    private val testedInteractor =
        StatisticsInteractorImpl(
            mockGetAllUsersUseCase,
            mockGetStatsForUserUseCase,
            mockDeleteSessionsForUserUseCase,
            mockResetWholeAppUseCase,
        )

    @BeforeEach
    fun setUpMock() {
        coEvery { mockGetAllUsersUseCase.execute() } answers { allUsersFlow }
        coEvery { mockGetStatsForUserUseCase.execute(any(), any()) } returns
            Output.Success(
                mockUserStats,
            )
        coEvery { mockDeleteSessionsForUserUseCase.execute(any()) } returns
            Output.Success(
                testReturnInt,
            )
        coEvery { mockResetWholeAppUseCase.execute() } just Runs
    }

    @Test
    fun `calls on interactor getAllUsers calls GetAllUsersUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.getAllUsers()
            coVerify(exactly = 1) { mockGetAllUsersUseCase.execute() }
            assertEquals(allUsersFlow, result)
        }

    @Test
    fun `calls on interactor getStatsForUser calls GetStatsForUserUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            val result = testedInteractor.getStatsForUser(testUser, 123L)
            coVerify(exactly = 1) { mockGetStatsForUserUseCase.execute(testUser, 123L) }
            assertTrue(result is Output.Success)
            result as Output.Success
            assertEquals(mockUserStats, result.result)
        }

    @Test
    fun `calls on interactor deleteSessionsForUser calls DeleteSessionsForUserUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.deleteSessionsForUser(123L)
            coVerify(exactly = 1) { mockDeleteSessionsForUserUseCase.execute(123L) }
        }

    @Test
    fun `calls on interactor resetWholeApp calls ResetWholeAppUseCase`() =
        runTest(UnconfinedTestDispatcher()) {
            testedInteractor.resetWholeApp()
            coVerify(exactly = 1) { mockResetWholeAppUseCase.execute() }
        }
}

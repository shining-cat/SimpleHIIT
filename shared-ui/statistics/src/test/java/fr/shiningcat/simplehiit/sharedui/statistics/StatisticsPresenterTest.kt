/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.commonutils.TimeProvider
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DisplayedStatistic
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.domain.common.models.UserStatistics
import fr.shiningcat.simplehiit.testutils.AbstractMockkTest
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterTest : AbstractMockkTest() {
    private val mockStatisticsInteractor = mockk<StatisticsInteractor>()
    private val mockMapper = mockk<StatisticsViewStateMapper>()
    private val mockTimeProvider = mockk<TimeProvider>()

    private val usersFlow = MutableStateFlow<Output<NonEmptyList<User>>>(Output.Success(testUsers()))

    private val testUser1 = User(id = 1L, name = "User One", selected = true)
    private val testUser2 = User(id = 2L, name = "User Two", selected = false)

    private fun testUsers() = NonEmptyList(testUser1, listOf(testUser2))

    private fun testUserStatistics() =
        UserStatistics(
            user = testUser1,
            totalNumberOfSessions = 10,
            cumulatedTimeOfExerciseMs = 9000000L,
            averageSessionLengthMs = 900000L,
            longestStreakDays = 7,
            currentStreakDays = 3,
            averageNumberOfSessionsPerWeek = "2.5",
        )

    private fun testNominalViewState() =
        StatisticsViewState.Nominal(
            user = testUser1,
            statistics = emptyList<DisplayedStatistic>(),
            showUsersSwitch = true,
        )

    private lateinit var testedPresenter: StatisticsPresenter

    @BeforeEach
    fun setUp() {
        every { mockStatisticsInteractor.getAllUsers() } returns usersFlow
        every { mockTimeProvider.getCurrentTimeMillis() } returns 123456789L

        testedPresenter =
            StatisticsPresenter(
                statisticsInteractor = mockStatisticsInteractor,
                mapper = mockMapper,
                timeProvider = mockTimeProvider,
                logger = mockHiitLogger,
            )
    }

    @Test
    fun `screenViewState initially emits Loading`() =
        runTest {
            val state = testedPresenter.screenViewState.first()
            assertEquals(StatisticsViewState.Loading, state)
        }

    @Test
    fun `dialogState initially emits None`() =
        runTest {
            val state = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, state)
        }

    @Test
    fun `observeUsers with success retrieves stats for first user`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())

            testedPresenter.observeUsers().take(1).toList()

            coVerify { mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L) }
            val state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Nominal)
        }

    @Test
    fun `observeUsers with error emits error state`() =
        runTest {
            val errorOutput = Output.Error(DomainError.NO_USERS_FOUND, Exception("Test"))
            val errorViewState = StatisticsViewState.NoUsers
            every { mockMapper.mapUsersError(DomainError.NO_USERS_FOUND) } returns errorViewState

            usersFlow.value = errorOutput

            testedPresenter.observeUsers().take(1).toList()

            val state = testedPresenter.screenViewState.first()
            assertEquals(StatisticsViewState.NoUsers, state)
        }

    @Test
    fun `retrieveStatsForUser with single user shows nominal state without users switch`() =
        runTest {
            val singleUserList = NonEmptyList(testUser1, emptyList())
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(false, userStats) } returns testNominalViewState().copy(showUsersSwitch = false)

            usersFlow.value = Output.Success(singleUserList)
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.retrieveStatsForUser(testUser1)

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Nominal)
            assertEquals(false, (state as StatisticsViewState.Nominal).showUsersSwitch)
        }

    @Test
    fun `retrieveStatsForUser with multiple users shows nominal state with users switch`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.retrieveStatsForUser(testUser1)

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Nominal)
            assertEquals(true, (state as StatisticsViewState.Nominal).showUsersSwitch)
        }

    @Test
    fun `retrieveStatsForUser with error emits error state`() =
        runTest {
            val errorOutput = Output.Error(DomainError.DATABASE_FETCH_FAILED, Exception("Test"))
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns errorOutput

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.retrieveStatsForUser(testUser1)

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Error)
            assertEquals(DomainError.DATABASE_FETCH_FAILED.code, (state as StatisticsViewState.Error).errorCode)
            assertEquals(testUser1, state.user)
            assertEquals(true, state.showUsersSwitch)
        }

    @Test
    fun `retrieveStatsForUser dismisses dialog`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.openPickUser()
            testedPresenter.retrieveStatsForUser(testUser1)

            val dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, dialogState)
        }

    @Test
    fun `openPickUser with multiple users emits SelectUser dialog`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.openPickUser()

            val dialogState = testedPresenter.dialogState.first()
            assertTrue(dialogState is StatisticsDialog.SelectUser)
            assertEquals(testUsers().toList(), (dialogState as StatisticsDialog.SelectUser).users)
        }

    @Test
    fun `openPickUser with single user does not emit dialog`() =
        runTest {
            val singleUserList = NonEmptyList(testUser1, emptyList())
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(false, userStats) } returns testNominalViewState().copy(showUsersSwitch = false)

            usersFlow.value = Output.Success(singleUserList)
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.openPickUser()

            val dialogState = testedPresenter.dialogState.first()
            // Correct behavior: Presenter guards against invalid call (defense in depth)
            // UI already hides button when showUsersSwitch == false, but presenter validates too
            assertEquals(StatisticsDialog.None, dialogState)
        }

    @Test
    fun `deleteAllSessionsForUser emits confirmation dialog`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.deleteAllSessionsForUser(testUser1)

            val dialogState = testedPresenter.dialogState.first()
            assertTrue(dialogState is StatisticsDialog.ConfirmDeleteAllSessionsForUser)
            assertEquals(testUser1, (dialogState as StatisticsDialog.ConfirmDeleteAllSessionsForUser).user)
        }

    @Test
    fun `deleteAllSessionsForUserConfirmation calls interactor and refreshes stats`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery { mockStatisticsInteractor.deleteSessionsForUser(testUser1.id) } returns Unit
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.deleteAllSessionsForUserConfirmation(testUser1)

            coVerify { mockStatisticsInteractor.deleteSessionsForUser(testUser1.id) }
            coVerify { mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L) }

            val dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, dialogState)
        }

    @Test
    fun `resetWholeApp emits confirmation dialog`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.resetWholeApp()

            val dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.ConfirmWholeReset, dialogState)
        }

    @Test
    fun `resetWholeAppConfirmation calls interactor`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery { mockStatisticsInteractor.resetWholeApp() } returns Unit
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.resetWholeAppConfirmation()

            coVerify { mockStatisticsInteractor.resetWholeApp() }
        }

    @Test
    fun `cancelDialog emits None dialog state`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            testedPresenter.openPickUser()
            testedPresenter.cancelDialog()

            val dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, dialogState)
        }

    @Test
    fun `observeUsers can be collected multiple times`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())

            // Collect first time
            testedPresenter.observeUsers().take(1).toList()

            // Collect second time to verify it can be re-collected
            testedPresenter.observeUsers().take(1).toList()

            // Verify the interactor was called twice (once per collection)
            coVerify(exactly = 2) { mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L) }
        }

    @Test
    fun `state updates correctly through sequence of operations`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(any(), any())
            } returns Output.Success(userStats)
            every { mockMapper.map(any(), any()) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            // Initially shows stats for first user
            var state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Nominal)

            // Open user picker
            testedPresenter.openPickUser()
            var dialogState = testedPresenter.dialogState.first()
            assertTrue(dialogState is StatisticsDialog.SelectUser)

            // Select different user
            testedPresenter.retrieveStatsForUser(testUser2)
            dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, dialogState)

            // Verify stats were retrieved for new user
            coVerify { mockStatisticsInteractor.getStatsForUser(testUser2, 123456789L) }
        }

    @Test
    fun `dialog state flow works correctly through deletion sequence`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery { mockStatisticsInteractor.deleteSessionsForUser(any()) } returns Unit
            coEvery {
                mockStatisticsInteractor.getStatsForUser(any(), any())
            } returns Output.Success(userStats)
            every { mockMapper.map(any(), any()) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            // Initially None
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            // Show delete confirmation
            testedPresenter.deleteAllSessionsForUser(testUser1)
            assertTrue(testedPresenter.dialogState.first() is StatisticsDialog.ConfirmDeleteAllSessionsForUser)

            // Confirm deletion
            testedPresenter.deleteAllSessionsForUserConfirmation(testUser1)
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())
        }

    @Test
    fun `dialog state flow works correctly through reset sequence`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery { mockStatisticsInteractor.resetWholeApp() } returns Unit
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())
            testedPresenter.observeUsers().take(1).toList()

            // Initially None
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            // Show reset confirmation
            testedPresenter.resetWholeApp()
            assertEquals(StatisticsDialog.ConfirmWholeReset, testedPresenter.dialogState.first())

            // Cancel
            testedPresenter.cancelDialog()
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            // Show again and confirm
            testedPresenter.resetWholeApp()
            testedPresenter.resetWholeAppConfirmation()
            coVerify { mockStatisticsInteractor.resetWholeApp() }
        }
}

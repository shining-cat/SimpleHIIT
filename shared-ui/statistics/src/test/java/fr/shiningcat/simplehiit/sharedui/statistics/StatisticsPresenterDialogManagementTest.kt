/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.domain.common.Output
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Dialog management tests for StatisticsPresenter.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterDialogManagementTest : StatisticsPresenterTestBase() {
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

            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            testedPresenter.deleteAllSessionsForUser(testUser1)
            assertTrue(testedPresenter.dialogState.first() is StatisticsDialog.ConfirmDeleteAllSessionsForUser)

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

            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            testedPresenter.resetWholeApp()
            assertEquals(StatisticsDialog.ConfirmWholeReset, testedPresenter.dialogState.first())

            testedPresenter.cancelDialog()
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())

            testedPresenter.resetWholeApp()
            testedPresenter.resetWholeAppConfirmation()
            coVerify { mockStatisticsInteractor.resetWholeApp() }
        }
}

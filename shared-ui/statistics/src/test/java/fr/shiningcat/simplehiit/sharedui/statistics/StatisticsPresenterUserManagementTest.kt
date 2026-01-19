/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
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
 * User management and selection tests for StatisticsPresenter.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterUserManagementTest : StatisticsPresenterTestBase() {
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
            assertEquals(StatisticsDialog.None, dialogState)
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

            val state = testedPresenter.screenViewState.first()
            assertTrue(state is StatisticsViewState.Nominal)

            testedPresenter.openPickUser()
            var dialogState = testedPresenter.dialogState.first()
            assertTrue(dialogState is StatisticsDialog.SelectUser)

            testedPresenter.retrieveStatsForUser(testUser2)
            dialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, dialogState)

            coVerify { mockStatisticsInteractor.getStatsForUser(testUser2, 123456789L) }
        }
}

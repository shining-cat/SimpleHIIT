/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.commonutils.NonEmptyList
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import io.mockk.coEvery
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
 * Statistics retrieval tests for StatisticsPresenter.
 * Tests retrieveStatsForUser with various scenarios.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterStatsRetrievalTest : StatisticsPresenterTestBase() {
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
}

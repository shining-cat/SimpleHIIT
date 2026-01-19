/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
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
 * Basic initialization and state management tests for StatisticsPresenter.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterBasicTest : StatisticsPresenterTestBase() {
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
    fun `observeUsers can be collected multiple times`() =
        runTest {
            val userStats = testUserStatistics()
            coEvery {
                mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L)
            } returns Output.Success(userStats)
            every { mockMapper.map(true, userStats) } returns testNominalViewState()

            usersFlow.value = Output.Success(testUsers())

            testedPresenter.observeUsers().take(1).toList()
            testedPresenter.observeUsers().take(1).toList()

            coVerify(exactly = 2) { mockStatisticsInteractor.getStatsForUser(testUser1, 123456789L) }
        }
}

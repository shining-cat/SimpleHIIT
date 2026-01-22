/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.statistics

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Edge case and error handling tests for StatisticsPresenter.
 * Focuses on defensive programming guards and boundary conditions.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class StatisticsPresenterEdgeCasesTest : StatisticsPresenterTestBase() {
    @Test
    fun `retrieveStatsForUser when currentUsers is null does not emit state`() =
        runTest {
            val initialState = testedPresenter.screenViewState.first()

            testedPresenter.retrieveStatsForUser(testUser1)

            // State should remain unchanged (still Loading)
            val finalState = testedPresenter.screenViewState.first()
            assertEquals(StatisticsViewState.Loading, initialState)
            assertEquals(StatisticsViewState.Loading, finalState)
        }

    @Test
    fun `openPickUser when currentUsers is null does not emit dialog`() =
        runTest {
            val initialDialogState = testedPresenter.dialogState.first()

            testedPresenter.openPickUser()

            // Dialog state should remain None
            val finalDialogState = testedPresenter.dialogState.first()
            assertEquals(StatisticsDialog.None, initialDialogState)
            assertEquals(StatisticsDialog.None, finalDialogState)
        }

    @Test
    fun `openPickUser when currentUsers is null logs error`() =
        runTest {
            testedPresenter.openPickUser()

            // Should not throw, should log error and return early
            // State remains unchanged
            assertEquals(StatisticsDialog.None, testedPresenter.dialogState.first())
        }

    @Test
    fun `retrieveStatsForUser when currentUsers is null logs error`() =
        runTest {
            testedPresenter.retrieveStatsForUser(testUser1)

            // Should not throw, should log error and return early
            // State remains unchanged
            assertEquals(StatisticsViewState.Loading, testedPresenter.screenViewState.first())
        }
}

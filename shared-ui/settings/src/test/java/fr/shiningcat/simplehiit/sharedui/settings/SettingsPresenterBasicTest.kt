/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Basic tests for SettingsPresenter.
 * Tests screen state initialization and dialog state.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterBasicTest : SettingsPresenterTestBase() {
    @Test
    fun `screenViewState returns mapped flow from interactor`() =
        runTest(testDispatcher) {
            val nominalState = testNominalViewState()
            every { mockMapper.map(Output.Success(testGeneralSettings())) } returns nominalState
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            // Launch a collector to activate the WhileSubscribed flow
            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val state = testedPresenter.screenViewState.first()
            assertEquals(nominalState, state)

            collectorJob.cancel()
        }

    @Test
    fun `dialogViewState initially emits None`() =
        runTest(testDispatcher) {
            val state = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, state)
        }

    @Test
    fun `cancelDialog emits None dialog state`() =
        runTest(testDispatcher) {
            testedPresenter.addUser("Test")
            advanceUntilIdle()

            testedPresenter.cancelDialog()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }
}

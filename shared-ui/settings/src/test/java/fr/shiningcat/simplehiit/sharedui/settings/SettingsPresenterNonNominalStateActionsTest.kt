/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests action methods when state is NOT Nominal (Loading/Error).
 * These methods check if state is Nominal before proceeding, logging an error otherwise.
 * Testing these else branches ensures proper defensive programming and error handling.
 *
 * This covers the remaining 9 uncovered branches in SettingsPresenter.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterNonNominalStateActionsTest : SettingsPresenterTestBase() {
    @Test
    fun `editWorkPeriodLength when state is Loading logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            advanceUntilIdle()

            testedPresenter.editWorkPeriodLength()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editRestPeriodLength when state is Error logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error("test-error")
            advanceUntilIdle()

            testedPresenter.editRestPeriodLength()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editNumberOfWorkPeriods when state is Loading logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            advanceUntilIdle()

            testedPresenter.editNumberOfWorkPeriods()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `toggleBeepSound when state is Error logs error and does not call interactor`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error("test-error")
            advanceUntilIdle()

            testedPresenter.toggleBeepSound()
            advanceUntilIdle()

            // Interactor should not be called
            coVerify(exactly = 0) { mockSettingsInteractor.setBeepSound(any()) }

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editSessionStartCountDown when state is Loading logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            advanceUntilIdle()

            testedPresenter.editSessionStartCountDown()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editPeriodStartCountDown when state is Error logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error("test-error")
            advanceUntilIdle()

            testedPresenter.editPeriodStartCountDown()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `toggleSelectedExercise when state is Loading logs error and does not call interactor`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            advanceUntilIdle()

            val exerciseType = ExerciseTypeSelected(type = ExerciseType.PLANK, selected = true)
            testedPresenter.toggleSelectedExercise(exerciseType)
            advanceUntilIdle()

            // Interactor methods should not be called
            verify(exactly = 0) { mockSettingsInteractor.toggleExerciseTypeInList(any(), any()) }
            coVerify(exactly = 0) { mockSettingsInteractor.saveSelectedExerciseTypes(any()) }

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editLanguage when state is Error logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Error("test-error")
            advanceUntilIdle()

            testedPresenter.editLanguage()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }

    @Test
    fun `editTheme when state is Loading logs error and does not emit dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.getGeneralSettings() } returns generalSettingsFlow
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            advanceUntilIdle()

            testedPresenter.editTheme()
            advanceUntilIdle()

            // Dialog should remain None
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)

            // Verify logger.e was called
            verify { mockHiitLogger.e("SettingsPresenter", any()) }
        }
}

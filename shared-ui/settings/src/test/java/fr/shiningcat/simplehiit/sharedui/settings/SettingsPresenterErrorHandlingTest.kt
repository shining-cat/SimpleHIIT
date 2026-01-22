/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

/**
 * Tests for SettingsPresenter error handling.
 * Tests non-Nominal state scenarios where operations should be blocked.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterErrorHandlingTest : SettingsPresenterTestBase() {
    @Test
    fun `editWorkPeriodLength with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editWorkPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editRestPeriodLength with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editRestPeriodLength()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editNumberOfWorkPeriods with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editNumberOfWorkPeriods()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `toggleBeepSound with non-Nominal state does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            coEvery { mockSettingsInteractor.setBeepSound(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.toggleBeepSound()
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.setBeepSound(any()) }
        }

    @Test
    fun `editSessionStartCountDown with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editSessionStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editPeriodStartCountDown with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editPeriodStartCountDown()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `validatePeriodLengthInput with non-Nominal state returns null`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validatePeriodLengthInput("25")

            assertEquals(null, result)
            coVerify(exactly = 0) { mockSettingsInteractor.validatePeriodLength(any(), any()) }
        }

    @Test
    fun `validateInputPeriodStartCountdown with non-Nominal state returns null`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputPeriodStartCountdown("6")

            assertEquals(null, result)
            coVerify(exactly = 0) { mockSettingsInteractor.validateInputPeriodStartCountdown(any(), any(), any()) }
        }

    @Test
    fun `validateInputUserNameString with non-Nominal state returns null`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(testUser1)

            assertEquals(null, result)
            coVerify(exactly = 0) { mockSettingsInteractor.validateInputUserName(any(), any()) }
        }

    @Test
    fun `toggleSelectedExercise with non-Nominal state does not call interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            coEvery { mockSettingsInteractor.saveSelectedExerciseTypes(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val exerciseToToggle = ExerciseTypeSelected(ExerciseType.LUNGE, true)
            testedPresenter.toggleSelectedExercise(exerciseToToggle)
            advanceUntilIdle()

            coVerify(exactly = 0) { mockSettingsInteractor.saveSelectedExerciseTypes(any()) }
        }

    @Test
    fun `editLanguage with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editLanguage()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `editTheme with non-Nominal state does not emit dialog`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns SettingsViewState.Loading
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            testedPresenter.editTheme()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }
}

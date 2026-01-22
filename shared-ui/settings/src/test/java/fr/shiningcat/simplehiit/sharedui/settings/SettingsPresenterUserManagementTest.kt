/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.sharedui.settings

import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.DomainError
import fr.shiningcat.simplehiit.domain.common.models.ExerciseType
import fr.shiningcat.simplehiit.domain.common.models.ExerciseTypeSelected
import fr.shiningcat.simplehiit.domain.common.models.User
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

/**
 * Tests for SettingsPresenter user management.
 * Includes user creation, editing, deletion, and validation.
 */
@OptIn(ExperimentalCoroutinesApi::class)
internal class SettingsPresenterUserManagementTest : SettingsPresenterTestBase() {
    @Test
    fun `addUser emits AddUser dialog with empty name`() =
        runTest(testDispatcher) {
            testedPresenter.addUser()
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.AddUser)
            assertEquals("", (dialogState as SettingsDialog.AddUser).userName)
        }

    @Test
    fun `addUser with name emits AddUser dialog with provided name`() =
        runTest(testDispatcher) {
            testedPresenter.addUser("Test Name")
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.AddUser)
            assertEquals("Test Name", (dialogState as SettingsDialog.AddUser).userName)
        }

    @Test
    fun `editUser emits EditUser dialog with user`() =
        runTest(testDispatcher) {
            testedPresenter.editUser(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.EditUser)
            assertEquals(testUser1, (dialogState as SettingsDialog.EditUser).user)
        }

    @Test
    fun `saveUser with new user (id=0) creates user via interactor`() =
        runTest(testDispatcher) {
            val newUser = User(id = 0L, name = "New User", selected = false)
            coEvery { mockSettingsInteractor.createUser(any()) } returns Output.Success(1L)

            testedPresenter.saveUser(newUser)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.createUser(newUser) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `saveUser with existing user updates user via interactor`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.updateUserName(any()) } returns Output.Success(1)

            testedPresenter.saveUser(testUser1)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.updateUserName(testUser1) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `saveUser with create error emits Error dialog`() =
        runTest(testDispatcher) {
            val newUser = User(id = 0L, name = "New User", selected = false)
            coEvery {
                mockSettingsInteractor.createUser(any())
            } returns Output.Error(DomainError.DATABASE_INSERT_FAILED, Exception("Test"))

            testedPresenter.saveUser(newUser)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.Error)
            assertEquals(DomainError.DATABASE_INSERT_FAILED.code, (dialogState as SettingsDialog.Error).errorCode)
        }

    @Test
    fun `saveUser with update error emits Error dialog`() =
        runTest(testDispatcher) {
            coEvery {
                mockSettingsInteractor.updateUserName(any())
            } returns Output.Error(DomainError.DATABASE_UPDATE_FAILED, Exception("Test"))

            testedPresenter.saveUser(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.Error)
            assertEquals(DomainError.DATABASE_UPDATE_FAILED.code, (dialogState as SettingsDialog.Error).errorCode)
        }

    @Test
    fun `deleteUser emits ConfirmDeleteUser dialog`() =
        runTest(testDispatcher) {
            testedPresenter.deleteUser(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.ConfirmDeleteUser)
            assertEquals(testUser1, (dialogState as SettingsDialog.ConfirmDeleteUser).user)
        }

    @Test
    fun `deleteUserConfirmation with success calls interactor and dismisses dialog`() =
        runTest(testDispatcher) {
            coEvery { mockSettingsInteractor.deleteUser(any()) } returns Output.Success(1)

            testedPresenter.deleteUserConfirmation(testUser1)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.deleteUser(testUser1) }
            val dialogState = testedPresenter.dialogViewState.first()
            assertEquals(SettingsDialog.None, dialogState)
        }

    @Test
    fun `deleteUserConfirmation with error emits Error dialog`() =
        runTest(testDispatcher) {
            coEvery {
                mockSettingsInteractor.deleteUser(any())
            } returns Output.Error(DomainError.DATABASE_DELETE_FAILED, Exception("Test"))

            testedPresenter.deleteUserConfirmation(testUser1)
            advanceUntilIdle()

            val dialogState = testedPresenter.dialogViewState.first()
            assertTrue(dialogState is SettingsDialog.Error)
        }

    @Test
    fun `validateInputUserNameString with Nominal state delegates to interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            every {
                mockSettingsInteractor.validateInputUserName(testUser1, listOf(testUser1, testUser2))
            } returns null
            generalSettingsFlow.value = Output.Success(testGeneralSettings())
            advanceUntilIdle()

            val result = testedPresenter.validateInputUserNameString(testUser1)

            assertEquals(null, result)
        }

    @Test
    fun `toggleSelectedExercise with Nominal state calls interactor`() =
        runTest(testDispatcher) {
            every { mockMapper.map(any()) } returns testNominalViewState()
            val toggledList =
                listOf(
                    ExerciseTypeSelected(ExerciseType.LUNGE, false),
                    ExerciseTypeSelected(ExerciseType.PLANK, false),
                )
            every {
                mockSettingsInteractor.toggleExerciseTypeInList(any(), any())
            } returns toggledList
            coEvery { mockSettingsInteractor.saveSelectedExerciseTypes(any()) } returns Unit
            generalSettingsFlow.value = Output.Success(testGeneralSettings())

            val collectorJob =
                launch {
                    testedPresenter.screenViewState.collect {}
                }
            advanceUntilIdle()

            val exerciseToToggle = ExerciseTypeSelected(ExerciseType.LUNGE, true)
            testedPresenter.toggleSelectedExercise(exerciseToToggle)
            advanceUntilIdle()

            coVerify { mockSettingsInteractor.saveSelectedExerciseTypes(toggledList) }

            collectorJob.cancel()
        }
}

/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.shared.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.domain.common.models.CyclesModification
import fr.shiningcat.simplehiit.domain.common.models.User
import fr.shiningcat.simplehiit.sharedui.home.HomeDialog
import fr.shiningcat.simplehiit.sharedui.home.HomePresenter
import fr.shiningcat.simplehiit.sharedui.home.HomeViewState
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * Thin ViewModel wrapper for the Home screen.
 * Provides Android lifecycle-aware scope and exposes state to the UI.
 * All business logic is delegated to HomePresenter.
 */
class HomeViewModel(
    private val homePresenter: HomePresenter,
    private val mainDispatcher: CoroutineDispatcher,
) : ViewModel() {
    val screenViewState: StateFlow<HomeViewState> =
        homePresenter.screenViewState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = HomeViewState.Loading,
            )

    val dialogViewState: StateFlow<HomeDialog> =
        homePresenter.dialogState
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.Companion.WhileSubscribed(stopTimeoutMillis = 5000),
                initialValue = HomeDialog.None,
            )

    fun modifyNumberCycles(modification: CyclesModification) {
        viewModelScope.launch(mainDispatcher) {
            homePresenter.modifyNumberCycles(
                currentViewState = screenViewState.value,
                modification = modification,
            )
        }
    }

    fun toggleSelectedUser(user: User) {
        viewModelScope.launch(mainDispatcher) {
            homePresenter.toggleUserSelection(user)
        }
    }

    fun resetWholeApp() {
        viewModelScope.launch(mainDispatcher) {
            homePresenter.showResetConfirmation()
        }
    }

    fun resetWholeAppConfirmationDeleteEverything() {
        viewModelScope.launch(mainDispatcher) {
            homePresenter.resetWholeApp()
        }
    }

    fun cancelDialog() {
        viewModelScope.launch(mainDispatcher) {
            homePresenter.dismissDialog()
        }
    }
}

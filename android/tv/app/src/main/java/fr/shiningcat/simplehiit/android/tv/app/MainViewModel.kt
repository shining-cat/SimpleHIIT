/*
 * SPDX-FileCopyrightText: 2024-2026 shining-cat
 * SPDX-License-Identifier: GPL-3.0-or-later
 */
package fr.shiningcat.simplehiit.android.tv.app

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.shiningcat.simplehiit.domain.common.Output
import fr.shiningcat.simplehiit.domain.common.models.AppTheme
import fr.shiningcat.simplehiit.domain.settings.usecases.GetGeneralSettingsUseCase
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel for MainActivity that provides the app theme preference.
 * This ensures proper MVVM architecture by keeping business logic out of the Activity.
 */
class MainViewModel(
    getGeneralSettingsUseCase: GetGeneralSettingsUseCase,
) : ViewModel() {
    /**
     * The current app theme preference.
     * Defaults to FOLLOW_SYSTEM if settings cannot be retrieved.
     */
    val appTheme: StateFlow<AppTheme> =
        getGeneralSettingsUseCase
            .execute()
            .map { output ->
                when (output) {
                    is Output.Success -> output.result.currentTheme
                    is Output.Error -> AppTheme.FOLLOW_SYSTEM
                }
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.Eagerly,
                initialValue = AppTheme.FOLLOW_SYSTEM,
            )
}

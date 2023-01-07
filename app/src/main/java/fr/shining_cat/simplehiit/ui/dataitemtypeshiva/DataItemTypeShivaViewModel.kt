/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package fr.shining_cat.simplehiit.ui.dataitemtypeshiva

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import fr.shining_cat.simplehiit.data.DataItemTypeShivaRepository
import fr.shining_cat.simplehiit.ui.dataitemtypeshiva.DataItemTypeShivaUiState.Error
import fr.shining_cat.simplehiit.ui.dataitemtypeshiva.DataItemTypeShivaUiState.Loading
import fr.shining_cat.simplehiit.ui.dataitemtypeshiva.DataItemTypeShivaUiState.Success
import javax.inject.Inject

@HiltViewModel
class DataItemTypeShivaViewModel @Inject constructor(
    private val dataItemTypeShivaRepository: DataItemTypeShivaRepository
) : ViewModel() {

    val uiState: StateFlow<DataItemTypeShivaUiState> = dataItemTypeShivaRepository
        .dataItemTypeShivas.map(::Success)
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addDataItemTypeShiva(name: String) {
        viewModelScope.launch {
            dataItemTypeShivaRepository.add(name)
        }
    }
}

sealed interface DataItemTypeShivaUiState {
    object Loading : DataItemTypeShivaUiState
    data class Error(val throwable: Throwable) : DataItemTypeShivaUiState
    data class Success(val data: List<String>) : DataItemTypeShivaUiState
}

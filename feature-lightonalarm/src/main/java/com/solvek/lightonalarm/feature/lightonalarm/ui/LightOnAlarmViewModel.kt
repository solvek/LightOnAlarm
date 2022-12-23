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

package com.solvek.lightonalarm.feature.lightonalarm.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import com.solvek.lightonalarm.core.data.LightOnAlarmRepositoryOld
import com.solvek.lightonalarm.feature.lightonalarm.ui.LightOnAlarmUiState.Error
import com.solvek.lightonalarm.feature.lightonalarm.ui.LightOnAlarmUiState.Loading
import com.solvek.lightonalarm.feature.lightonalarm.ui.LightOnAlarmUiState.Success
import javax.inject.Inject

@HiltViewModel
class LightOnAlarmViewModel @Inject constructor(
    private val lightOnAlarmRepository: LightOnAlarmRepositoryOld
) : ViewModel() {

    val uiState: StateFlow<LightOnAlarmUiState> = lightOnAlarmRepository
        .lightOnAlarms.map { Success(data = it) }
        .catch { Error(it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), Loading)

    fun addLightOnAlarm(name: String) {
        viewModelScope.launch {
            lightOnAlarmRepository.add(name)
        }
    }
}

sealed interface LightOnAlarmUiState {
    object Loading : LightOnAlarmUiState
    data class Error(val throwable: Throwable) : LightOnAlarmUiState
    data class Success(val data: List<String>) : LightOnAlarmUiState
}

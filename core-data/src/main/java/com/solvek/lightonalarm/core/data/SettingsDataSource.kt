package com.solvek.lightonalarm.core.data

import kotlinx.coroutines.flow.Flow

interface SettingsDataSource {
    val isAlarmEnabledChanges: Flow<Boolean>
    suspend fun setAlarmEnabled(enabled: Boolean)
}
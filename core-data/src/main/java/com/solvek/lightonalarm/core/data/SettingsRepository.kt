package com.solvek.lightonalarm.core.data

import kotlinx.coroutines.flow.Flow

class SettingsRepository(private val dataSource: SettingsDataSource) {
    val isAlarmEnabledChanges: Flow<Boolean> = dataSource.isAlarmEnabledChanges
    suspend fun setAlarmEnabled(enable: Boolean){
        dataSource.setAlarmEnabled(enable)
    }
}
package com.solvek.lightonalarm.core.data

import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LightOnAlarmRepository @Inject constructor(
    private val settingsDataSource: SettingsDataSource,
    private val playDataStore: PlayDataStore,
) {
    private var isEnabled: Boolean? = null

    val isAlarmEnabledChanges: Flow<Boolean> =
        settingsDataSource.isAlarmEnabledChanges.map { enabled ->
            isEnabled = enabled
            enabled
        }

    private val _isPlayingChanges = MutableStateFlow(false)
    val isPlayingChanges: Flow<Boolean> = _isPlayingChanges.asStateFlow()

    suspend fun setAlarmEnabled(enabled: Boolean){
        settingsDataSource.setAlarmEnabled(enabled)
    }

    fun stopPlaying(){
        playDataStore.stop()
        _isPlayingChanges.value = false
    }

    suspend fun notifyLightOn(){
        isAlarmEnabledChanges.first()

        if (!isEnabled!!) return
        _isPlayingChanges.value = true
        playDataStore.play()
    }
}
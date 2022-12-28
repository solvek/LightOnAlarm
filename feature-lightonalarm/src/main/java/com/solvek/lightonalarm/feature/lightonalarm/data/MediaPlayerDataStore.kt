package com.solvek.lightonalarm.feature.lightonalarm.data

import android.content.Context
import com.solvek.lightonalarm.core.data.PlayDataStore
import com.solvek.lightonalarm.feature.lightonalarm.service.LightOnAlarmService.Companion.playAlarm
import com.solvek.lightonalarm.feature.lightonalarm.service.LightOnAlarmService.Companion.stopAlarm
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaPlayerDataStore @Inject constructor(@ApplicationContext private val context: Context) : PlayDataStore {


    override fun play() {
        context.playAlarm()
    }

    override fun stop() {
        context.stopAlarm()
    }
}
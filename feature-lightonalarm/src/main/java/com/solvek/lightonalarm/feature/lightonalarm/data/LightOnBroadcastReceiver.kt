package com.solvek.lightonalarm.feature.lightonalarm.data

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.solvek.lightonalarm.core.data.LightOnAlarmRepository
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LightOnBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var lightOnAlarmRepository: LightOnAlarmRepository

    override fun onReceive(context: Context?, intent: Intent) {
        lightOnAlarmRepository.notifyLightOn()
    }
}
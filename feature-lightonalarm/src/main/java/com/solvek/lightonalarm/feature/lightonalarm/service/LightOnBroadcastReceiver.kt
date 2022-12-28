package com.solvek.lightonalarm.feature.lightonalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.solvek.lightonalarm.core.data.LightOnAlarmRepository
import com.solvek.lightonalarm.feature.lightonalarm.service.LightOnAlarmService.Companion.startLightOnAlarmService
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LightOnBroadcastReceiver : BroadcastReceiver() {
    @Inject
    lateinit var lightOnAlarmRepository: LightOnAlarmRepository

    override fun onReceive(context: Context?, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            context!!.startLightOnAlarmService()
            return
        }

        Log.i(TAG, "Power connected action received")
        context?.let {
            context.startActivity(LightOnAlarmService.createLaunchActivityIntent(context))
        }
        CoroutineScope(SupervisorJob()).launch{
            lightOnAlarmRepository.notifyLightOn()
        }
    }

    companion object {
        private const val TAG = "LightOnReceiver"
    }
}
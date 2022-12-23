package com.solvek.lightonalarm.feature.lightonalarm.service

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootBroadcastReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent) {
        if (Intent.ACTION_BOOT_COMPLETED == intent.action) {
            context!!.startLightOnAlarmService()
        }
    }
}
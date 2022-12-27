package com.solvek.lightonalarm.feature.lightonalarm.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Build
import android.os.IBinder
import android.util.Log
import com.solvek.lightonalarm.feature.lightonalarm.R

fun Context.startLightOnAlarmService(){
    val i = Intent(this, LightOnAlarmService::class.java)
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        startForegroundService(i)
    }
    else {
        startService(i)
    }
}

class LightOnAlarmService : Service() {
    private val receiver = LightOnBroadcastReceiver()

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Service started")

        registerReceiver()
        addNotification()

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        Log.i(TAG, "Service stopped")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {return null}

    private fun registerReceiver() {
        val filter = IntentFilter()
        filter.addAction(Intent.ACTION_POWER_CONNECTED)
        registerReceiver(receiver, filter)
    }

    private fun addNotification() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) return

        createNotificationChannel()

        val launchIntent = createLaunchActivityIntent(this)
        val pendingIntent: PendingIntent =
            launchIntent.let { notificationIntent ->
                PendingIntent.getActivity(this, 0, notificationIntent,
                    PendingIntent.FLAG_IMMUTABLE)
            }

        val notification: Notification = Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle(getText(R.string.notification_title))
            .setContentText(getText(R.string.notification_text))
            .setSmallIcon(android.R.drawable.ic_lock_idle_alarm)
            .setContentIntent(pendingIntent)
            .setTicker(getText(R.string.notification_ticker))
            .build()

        startForeground(1, notification)
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = getString(R.string.channel_name)
            val descriptionText = getString(R.string.channel_description)
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            // Register the channel with the system
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    companion object {
        private const val TAG = "LightOnAlarmService"

        private const val NOTIFICATION_CHANNEL_ID = "10001"

        fun createLaunchActivityIntent(context: Context)
            = context.packageManager.getLaunchIntentForPackage("com.solvek.lightonalarm")
    }
}
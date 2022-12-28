package com.solvek.lightonalarm.feature.lightonalarm.service

import android.app.*
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaPlayer
import android.os.Build
import android.os.IBinder
import android.os.PowerManager
import android.os.PowerManager.WakeLock
import android.util.Log
import com.solvek.lightonalarm.feature.lightonalarm.R


class LightOnAlarmService : Service() {
    private val receiver = LightOnBroadcastReceiver()

    private lateinit var mp: MediaPlayer
    private lateinit var wakeLock: WakeLock

    override fun onCreate() {
        super.onCreate()
        Log.i(TAG, "Service started")

        mp = MediaPlayer.create(this, R.raw.alarm)
        mp.isLooping = true

        registerReceiver()
        addNotification()

        val pm = applicationContext.getSystemService(POWER_SERVICE) as PowerManager
        wakeLock = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "LightOnAlarm:MusicService")
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        Log.i(TAG, "Received command")

        if (intent != null && intent.hasExtra(EXTRA_COMMAND)){
            when(intent.getIntExtra(EXTRA_COMMAND, -1)){
                COMMAND_PLAY -> play()
                COMMAND_STOP -> stop()
            }
        }

        return super.onStartCommand(intent, flags, startId)
    }

    override fun onDestroy() {
        unregisterReceiver(receiver)
        Log.i(TAG, "Service stopped")
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {return null}

    private fun play(){
        wakeLock.acquire(5*60*1000L)
        mp.start()
    }

    private fun stop(){
        mp.pause();
        mp.seekTo(0);
        if(wakeLock.isHeld)
            wakeLock.release()
    }

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

        private const val COMMAND_PLAY = 1
        private const val COMMAND_STOP = 2

        private const val EXTRA_COMMAND = "command"

        fun Context.playAlarm(){
            runCommand(COMMAND_PLAY)
        }

        fun Context.stopAlarm(){
            runCommand(COMMAND_STOP)
        }

        fun Context.startLightOnAlarmService(){
            startLightOnAlarmService(createIntent())
        }

        private fun Context.startLightOnAlarmService(i: Intent){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                startForegroundService(i)
            }
            else {
                startService(i)
            }
        }

        private fun Context.createIntent() = Intent(this, LightOnAlarmService::class.java)

        private fun Context.runCommand(command: Int){
            val intent = this.createIntent()
            intent.putExtra(EXTRA_COMMAND, command)
            this.startLightOnAlarmService(intent)
        }
    }
}
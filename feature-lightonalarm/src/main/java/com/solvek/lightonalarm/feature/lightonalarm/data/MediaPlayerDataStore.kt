package com.solvek.lightonalarm.feature.lightonalarm.data

import android.content.Context
import android.media.MediaPlayer
import com.solvek.lightonalarm.core.data.PlayDataStore
import com.solvek.lightonalarm.feature.lightonalarm.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class MediaPlayerDataStore @Inject constructor(@ApplicationContext context: Context) : PlayDataStore {
    private val mp = MediaPlayer.create(context, R.raw.alarm)

    override fun play() {
        mp.isLooping = true
        mp.start()
    }

    override fun stop() {
        mp.stop()
    }
}
package com.solvek.lightonalarm.core.data

import kotlinx.coroutines.flow.Flow

interface PlayDataStore {
    fun play()
    fun stop()
}
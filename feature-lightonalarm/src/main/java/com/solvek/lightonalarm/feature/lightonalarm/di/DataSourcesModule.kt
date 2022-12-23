package com.solvek.lightonalarm.feature.lightonalarm.di

import com.solvek.lightonalarm.core.data.PlayDataStore
import com.solvek.lightonalarm.core.data.SettingsDataSource
import com.solvek.lightonalarm.feature.lightonalarm.data.MediaPlayerDataStore
import com.solvek.lightonalarm.feature.lightonalarm.data.SettingsDataStoreDataSource
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
interface DataSourcesModule {
    @Singleton
    @Binds
    fun bindsPlayDataSource(
        playDataStore: MediaPlayerDataStore
    ): PlayDataStore

    @Singleton
    @Binds
    fun bindsSettingsDataSource(
        settingsDataSource: SettingsDataStoreDataSource
    ): SettingsDataSource
}
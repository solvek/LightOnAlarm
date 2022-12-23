package com.solvek.lightonalarm.feature.lightonalarm.data

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import com.solvek.lightonalarm.core.data.SettingsDataSource
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first

private val KEY_IS_ALARM_ENABLED = booleanPreferencesKey("isAlarmEnabled")

class SettingsDataStoreDataSource @Inject constructor(@ApplicationContext private val context: Context) : SettingsDataSource{
    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override val isAlarmEnabledChanges: Flow<Boolean>
        get() = context.dataStore.data.map { preferences ->
            preferences[KEY_IS_ALARM_ENABLED] ?: true
        }

    override suspend fun setAlarmEnabled(enabled: Boolean) {
        context.dataStore.edit { settings ->
            settings[KEY_IS_ALARM_ENABLED] = enabled
        }
    }
}
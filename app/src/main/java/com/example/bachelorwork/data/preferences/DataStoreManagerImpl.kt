package com.example.bachelorwork.data.preferences

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import com.example.bachelorwork.domain.manager.DataStoreManager
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map


class DataStoreManagerImpl(
    private val dataStore: DataStore<Preferences>
): DataStoreManager {

    override suspend fun <T> getPreference(preferenceKey: Preferences.Key<T>): Flow<T?> {
        return dataStore.data.map { preferences ->
            preferences[preferenceKey]
        }
    }

    override suspend fun <T> savePreference(preferencePair: Preferences.Pair<T>): Result<Unit> {
        return runCatching {
            dataStore.edit { preferences ->
                preferences.plusAssign(preferencePair)
            }
        }
    }

    override suspend fun <T> updatePreference(preferencePair: Preferences.Pair<T>): Result<Unit> {
        return runCatching {
            dataStore.updateData { preferences ->
                preferences.toPreferences()
            }
        }
    }

    override suspend fun <T> deletePreference(preferenceKey: Preferences.Key<T>): Result<Unit> {
        return runCatching {
            dataStore.edit { preferences ->
                preferences.remove(preferenceKey)
            }
        }
    }

    override suspend fun <T> clearPreference(preferenceKey: Preferences.Key<T>): Result<Unit> {
        return runCatching {
            dataStore.edit { preferences ->
                preferences.clear()
            }
        }
    }
}
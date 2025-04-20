package com.example.bachelorwork.domain.manager

import androidx.datastore.preferences.core.Preferences
import kotlinx.coroutines.flow.Flow

interface DataStoreManager {
    suspend fun <T> getPreference(preferenceKey: Preferences.Key<T>): Flow<T?>
    suspend fun <T> savePreference(preferencePair: Preferences.Pair<T>): Result<Unit>
    suspend fun <T> updatePreference(preferencePair: Preferences.Pair<T>): Result<Unit>
    suspend fun <T> deletePreference(preferenceKey: Preferences.Key<T>): Result<Unit>
    suspend fun <T> clearPreference(preferenceKey: Preferences.Key<T>): Result<Unit>
}
package com.doodle.core.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

internal const val IS_AVAILABLE_FOR_APP_OPEN_AD_THRESHOLD = 2

class AppPreferencesDataStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val PREFERENCES_NAME = "app_preferences"
    }

    object Keys {
        val IS_AVAILABLE_FOR_REVIEW = booleanPreferencesKey("is_available_for_review")
        val IS_AVAILABLE_FOR_APP_OPEN_AD = intPreferencesKey("is_available_for_app_open_ad")
    }

    private val dataFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    suspend fun setIsAvailableForReview(value: Boolean) {
        dataStore.edit { preferences ->
            preferences[Keys.IS_AVAILABLE_FOR_REVIEW] = value
        }
    }

    suspend fun getIsAvailableForReview(): Boolean {
        return dataFlow.first()[Keys.IS_AVAILABLE_FOR_REVIEW] ?: true
    }

    suspend fun incrementAppOpenTimes() {
        dataStore.edit { preferences ->
            val current = preferences[Keys.IS_AVAILABLE_FOR_APP_OPEN_AD] ?: 0
            preferences[Keys.IS_AVAILABLE_FOR_APP_OPEN_AD] = current + 1
        }
    }

    fun getIsAvailableForAppOpenAd(): Flow<Boolean> {
        return dataFlow.map { preferences ->
            val current = preferences[Keys.IS_AVAILABLE_FOR_APP_OPEN_AD] ?: 0
            current > IS_AVAILABLE_FOR_APP_OPEN_AD_THRESHOLD
        }
    }
}

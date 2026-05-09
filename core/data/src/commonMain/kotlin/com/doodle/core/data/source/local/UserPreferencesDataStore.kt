package com.doodle.core.data.source.local

import androidx.datastore.core.DataStore
import androidx.datastore.core.IOException
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import com.doodle.core.domain.enums.RemoveAdsStatus
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map

class UserPreferencesDataStore(private val dataStore: DataStore<Preferences>) {
    companion object {
        const val PREFERENCES_NAME = "user_preferences"
    }

    object Keys {
        val remove_ads_bought = stringPreferencesKey("remove_ads_bought")
    }

    private val dataFlow = dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }

    suspend fun setRemoveAds(value: RemoveAdsStatus) {
        dataStore.edit { preferences ->
            preferences[Keys.remove_ads_bought] = value.name
        }
    }

    suspend fun getRemoveAdsStatus(): RemoveAdsStatus {
        val value = dataFlow.first()[Keys.remove_ads_bought] ?: RemoveAdsStatus.NOT_PURCHASED.name
        return RemoveAdsStatus.valueOf(value)
    }

    fun getRemoveAdsStatusFlow(): Flow<RemoveAdsStatus> {
        return dataFlow.map { preferences ->
            val value = preferences[Keys.remove_ads_bought] ?: RemoveAdsStatus.NOT_PURCHASED.name
            RemoveAdsStatus.valueOf(value)
        }
    }

}
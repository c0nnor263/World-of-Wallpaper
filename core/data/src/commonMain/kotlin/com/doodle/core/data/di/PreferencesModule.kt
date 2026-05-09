package com.doodle.core.data.di

import androidx.datastore.core.DataStore
import androidx.datastore.core.DataStoreFactory
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import com.doodle.core.data.source.local.AppPreferencesDataStore
import com.doodle.core.data.source.local.UserPreferencesDataStore
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module
class PreferencesModule {
    @Single
    fun provideUserPreferencesDataStore(
        storageProvider: PreferencesStorageProvider
    ): UserPreferencesDataStore {
        val dataStoreFileName = UserPreferencesDataStore.PREFERENCES_NAME
        val storage: Storage<Preferences> = storageProvider.get(dataStoreFileName)
        return UserPreferencesDataStore(createDataStore(storage))
    }

    @Single
    fun provideAppPreferencesDataStore(
        storageProvider: PreferencesStorageProvider
    ): AppPreferencesDataStore {
        val dataStoreFileName = AppPreferencesDataStore.PREFERENCES_NAME
        val storage: Storage<Preferences> = storageProvider.get(dataStoreFileName)
        return AppPreferencesDataStore(createDataStore(storage))
    }
}

internal fun createDataStore(storage: Storage<Preferences>): DataStore<Preferences> =
    DataStoreFactory.create(storage = storage)
package com.doodle.core.data.di

import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import org.koin.core.annotation.Factory
import org.koin.core.scope.Scope

expect class PreferencesStorageModule {
    @Factory
    fun provideStorage(scope: Scope): PreferencesStorageProvider
}

interface PreferencesStorageProvider {
    fun get(dataStoreFileName: String): Storage<Preferences>
}
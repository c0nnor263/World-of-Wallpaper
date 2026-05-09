package com.doodle.core.data.di

import android.content.Context
import androidx.datastore.core.FileStorage
import androidx.datastore.core.Storage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesFileSerializer
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

@Module
actual class PreferencesStorageModule {
    @Factory
    actual fun provideStorage(scope: Scope): PreferencesStorageProvider {
        return PreferencesStorageProviderAndroid(scope)
    }
}

class PreferencesStorageProviderAndroid(scope: Scope) : PreferencesStorageProvider {
    val context: Context = scope.get()
    override fun get(dataStoreFileName: String): Storage<Preferences> {
        return FileStorage(
            serializer = PreferencesFileSerializer,
            produceFile = { context.filesDir.resolve(dataStoreFileName) }
        )
    }
}
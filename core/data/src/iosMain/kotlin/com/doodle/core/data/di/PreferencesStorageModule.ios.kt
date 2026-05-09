package com.doodle.core.data.di

import androidx.datastore.core.Storage
import androidx.datastore.core.okio.OkioStorage
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.PreferencesSerializer
import kotlinx.cinterop.ExperimentalForeignApi
import okio.FileSystem
import okio.Path.Companion.toPath
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSURL
import platform.Foundation.NSUserDomainMask

@Module
actual class PreferencesStorageModule {
    @OptIn(ExperimentalForeignApi::class)
    @Factory
    actual fun provideStorage(scope: Scope): PreferencesStorageProvider {
        return PreferencesStorageProviderIos()
    }
}

class PreferencesStorageProviderIos : PreferencesStorageProvider {
    @OptIn(ExperimentalForeignApi::class)
    override fun get(dataStoreFileName: String): Storage<Preferences> {
        return OkioStorage(
            fileSystem = FileSystem.SYSTEM,
            serializer = PreferencesSerializer,
            producePath = {
                val documentDirectory: NSURL? = NSFileManager.defaultManager.URLForDirectory(
                    directory = NSDocumentDirectory,
                    inDomain = NSUserDomainMask,
                    appropriateForURL = null,
                    create = false,
                    error = null,
                )
                (requireNotNull(documentDirectory).path + "/$dataStoreFileName").toPath()
            }
        )
    }
}
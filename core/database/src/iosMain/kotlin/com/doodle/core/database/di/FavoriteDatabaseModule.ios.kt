package com.doodle.core.database.di

import androidx.room.Room
import androidx.room.RoomDatabase
import com.doodle.core.database.FavoriteDatabase
import com.doodle.core.database.FavoriteDatabaseBuilder
import com.doodle.core.database.domain.FAVORITE_DATABASE_NAME
import kotlinx.cinterop.ExperimentalForeignApi
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope
import platform.Foundation.NSDocumentDirectory
import platform.Foundation.NSFileManager
import platform.Foundation.NSUserDomainMask

@Module
actual class FavoriteDatabaseModule {
    @Factory
    actual fun provideBuilder(scope: Scope): FavoriteDatabaseBuilder {
        return FavoriteDatabaseBuilderIOS()
    }
}


class FavoriteDatabaseBuilderIOS : FavoriteDatabaseBuilder {
    override fun get(): RoomDatabase.Builder<FavoriteDatabase> {
        val dbFilePath = documentDirectory() + "/$FAVORITE_DATABASE_NAME"
        return Room.databaseBuilder<FavoriteDatabase>(
            name = dbFilePath
        )
    }

    @OptIn(ExperimentalForeignApi::class)
    private fun documentDirectory(): String {
        val documentDirectory = NSFileManager.defaultManager.URLForDirectory(
            directory = NSDocumentDirectory,
            inDomain = NSUserDomainMask,
            appropriateForURL = null,
            create = false,
            error = null,
        )
        return requireNotNull(documentDirectory?.path)
    }
}
package com.doodle.core.database.di

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.doodle.core.database.FavoriteDatabase
import com.doodle.core.database.FavoriteDatabaseBuilder
import com.doodle.core.database.domain.FAVORITE_DATABASE_NAME
import org.koin.core.annotation.Factory
import org.koin.core.annotation.Module
import org.koin.core.scope.Scope

@Module
actual class FavoriteDatabaseModule {
    @Factory
    actual fun provideBuilder(scope : Scope): FavoriteDatabaseBuilder {
        return FavoriteDatabaseBuilderAndroid(scope = scope)
    }
}

class FavoriteDatabaseBuilderAndroid(scope: Scope) : FavoriteDatabaseBuilder {
    val context: Context = scope.get()
    override fun get(): RoomDatabase.Builder<FavoriteDatabase> {
        val appContext = context.applicationContext
        val dbFile = appContext.getDatabasePath(FAVORITE_DATABASE_NAME)
        return Room.databaseBuilder<FavoriteDatabase>(
            context = appContext,
            name = dbFile.absolutePath
        )
    }
}
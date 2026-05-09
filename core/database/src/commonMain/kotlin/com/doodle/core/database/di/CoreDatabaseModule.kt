package com.doodle.core.database.di

import androidx.sqlite.driver.bundled.BundledSQLiteDriver
import com.doodle.core.database.FavoriteDatabase
import com.doodle.core.database.FavoriteDatabaseBuilder
import com.doodle.core.database.dao.FavoriteImageDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Configuration
import org.koin.core.annotation.Module
import org.koin.core.annotation.Single

@Module(
    includes = [
        FavoriteDatabaseModule::class
    ]
)
@Configuration
@ComponentScan("com.doodle.core.database")
class CoreDatabaseModule {

    @Single
    fun provideFavoriteDatabase(
        builder: FavoriteDatabaseBuilder
    ): FavoriteDatabase {
        return builder.get()
            .setDriver(BundledSQLiteDriver())
            .setQueryCoroutineContext(Dispatchers.IO)
            .build()
    }

    @Single
    fun provideFavoriteImageDao(
        favoriteDatabase: FavoriteDatabase
    ): FavoriteImageDao = favoriteDatabase.favoriteImageDao()
}

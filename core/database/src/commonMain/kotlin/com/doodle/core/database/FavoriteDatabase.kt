package com.doodle.core.database

import androidx.room.ConstructedBy
import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.RoomDatabaseConstructor
import com.doodle.core.database.dao.FavoriteImageDao
import com.doodle.core.database.domain.model.FavoriteImage

@Database(entities = [FavoriteImage::class], version = 1, exportSchema = false)
@ConstructedBy(FavoriteDatabaseConstructor::class)
abstract class FavoriteDatabase : RoomDatabase() {
    abstract fun favoriteImageDao(): FavoriteImageDao
}

// The Room compiler generates the `actual` implementations.
@Suppress("KotlinNoActualForExpect", "EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
expect object FavoriteDatabaseConstructor : RoomDatabaseConstructor<FavoriteDatabase> {
    override fun initialize(): FavoriteDatabase
}


interface FavoriteDatabaseBuilder {
    fun get(): RoomDatabase.Builder<FavoriteDatabase>
}

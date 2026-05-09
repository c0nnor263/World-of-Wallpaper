package com.doodle.core.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.doodle.core.database.domain.model.FavoriteImage

@Dao
interface FavoriteImageDao {

    @Upsert
    suspend fun upsert(favoriteImage: FavoriteImage)

    @Query("DELETE FROM FavoriteImage WHERE imageId = :imageId")
    suspend fun deleteById(imageId: Int)

    @Query("SELECT * FROM FavoriteImage")
    suspend fun getAll(): List<FavoriteImage>

    @Query("SELECT COUNT(*) FROM FavoriteImage")
    suspend fun getCount(): Int

    @Query("SELECT COUNT(*) FROM FavoriteImage WHERE imageId = :imageId")
    suspend fun checkForFavorite(imageId: Int): Int
}

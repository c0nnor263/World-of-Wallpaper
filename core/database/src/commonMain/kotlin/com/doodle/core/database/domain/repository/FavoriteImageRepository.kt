package com.doodle.core.database.domain.repository

import com.doodle.core.database.domain.model.FavoriteImage
import com.doodle.core.domain.source.local.repository.LocalImageRepository

interface FavoriteImageRepository : LocalImageRepository {
    suspend fun upsertById(favoriteImage: FavoriteImage)
    suspend fun deleteById(imageId: Int)

    suspend fun getAll(): List<FavoriteImage>

    suspend fun getCount(): Int
    suspend fun checkForFavorite(imageId: Int): Boolean
}

package com.doodle.core.data.source.local.repository

import com.doodle.core.database.dao.FavoriteImageDao
import com.doodle.core.database.domain.model.FavoriteImage
import com.doodle.core.database.domain.repository.FavoriteImageRepository
import com.doodle.core.domain.model.remote.RemoteImage
import org.koin.core.annotation.Single

@Single(binds = [FavoriteImageRepository::class])
class FavoriteImageRepositoryImpl(
    private val favoriteImageDao: FavoriteImageDao
) : FavoriteImageRepository {
    override suspend fun upsertById(favoriteImage: FavoriteImage) {
        favoriteImageDao.upsert(favoriteImage)
    }

    override suspend fun getCount(): Int {
        return favoriteImageDao.getCount()
    }

    override suspend fun deleteById(imageId: Int) {
        favoriteImageDao.deleteById(imageId)
    }

    override suspend fun getAll(): List<FavoriteImage> {
        return favoriteImageDao.getAll()
    }

    override suspend fun checkForFavorite(imageId: Int): Boolean {
        return favoriteImageDao.checkForFavorite(imageId) > 0
    }

    override suspend fun getPagingSource(): List<RemoteImage.Hit> {
        return getAll().map {
            it.mapToRemoteImageHit()
        }
    }
}

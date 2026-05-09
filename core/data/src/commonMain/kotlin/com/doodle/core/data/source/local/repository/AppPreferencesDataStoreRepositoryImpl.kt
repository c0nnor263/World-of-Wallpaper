package com.doodle.core.data.source.local.repository

import com.doodle.core.data.source.local.AppPreferencesDataStore
import com.doodle.core.domain.source.local.repository.AppPreferencesDataStoreRepository
import org.koin.core.annotation.Single

@Single(binds = [AppPreferencesDataStoreRepository::class])
class AppPreferencesDataStoreRepositoryImpl(
    private val appPreferencesDataStore: AppPreferencesDataStore
) : AppPreferencesDataStoreRepository {
    override suspend fun setIsAvailableForReview(value: Boolean) {
        appPreferencesDataStore.setIsAvailableForReview(value)
    }

    override suspend fun getIsAvailableForReview(): Boolean {
        return appPreferencesDataStore.getIsAvailableForReview()
    }

    override suspend fun incrementAppOpenTimes() {
        appPreferencesDataStore.incrementAppOpenTimes()
    }

    override suspend fun getIsAvailableForAppOpenAd(): Boolean {
        return appPreferencesDataStore.getIsAvailableForAppOpenAd()
    }
}

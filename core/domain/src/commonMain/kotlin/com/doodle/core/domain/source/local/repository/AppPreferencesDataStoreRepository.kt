package com.doodle.core.domain.source.local.repository

interface AppPreferencesDataStoreRepository {
    suspend fun setIsAvailableForReview(value: Boolean)
    suspend fun getIsAvailableForReview(): Boolean

    suspend fun incrementAppOpenTimes()

    suspend fun getIsAvailableForAppOpenAd(): Boolean
}

package com.doodle.core.domain.source.local.repository

import kotlinx.coroutines.flow.Flow

interface AppPreferencesDataStoreRepository {
    suspend fun setIsAvailableForReview(value: Boolean)
    suspend fun getIsAvailableForReview(): Boolean

    suspend fun incrementAppOpenTimes()

    fun getIsAvailableForAppOpenAdFlow(): Flow<Boolean>
}

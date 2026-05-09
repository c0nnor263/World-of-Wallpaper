package com.doodle.core.domain.source.local.repository

import com.doodle.core.domain.enums.RemoveAdsStatus
import kotlinx.coroutines.flow.Flow

interface UserPreferencesDataStoreRepository {
    suspend fun setRemoveAds(value: RemoveAdsStatus)
    suspend fun getRemoveAdsStatus(): RemoveAdsStatus
    fun getRemoveAdsStatusFlow(): Flow<RemoveAdsStatus>
}


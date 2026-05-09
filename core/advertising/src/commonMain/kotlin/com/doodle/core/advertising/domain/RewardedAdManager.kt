package com.doodle.core.advertising.domain

import com.doodle.core.advertising.enums.RewardedAdResult

interface RewardedAdManager {
    suspend fun loadAd(adUnitID: String): Result<CommonRewardedAd>
    suspend fun showAd(): RewardedAdResult
    fun reset()
}
package com.doodle.core.advertising.data

import androidx.activity.ComponentActivity
import com.doodle.core.advertising.domain.AdvertisingManager
import com.doodle.core.advertising.domain.BannerAdProvider
import com.doodle.core.advertising.domain.RewardedAdManager

class AndroidAdvertisingManager(
    private val activityProvider: () -> ComponentActivity
) : AdvertisingManager {
    override val rewarded: RewardedAdManager = AndroidRewardedAdManager(activityProvider)
    override val banner: BannerAdProvider = AndroidBannerAdProvider()
}
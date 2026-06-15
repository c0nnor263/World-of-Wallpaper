package com.doodle.core.advertising.data

import androidx.activity.ComponentActivity
import com.doodle.core.advertising.appopen.data.AndroidAppOpenAdManager
import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.banner.data.AndroidBannerAdProvider
import com.doodle.core.advertising.banner.domain.BannerAdProvider
import com.doodle.core.advertising.domain.AdvertisingManager
import com.doodle.core.advertising.rewarded.data.AndroidRewardedAdManager
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager

class AndroidAdvertisingManager(
    activityProvider: () -> ComponentActivity
) : AdvertisingManager {
    override val rewarded: RewardedAdManager = AndroidRewardedAdManager(activityProvider)
    override val banner: BannerAdProvider = AndroidBannerAdProvider()
    override val appOpen: AppOpenAdManager = AndroidAppOpenAdManager(activityProvider)
}
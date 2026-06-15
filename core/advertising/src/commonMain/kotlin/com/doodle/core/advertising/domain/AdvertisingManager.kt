package com.doodle.core.advertising.domain

import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.banner.domain.BannerAdProvider
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager

interface AdvertisingManager {
    val rewarded: RewardedAdManager
    val banner: BannerAdProvider
    val appOpen: AppOpenAdManager
}
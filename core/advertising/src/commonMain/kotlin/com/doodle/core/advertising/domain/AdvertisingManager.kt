package com.doodle.core.advertising.domain

interface AdvertisingManager {
    val rewarded: RewardedAdManager
    val banner: BannerAdProvider
}
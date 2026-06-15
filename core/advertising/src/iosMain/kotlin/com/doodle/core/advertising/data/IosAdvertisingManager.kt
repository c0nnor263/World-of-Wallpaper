package com.doodle.core.advertising.data

import com.doodle.core.advertising.appopen.data.IosAppOpenAdManager
import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.banner.data.IosBannerAdProvider
import com.doodle.core.advertising.banner.domain.BannerAdProvider
import com.doodle.core.advertising.domain.AdvertisingManager
import com.doodle.core.advertising.domain.IosAdvertisingBridge
import com.doodle.core.advertising.rewarded.data.IosRewardedAdManager
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager

class IosAdvertisingManager(
    bridge: IosAdvertisingBridge
) : AdvertisingManager {

    override val rewarded: RewardedAdManager =
        IosRewardedAdManager(bridge.rewarded)

    override val banner: BannerAdProvider =
        IosBannerAdProvider(bridge.banner)

    override val appOpen: AppOpenAdManager =
        IosAppOpenAdManager(bridge.appOpen)
}
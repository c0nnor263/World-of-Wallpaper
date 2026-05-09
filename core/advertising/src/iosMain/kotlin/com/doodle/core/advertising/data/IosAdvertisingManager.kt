package com.doodle.core.advertising.data

import com.doodle.core.advertising.domain.AdvertisingManager
import com.doodle.core.advertising.domain.BannerAdProvider
import com.doodle.core.advertising.domain.IosAdvertisingBridge
import com.doodle.core.advertising.domain.RewardedAdManager

class IosAdvertisingManager(
    bridge: IosAdvertisingBridge
) : AdvertisingManager {

    override val rewarded: RewardedAdManager =
        IosRewardedAdManager(bridge.rewarded)

    override val banner: BannerAdProvider =
        IosBannerAdProvider(bridge.banner)
}
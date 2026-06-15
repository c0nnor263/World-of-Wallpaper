package com.doodle.core.advertising.domain

import com.doodle.core.advertising.appopen.domain.IosAppOpenAdBridge
import com.doodle.core.advertising.banner.domain.IosBannerAdBridge
import com.doodle.core.advertising.rewarded.domain.IosRewardedAdBridge

interface IosAdvertisingBridge {
    val rewarded: IosRewardedAdBridge
    val banner: IosBannerAdBridge
    val appOpen: IosAppOpenAdBridge
}
package com.doodle.core.advertising.domain

interface IosAdvertisingBridge {
    val rewarded: IosRewardedAdBridge
    val banner: IosBannerAdBridge
}
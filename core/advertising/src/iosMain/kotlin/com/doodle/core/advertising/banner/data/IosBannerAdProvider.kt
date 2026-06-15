package com.doodle.core.advertising.banner.data

import com.doodle.core.advertising.banner.domain.BannerAdProvider
import com.doodle.core.advertising.banner.domain.IosBannerAdBridge
import platform.UIKit.UIView

class IosBannerAdProvider(
    private val bridge: IosBannerAdBridge
) : BannerAdProvider {

    fun createView(adUnitId: String): UIView {
        return bridge.createBannerView(adUnitId)
    }
}
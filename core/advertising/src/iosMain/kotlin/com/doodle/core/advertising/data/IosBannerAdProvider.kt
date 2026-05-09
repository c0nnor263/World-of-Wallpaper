package com.doodle.core.advertising.data

import com.doodle.core.advertising.domain.BannerAdProvider
import com.doodle.core.advertising.domain.IosBannerAdBridge
import platform.UIKit.UIView

class IosBannerAdProvider(
    private val bridge: IosBannerAdBridge
) : BannerAdProvider {

    fun createView(adUnitId: String): UIView {
        return bridge.createBannerView(adUnitId)
    }
}
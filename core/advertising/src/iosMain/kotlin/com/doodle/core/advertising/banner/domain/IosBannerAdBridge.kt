package com.doodle.core.advertising.banner.domain

import platform.UIKit.UIView

interface IosBannerAdBridge {
    fun createBannerView(adUnitId: String): UIView
}
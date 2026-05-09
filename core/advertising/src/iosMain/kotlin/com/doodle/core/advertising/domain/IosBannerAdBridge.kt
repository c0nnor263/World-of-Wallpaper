package com.doodle.core.advertising.domain

import platform.UIKit.UIView

interface IosBannerAdBridge {
    fun createBannerView(adUnitId: String): UIView
}
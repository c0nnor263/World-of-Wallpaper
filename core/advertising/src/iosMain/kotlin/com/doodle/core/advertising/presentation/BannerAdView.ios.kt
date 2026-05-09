package com.doodle.core.advertising.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.UIKitView
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.data.IosBannerAdProvider

@Composable
actual fun BannerAdView(modifier: Modifier, adUnitId: String) {
    val advertisingManager = LocalAdvertisingManager.current
    val bannerProvider = advertisingManager.banner as IosBannerAdProvider

    UIKitView(
        modifier = modifier,
        factory = {
            bannerProvider.createView(adUnitId)
        }
    )
}
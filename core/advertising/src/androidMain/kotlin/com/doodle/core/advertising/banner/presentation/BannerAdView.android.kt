package com.doodle.core.advertising.banner.presentation

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.viewinterop.AndroidView
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.banner.data.AndroidBannerAdProvider

@SuppressLint("MissingPermission")
@Composable
actual fun BannerAdView(modifier: Modifier, adUnitId: String) {
    val advertisingManager = LocalAdvertisingManager.current
    val bannerProvider = advertisingManager.banner as AndroidBannerAdProvider
    val deviceCurrentWidth = LocalConfiguration.current.screenWidthDp

    AndroidView(
        modifier = modifier,
        factory = { context: Context ->
            bannerProvider.createView(
                context,
                adUnitId,
                deviceCurrentWidth
            )
        }
    )
}
package com.doodle.core.advertising.banner.data

import android.annotation.SuppressLint
import android.content.Context
import com.doodle.core.advertising.banner.domain.BannerAdProvider
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView

class AndroidBannerAdProvider : BannerAdProvider {

    @SuppressLint("MissingPermission")
    fun createView(context: Context, adUnitId: String, width: Int): AdView {
        return AdView(context).apply {
            setAdSize(
                AdSize.getPortraitInlineAdaptiveBannerAdSize(
                    context,
                    width
                )
            )
            setAdUnitId(adUnitId)
            loadAd(AdRequest.Builder().build())
        }
    }
}
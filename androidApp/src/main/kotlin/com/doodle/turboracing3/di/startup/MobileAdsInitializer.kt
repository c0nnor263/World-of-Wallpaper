package com.doodle.turboracing3.di.startup

import android.content.Context
import androidx.startup.Initializer
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.RequestConfiguration

class MobileAdsInitializer : Initializer<MobileAdsInitializer> {
    private val configuration = RequestConfiguration.Builder()
        .setTestDeviceIds(
            listOf(
                "09AF940917E6B5AF122140AE402FABF1"
            )
        )
        .build()

    override fun create(context: Context): MobileAdsInitializer {
        return MobileAdsInitializer().also {
            MobileAds.setRequestConfiguration(configuration)
            MobileAds.initialize(context)
        }
    }

    override fun dependencies(): List<Class<out Initializer<*>>> {
        return emptyList()
    }
}

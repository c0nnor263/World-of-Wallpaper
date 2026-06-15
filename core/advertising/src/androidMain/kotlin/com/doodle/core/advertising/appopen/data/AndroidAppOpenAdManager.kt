package com.doodle.core.advertising.appopen.data

import androidx.activity.ComponentActivity
import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.domain.enums.AdShowResult
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.appopen.AppOpenAd
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidAppOpenAdManager(
    private val activityProvider: () -> ComponentActivity,
) : AppOpenAdManager() {

    private var appOpenAd: AppOpenAd? = null

    override fun hasCachedAd(): Boolean {
        return appOpenAd != null
    }

    override suspend fun platformLoadAd(adUnitID: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            AppOpenAd.load(
                activityProvider(),
                adUnitID,
                AdRequest.Builder().build(),
                object : AppOpenAd.AppOpenAdLoadCallback() {
                    override fun onAdLoaded(ad: AppOpenAd) {
                        appOpenAd = ad
                        cont.resume(Result.success(Unit))
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        cont.resume(Result.failure(Exception(error.message)))
                    }
                }
            )
        }

    override suspend fun platformShowAd(): AdShowResult =
        suspendCancellableCoroutine { cont ->
            val localAd = appOpenAd ?: run {
                cont.resume(AdShowResult.ERROR); return@suspendCancellableCoroutine
            }
            localAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    cont.resume(AdShowResult.DISMISSED)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    cont.resume(AdShowResult.ERROR)
                }
            }
            localAd.show(activityProvider())
        }

    override fun platformReset() {
        super.platformReset()
        appOpenAd = null
    }
}
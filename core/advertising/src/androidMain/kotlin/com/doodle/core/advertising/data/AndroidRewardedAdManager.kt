package com.doodle.core.advertising.data

import androidx.activity.ComponentActivity
import com.doodle.core.advertising.domain.CommonRewardedAd
import com.doodle.core.advertising.domain.RewardedAdManager
import com.doodle.core.advertising.enums.RewardedAdResult
import com.doodle.core.advertising.wasLoadTimeLessThanLimitHoursAgo
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class AndroidRewardedAdManager(
    private val activityProvider: () -> ComponentActivity
) : RewardedAdManager {
    private var rewardedAd: RewardedAd? = null
    private var lastLoadTime: Long = 0

    override suspend fun loadAd(adUnitID: String): Result<CommonRewardedAd> =
        suspendCancellableCoroutine { continuation ->
            val localAd = rewardedAd
            if (localAd != null && wasLoadTimeLessThanLimitHoursAgo(lastLoadTime, 1)) {
                continuation.resume(Result.success(CommonRewardedAd()))
                return@suspendCancellableCoroutine
            }

            RewardedAd.load(
                activityProvider(),
                adUnitID,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        lastLoadTime = System.currentTimeMillis()
                        rewardedAd = ad
                        continuation.resume(Result.success(CommonRewardedAd()))
                    }

                    override fun onAdFailedToLoad(error: LoadAdError) {
                        val adException = Exception(error.message)
                        val result = Result.failure<CommonRewardedAd>(adException)
                        continuation.resume(result)
                    }
                }
            )
        }

    override suspend fun showAd(): RewardedAdResult =
        suspendCancellableCoroutine { continuation ->
            val localAd = rewardedAd

            if (localAd == null) {
                continuation.resume(RewardedAdResult.ERROR)
                return@suspendCancellableCoroutine
            }

            var rewarded = false

            localAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    continuation.resume(
                        if (rewarded) RewardedAdResult.REWARDED
                        else RewardedAdResult.DISMISSED
                    )
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    continuation.resume(RewardedAdResult.ERROR)
                }
            }

            localAd.show(activityProvider()) {
                rewarded = true
            }
        }

    override fun reset() {
        rewardedAd = null
    }
}
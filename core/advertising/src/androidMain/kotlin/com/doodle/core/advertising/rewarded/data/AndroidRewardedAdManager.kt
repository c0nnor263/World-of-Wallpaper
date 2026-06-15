package com.doodle.core.advertising.rewarded.data

import androidx.activity.ComponentActivity
import com.doodle.core.advertising.domain.enums.AdShowResult
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager
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
) : RewardedAdManager() {
    private var rewardedAd: RewardedAd? = null

    override fun hasCachedAd(): Boolean {
        return rewardedAd != null
    }

    override suspend fun platformLoadAd(adUnitID: String): Result<Unit> =
        suspendCancellableCoroutine { cont ->
            RewardedAd.load(
                activityProvider(),
                adUnitID,
                AdRequest.Builder().build(),
                object : RewardedAdLoadCallback() {
                    override fun onAdLoaded(ad: RewardedAd) {
                        rewardedAd = ad
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
            val localAd = rewardedAd ?: run {
                cont.resume(AdShowResult.ERROR); return@suspendCancellableCoroutine
            }

            var rewarded = false

            localAd.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    cont.resume(
                        if (rewarded) AdShowResult.SHOWN_OR_REWARD_EARNED
                        else AdShowResult.DISMISSED
                    )
                }

                override fun onAdFailedToShowFullScreenContent(error: AdError) {
                    cont.resume(AdShowResult.ERROR)
                }
            }

            localAd.show(activityProvider()) {
                rewarded = true
            }
        }

    override fun platformReset() {
        super.platformReset()
        rewardedAd = null
    }
}
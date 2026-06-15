package com.doodle.core.advertising.rewarded.data

import com.doodle.core.advertising.domain.enums.AdShowResult
import com.doodle.core.advertising.rewarded.domain.IosRewardedAdBridge
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

// TODO: Maybe create a common iOS Ad manager
class IosRewardedAdManager(
    private val bridge: IosRewardedAdBridge
) : RewardedAdManager() {

    override fun hasCachedAd(): Boolean {
        return bridge.hasCachedAd()
    }

    override suspend fun platformLoadAd(adUnitID: String): Result<Unit> =
        suspendCancellableCoroutine { continuation ->
            bridge.loadAd(
                adUnitID = adUnitID,
                onSuccess = {
                    if (continuation.isActive) {
                        continuation.resume(Result.success(Unit))
                    }
                },
                onError = { message ->
                    if (continuation.isActive) {
                        continuation.resume(Result.failure(Exception(message)))
                    }
                }
            )
        }

    override suspend fun platformShowAd(): AdShowResult =
        suspendCancellableCoroutine { continuation ->
            bridge.showAd { result ->
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
        }

    override fun platformReset() {
        super.platformReset()
        bridge.reset()
    }
}
package com.doodle.core.advertising.data

import com.doodle.core.advertising.domain.CommonRewardedAd
import com.doodle.core.advertising.domain.IosRewardedAdBridge
import com.doodle.core.advertising.domain.RewardedAdManager
import com.doodle.core.advertising.enums.RewardedAdResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosRewardedAdManager(
    private val bridge: IosRewardedAdBridge
) : RewardedAdManager {

    override suspend fun loadAd(adUnitID: String): Result<CommonRewardedAd> =
        suspendCancellableCoroutine { continuation ->
            bridge.loadAd(
                adUnitID = adUnitID,
                onSuccess = {
                    if (continuation.isActive) {
                        continuation.resume(Result.success(CommonRewardedAd()))
                    }
                },
                onError = { message ->
                    if (continuation.isActive) {
                        continuation.resume(Result.failure(Exception(message)))
                    }
                }
            )
        }

    override suspend fun showAd(): RewardedAdResult =
        suspendCancellableCoroutine { continuation ->
            bridge.showAd { result ->
                if (continuation.isActive) {
                    continuation.resume(result)
                }
            }
        }

    override fun reset() {
        bridge.reset()
    }
}
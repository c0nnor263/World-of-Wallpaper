package com.doodle.core.advertising.appopen.data

import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.appopen.domain.IosAppOpenAdBridge
import com.doodle.core.advertising.domain.enums.AdShowResult
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlin.coroutines.resume

class IosAppOpenAdManager(
    private val bridge: IosAppOpenAdBridge
) : AppOpenAdManager() {
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
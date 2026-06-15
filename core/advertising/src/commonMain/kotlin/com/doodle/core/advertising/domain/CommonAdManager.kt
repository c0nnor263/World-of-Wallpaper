package com.doodle.core.advertising.domain

import com.doodle.core.advertising.DEFAULT_EXPIRE_TIME_HOURS
import com.doodle.core.advertising.data.AdRetryPolicy
import com.doodle.core.advertising.domain.enums.AdShowResult
import com.doodle.core.advertising.domain.enums.AdStatus
import com.doodle.core.advertising.isExpired
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlin.time.Clock

abstract class CommonAdManager {
    private val _adStatus: MutableStateFlow<AdStatus> = MutableStateFlow(AdStatus.EMPTY)
    val adStatus = _adStatus.asStateFlow()
    private val retryPolicy = AdRetryPolicy()
    private var lastLoadTime = 0L

    protected abstract fun hasCachedAd(): Boolean
    protected fun isCachedAdExpired(): Boolean {
        return hasCachedAd() && isExpired(lastLoadTime, DEFAULT_EXPIRE_TIME_HOURS)
    }

    suspend fun loadAd(adUnitID: String) {
        if (adStatus.value == AdStatus.LOADING) return

        _adStatus.value = AdStatus.LOADING

        val cachedExpired = isCachedAdExpired()
        if (hasCachedAd() && !cachedExpired) {
            _adStatus.value = AdStatus.READY_TO_SHOW
            return
        }

        if (cachedExpired) {
            platformReset()
            _adStatus.value = AdStatus.LOADING
        }

        platformLoadAd(adUnitID)
            .fold(
                onSuccess = {
                    lastLoadTime = Clock.System.now().toEpochMilliseconds()
                    retryPolicy.reset()
                    _adStatus.value = AdStatus.READY_TO_SHOW
                },
                onFailure = {
                    _adStatus.value = AdStatus.FAILED_TO_LOAD
                    retryPolicy.retry(
                        block = {
                            loadAd(adUnitID)
                        },
                        onExhausted = {
                            _adStatus.value = AdStatus.CANNOT_LOAD
                        }
                    )
                }
            )
    }

    suspend fun showAd(): AdShowResult {
        if (!hasCachedAd() || adStatus.value != AdStatus.READY_TO_SHOW) return AdShowResult.ERROR
        val result = platformShowAd()
        platformReset()
        return result
    }

    protected abstract suspend fun platformLoadAd(adUnitID: String): Result<Unit>
    protected abstract suspend fun platformShowAd(): AdShowResult
    protected open fun platformReset() {
        retryPolicy.reset()
        _adStatus.value = AdStatus.EMPTY
    }
}
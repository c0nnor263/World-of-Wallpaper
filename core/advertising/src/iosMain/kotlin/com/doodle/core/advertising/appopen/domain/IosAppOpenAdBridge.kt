package com.doodle.core.advertising.appopen.domain

import com.doodle.core.advertising.domain.enums.AdShowResult

interface IosAppOpenAdBridge {
    fun hasCachedAd(): Boolean
    fun loadAd(
        adUnitID: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun showAd(
        onResult: (AdShowResult) -> Unit
    )

    fun reset()
}
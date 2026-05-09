package com.doodle.core.advertising.domain

import com.doodle.core.advertising.enums.RewardedAdResult

interface IosRewardedAdBridge {
    fun loadAd(
        adUnitID: String,
        onSuccess: () -> Unit,
        onError: (message: String) -> Unit
    )

    fun showAd(
        onResult: (RewardedAdResult) -> Unit
    )

    fun reset()
}
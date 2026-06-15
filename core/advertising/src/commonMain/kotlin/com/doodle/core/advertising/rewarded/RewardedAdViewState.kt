package com.doodle.core.advertising.rewarded

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.doodle.core.advertising.CoreAdvertisingBuildKonfig
import com.doodle.core.advertising.domain.enums.AdShowResult
import com.doodle.core.advertising.rewarded.domain.RewardedAdManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberRewardedAdViewState(
    rewardedAdManager: RewardedAdManager,
    adUnitID: String = CoreAdvertisingBuildKonfig.rewarded_ad_unit_id
): RewardedAdViewState {
    val scope = rememberCoroutineScope()
    val state = remember {
        RewardedAdViewState(
            adUnitID = adUnitID,
            scope = scope,
            rewardedAdManager = rewardedAdManager
        )
    }

    LaunchedEffect(state) {
        state.loadAd()
    }

    return state
}

class RewardedAdViewState(
    private val scope: CoroutineScope,
    private val adUnitID: String,
    private val rewardedAdManager: RewardedAdManager
) {
    fun loadAd() {
        scope.launch {
            rewardedAdManager.loadAd(adUnitID)
        }
    }

    fun showAd(
        onDismissed: (AdShowResult) -> Unit = {}
    ) {
        scope.launch {
            val result = rewardedAdManager.showAd()
            loadAd()
            onDismissed(result)
        }
    }
}

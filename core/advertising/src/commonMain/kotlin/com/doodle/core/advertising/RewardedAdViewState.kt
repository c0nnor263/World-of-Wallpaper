package com.doodle.core.advertising

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import com.doodle.core.advertising.data.AdRetryPolicy
import com.doodle.core.advertising.domain.RewardedAdManager
import com.doodle.core.advertising.enums.RewardedAdResult
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
        ).also { state ->
            state.loadOrGetAd()
        }
    }

    LaunchedEffect(state) {
        state.loadOrGetAd()
    }

    return state
}

class RewardedAdViewState(
    private val scope: CoroutineScope,
    private val adUnitID: String,
    private val rewardedAdManager: RewardedAdManager
) {
    private val retryPolicy = AdRetryPolicy(scope)

    var isLoaded by mutableStateOf(false)
        private set

    var isLoading by mutableStateOf(false)
        private set

    fun loadOrGetAd() {
        if (isLoading || isLoaded) return

        isLoading = true
        scope.launch {
            rewardedAdManager
                .loadAd(adUnitID)
                .fold(
                    onSuccess = {
                        isLoaded = true
                        retryPolicy.reset()
                    },
                    onFailure = {
                        isLoaded = false
                        retryPolicy.retry {
                            loadOrGetAd()
                        }
                    }
                )
            isLoading = false
        }
    }

    fun showAd(
        onDismissed: (RewardedAdResult) -> Unit = {}
    ) {
        scope.launch {
            val result = rewardedAdManager.showAd()
            isLoaded = false
            rewardedAdManager.reset()
            loadOrGetAd()
            onDismissed(result)
        }
    }

    fun reset() {
        isLoaded = false
        rewardedAdManager.reset()
        retryPolicy.reset()
    }
}

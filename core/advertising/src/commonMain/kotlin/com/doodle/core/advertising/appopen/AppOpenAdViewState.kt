package com.doodle.core.advertising.appopen

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import com.doodle.core.advertising.CoreAdvertisingBuildKonfig
import com.doodle.core.advertising.appopen.domain.AppOpenAdManager
import com.doodle.core.advertising.domain.enums.AdShowResult
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


@Composable
fun rememberAppOpenAdViewState(
    appOpenAdManager: AppOpenAdManager,
    adUnitID: String = CoreAdvertisingBuildKonfig.app_open_ad_unit_id,
): AppOpenAdViewState {
    val scope = rememberCoroutineScope()
    val state = remember {
        AppOpenAdViewState(
            adUnitID = adUnitID,
            scope = scope,
            appOpenAdManager = appOpenAdManager
        )
    }

    LaunchedEffect(state) {
        state.loadAd()
    }

    return state
}

class AppOpenAdViewState(
    private val scope: CoroutineScope,
    private val adUnitID: String,
    private val appOpenAdManager: AppOpenAdManager
) {
    fun loadAd() {
        scope.launch {
            appOpenAdManager.loadAd(adUnitID)
        }
    }

    fun showAd(
        onDismissed: (AdShowResult) -> Unit = {}
    ) {
        scope.launch {
            val result = appOpenAdManager.showAd()
            loadAd()
            onDismissed(result)
        }
    }
}

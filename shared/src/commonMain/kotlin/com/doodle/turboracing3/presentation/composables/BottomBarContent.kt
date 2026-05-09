package com.doodle.turboracing3.presentation.composables

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.doodle.core.advertising.CoreAdvertisingBuildKonfig
import com.doodle.core.advertising.presentation.BannerAdView
import com.doodle.core.ui.tweenLong

@Composable
fun BottomBarContent(modifier: Modifier = Modifier, isVisible: Boolean) {
    AnimatedVisibility(
        visible = isVisible,
        enter = slideInVertically(tweenLong()) { it },
        exit = slideOutVertically(tweenLong()) { it },
        modifier = modifier
    ) {
        Box(
            modifier = Modifier.fillMaxWidth(),
            contentAlignment = Alignment.Center
        ) {
            BannerAdView(
                modifier = Modifier.fillMaxWidth().height(50.dp),
                adUnitId = CoreAdvertisingBuildKonfig.banner_ad_unit_id
            )
        }
    }
}


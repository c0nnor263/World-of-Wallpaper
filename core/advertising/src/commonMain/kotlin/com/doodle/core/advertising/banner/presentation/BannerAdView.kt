package com.doodle.core.advertising.banner.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun BannerAdView(
    modifier: Modifier = Modifier,
    adUnitId: String
)
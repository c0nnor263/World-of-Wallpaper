package com.doodle.core.advertising

import androidx.compose.runtime.staticCompositionLocalOf
import com.doodle.core.advertising.domain.AdvertisingManager

val LocalAdvertisingManager = staticCompositionLocalOf<AdvertisingManager> {
    error("AdvertisingManager is not provided")
}
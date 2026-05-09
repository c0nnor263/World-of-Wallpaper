package com.doodle.core.ui.state

import androidx.compose.runtime.compositionLocalOf
import com.doodle.core.domain.enums.RemoveAdsStatus


val LocalRemoveAdsStatus = compositionLocalOf {
    RemoveAdsStatus.NOT_PURCHASED
}
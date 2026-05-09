package com.doodle.turboracing3

import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.compose.ui.window.ComposeUIViewController
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.data.IosAdvertisingManager
import com.doodle.core.advertising.domain.IosAdvertisingBridge
import com.doodle.turboracing3.presentation.composables.AppContent


fun MainViewController(
    advertisingBridge: IosAdvertisingBridge
) = ComposeUIViewController {
    CompositionLocalProvider(
        LocalAdvertisingManager provides remember {
            IosAdvertisingManager(advertisingBridge)
        }
    ) {
        AppContent()
    }
}
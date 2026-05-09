package com.doodle.turboracing3.presentation

import android.os.Build
import android.os.Bundle
import android.view.WindowInsets
import android.view.WindowInsetsController
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.remember
import androidx.core.view.WindowCompat
import com.doodle.core.advertising.LocalAdvertisingManager
import com.doodle.core.advertising.data.AndroidAdvertisingManager
import com.doodle.turboracing3.presentation.composables.AppContent

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        setContent {
            CompositionLocalProvider(
                LocalAdvertisingManager provides remember {
                    AndroidAdvertisingManager { this }
                }
            ) {
                AppContent()
            }
        }
        setFullscreen()
    }

    private fun setFullscreen() {
        WindowCompat.setDecorFitsSystemWindows(window, false)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.R) {
            window.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
        } else {
            window.insetsController?.apply {
                systemBarsBehavior = WindowInsetsController.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                hide(WindowInsets.Type.systemBars())
            }
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            setDisplayCutoutMode()
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    private fun setDisplayCutoutMode() {
        window.attributes.layoutInDisplayCutoutMode =
            WindowManager.LayoutParams.LAYOUT_IN_DISPLAY_CUTOUT_MODE_SHORT_EDGES
    }
}
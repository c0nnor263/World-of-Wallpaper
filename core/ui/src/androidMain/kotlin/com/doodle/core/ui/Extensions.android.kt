package com.doodle.core.ui

import androidx.compose.foundation.layout.displayCutoutPadding
import androidx.compose.ui.Modifier

actual fun Modifier.platformDisplayCutoutPadding(): Modifier {
    return this.displayCutoutPadding()
}
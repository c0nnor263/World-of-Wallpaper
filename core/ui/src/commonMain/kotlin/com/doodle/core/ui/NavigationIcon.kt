package com.doodle.core.ui

import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.painter.Painter

@Composable
fun NavigationIcon(painter: Painter, onClick: () -> Unit) {
    IconButton(
        onClick = onClick
    ) {
        Icon(
            painter = painter,
            contentDescription = null
        )
    }
}

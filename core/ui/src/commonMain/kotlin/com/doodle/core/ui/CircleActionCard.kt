package com.doodle.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun CircleActionCard(
    modifier: Modifier = Modifier,
    buttonScale: Float = 1F,
    borderWidthAnimation: Dp,
    content: @Composable () -> Unit
) {
    Card(
        elevation = CardDefaults.cardElevation(4.dp),
        shape = CircleShape,
        colors = CardDefaults.cardColors(MaterialTheme.colorScheme.secondary),
        border = BorderStroke(
            width = borderWidthAnimation,
            color = MaterialTheme.colorScheme.secondaryContainer
        ),
        modifier = modifier
            .padding(8.dp)
            .scale(buttonScale)
    ) {
        content()
    }
}

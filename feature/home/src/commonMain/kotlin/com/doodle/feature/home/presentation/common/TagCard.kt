package com.doodle.feature.home.presentation.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.doodle.core.ui.card.CardImage
import com.doodle.core.ui.scaleWithPressAnimation

@Composable
fun TagCard(
    modifier: Modifier = Modifier,
    title: String,
    previewURL: String,
    onNavigateToSearch: (String) -> Unit
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    CardImage(modifier = modifier) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .height(75.dp)
                .scaleWithPressAnimation(isPressed.value)
                .clickable(onClick = { onNavigateToSearch(title) })
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxWidth()
                    .defaultMinSize(minHeight = 200.dp),
                model = previewURL,
                contentDescription = null,
                contentScale = ContentScale.Crop
            )
            Text(
                text = title,
                style = MaterialTheme.typography.titleLarge.copy(
                    shadow = Shadow(
                        color = Color.Black,
                        offset = Offset(0F, 10F),
                        blurRadius = 50f
                    )
                ),
                color = Color.White,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )
        }
    }
}

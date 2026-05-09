package com.doodle.core.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.doodle.core.ui.card.CardImage
import org.jetbrains.compose.resources.painterResource
import worldofwallpapers.core.ui.generated.resources.Res
import worldofwallpapers.core.ui.generated.resources.editors_choice

@Composable
fun FetchedImageItem(
    modifier: Modifier = Modifier,
    isPremium: Boolean,
    previewURL: String,
    onNavigateToDetails: () -> Unit
) {
    val premiumBlur = if (isPremium) 5.dp else 0.dp
    CardImage(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(onClick = onNavigateToDetails),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .defaultMinSize(minHeight = 200.dp)
                    .blur(premiumBlur, premiumBlur),
                model = previewURL,
                contentDescription = null,
                contentScale = ContentScale.Crop,
            )
            if (isPremium) PremiumBadge()
        }
    }
}

@Composable
fun BoxScope.PremiumBadge() {
    Image(
        painter = painterResource(Res.drawable.editors_choice),
        contentDescription = null,
        modifier = Modifier
            .align(Alignment.BottomEnd)
            .padding(8.dp),
        colorFilter = ColorFilter.tint(Color.White)
    )
}



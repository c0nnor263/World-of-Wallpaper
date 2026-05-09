package com.doodle.core.ui

import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex

@Composable
fun ErrorMessage(errorMsg: String?, onErrorClick: () -> Unit) {
    val isError = errorMsg != null
    androidx.compose.animation.AnimatedVisibility(
        visible = isError,
        enter = slideInVertically(tweenMedium()) { -it },
        exit = slideOutVertically(tweenMedium()) { -it },
        modifier = Modifier
            .fillMaxWidth()
            .zIndex(-1F)
    ) {
        val msg by remember { mutableStateOf(errorMsg ?: "") }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.error)
                .clickable { if (isError) onErrorClick() },
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = msg,
                modifier = Modifier.padding(16.dp),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onError
            )
        }
    }
}
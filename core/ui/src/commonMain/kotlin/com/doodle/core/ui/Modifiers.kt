package com.doodle.core.ui

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerInputChange
import androidx.compose.ui.input.pointer.pointerInput

@Composable
fun Modifier.scaleWithPressAnimation(
    isPressed: Boolean,
    from: Float = 1F,
    to: Float = 0.6F
): Modifier {
    val scaleAnimation by animateFloatAsState(
        targetValue = if (isPressed) to else from,
        animationSpec = tweenEasy(),
        label = "Default press scale animation"
    )
    return this then Modifier.scale(scaleAnimation)
}

fun Modifier.captureChildrenGestures(enabled: Boolean = true, onChildrenClick: () -> Unit = {}) =
    if (enabled) {
        pointerInput(Unit) {
            awaitPointerEventScope {
                onChildrenClick()
                while (true) {
                    awaitPointerEvent(pass = PointerEventPass.Initial)
                        .changes
                        .forEach(PointerInputChange::consume)
                }
            }
        }
    } else {
        this
    }

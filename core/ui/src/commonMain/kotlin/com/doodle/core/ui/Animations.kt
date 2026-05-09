package com.doodle.core.ui

import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.tween

fun <T> tweenEasy(delayMillis: Int = 0, easing: Easing = FastOutSlowInEasing) =
    tween<T>(durationMillis = 150, delayMillis = delayMillis, easing = easing)

fun <T> tweenMedium(delayMillis: Int = 0, easing: Easing = FastOutSlowInEasing) =
    tween<T>(durationMillis = 500, delayMillis = delayMillis, easing = easing)

fun <T> tweenLong(delayMillis: Int = 0, easing: Easing = FastOutSlowInEasing) =
    tween<T>(durationMillis = 1000, delayMillis = delayMillis, easing = easing)

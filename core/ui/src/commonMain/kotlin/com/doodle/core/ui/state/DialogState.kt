package com.doodle.core.ui.state

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.time.Duration


@Composable
fun rememberDialogState(initial: Boolean = false): DialogState {
    val scope = rememberCoroutineScope()
    return remember {
        DialogState(initial, scope)
    }
}

class DialogState(
    initial: Boolean,
    private val scope: CoroutineScope
) {
    private var autoDismissJob: Job? = null
    var isVisible by mutableStateOf(initial)
        private set

    fun show() {
        autoDismissJob?.cancel()
        isVisible = true
    }

    fun showFor(duration: Duration) {
        autoDismissJob?.cancel()
        isVisible = true
        autoDismissJob = scope.launch {
            delay(duration)
            isVisible = false
        }
    }

    fun dismiss() {
        autoDismissJob?.cancel()
        isVisible = false
    }
}
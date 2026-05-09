package com.doodle.feature.home.state

import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberHomeDrawerState(initialValue: DrawerValue = DrawerValue.Closed): HomeDrawerState {
    val scope = rememberCoroutineScope()
    val state = rememberDrawerState(initialValue = initialValue)
    return remember {
        HomeDrawerState(
            scope = scope,
            state = state
        )
    }
}

class HomeDrawerState(
    private val scope: CoroutineScope,
    val state: DrawerState
) {

    private val offset: Float
        get() = state.currentOffset

    var width by mutableStateOf(0F)

    val fraction: Float
        get() {
            if (width <= 0f || offset.isNaN()) return 0f
            val result = ((offset + width) / width)
            return result
        }

    fun open() {
        scope.launch {
            state.open()
        }
    }

    fun close() {
        scope.launch {
            state.close()
        }
    }

    fun updateWidth(value: Float) {
        if (width == 0f) {
            width = value
        }
    }

    fun showOrClose() {
        if (state.isClosed) {
            open()
        } else {
            close()
        }
    }
}

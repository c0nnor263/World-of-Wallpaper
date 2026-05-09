package com.doodle.core.advertising.data

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class AdRetryPolicy(
    private val scope: CoroutineScope,
    private var retryCount: Int = MAX_RETRY_COUNT,
    private val delayMillis: Long = RETRY_DELAY
) {
    companion object {
        const val MAX_RETRY_COUNT = 2
        const val RETRY_DELAY = 2000L
    }

    fun retry(block: () -> Unit) {
        if (retryCount > 0) {
            retryCount--
            scope.launch {
                delay(delayMillis)
                block()
            }
        }
    }

    fun reset() {
        retryCount = MAX_RETRY_COUNT
    }
}
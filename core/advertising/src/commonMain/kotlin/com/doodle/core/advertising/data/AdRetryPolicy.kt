package com.doodle.core.advertising.data

import kotlinx.coroutines.delay
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

class AdRetryPolicy(
    private var retryCount: Int = MAX_RETRY_COUNT,
    private val delayDuration: Duration = RETRY_DELAY_IN_SECONDS.seconds
) {
    companion object {
        const val MAX_RETRY_COUNT = 2
        const val RETRY_DELAY_IN_SECONDS = 2L
    }

    suspend fun retry(block: suspend () -> Unit, onExhausted: () -> Unit) {
        if (retryCount > 0) {
            retryCount--
            delay(delayDuration)
            block()
        } else {
            onExhausted()
        }
    }

    fun reset() {
        retryCount = MAX_RETRY_COUNT
    }
}
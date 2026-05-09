package com.doodle.core.advertising

import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

fun wasLoadTimeLessThanLimitHoursAgo(lastLoadTime: Long, hours: Long): Boolean {
    val timeDifference: Long = Clock.System.now().toEpochMilliseconds() - lastLoadTime
    val limitHours = hours.hours.inWholeMilliseconds
    return timeDifference < limitHours
}

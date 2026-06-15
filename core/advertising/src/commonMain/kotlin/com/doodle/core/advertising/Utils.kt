package com.doodle.core.advertising

import kotlin.time.Clock
import kotlin.time.Duration.Companion.hours

fun isExpired(lastLoadTime: Long, limitHoursTime: Long): Boolean {
    if (lastLoadTime <= 0) return false
    val timeDifference: Long = Clock.System.now().toEpochMilliseconds() - lastLoadTime
    val limitHours = limitHoursTime.hours.inWholeMilliseconds
    return timeDifference > limitHours
}

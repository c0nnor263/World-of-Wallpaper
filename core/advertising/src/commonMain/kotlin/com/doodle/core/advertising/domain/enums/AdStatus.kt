package com.doodle.core.advertising.domain.enums

enum class AdStatus {
    EMPTY,
    LOADING,
    READY_TO_SHOW,
    FAILED_TO_LOAD,
    CANNOT_LOAD
}

fun AdStatus.isReadyToShow(): Boolean {
    return this == AdStatus.READY_TO_SHOW
}

fun AdStatus.isFinishLoading(): Boolean {
    return isReadyToShow() || this == AdStatus.CANNOT_LOAD
}
